package com.lartimes.media.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;
import com.lartimes.content.exception.XueChengPlusException;
import com.lartimes.content.model.PageParams;
import com.lartimes.content.model.PageResult;
import com.lartimes.content.model.RestResponse;
import com.lartimes.media.mapper.MediaFilesMapper;
import com.lartimes.media.model.dto.MediaFilesDTO;
import com.lartimes.media.model.dto.UploadFileParamsDto;
import com.lartimes.media.model.dto.UploadFileResultDto;
import com.lartimes.media.model.po.MediaFiles;
import com.lartimes.media.service.MediaFilesService;
import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * 媒资信息 服务实现类
 * </p>
 *
 * @author itcast
 */
@Slf4j
@Service
public class MediaFilesServiceImpl extends ServiceImpl<MediaFilesMapper, MediaFiles> implements MediaFilesService {

    private final MinioClient minioClient;
    private final MediaFilesMapper mediaFilesMapper;
    @Value("${minio.bucket.videofiles}")
    String bucket_videofiles;
    private MediaFilesService currentProxy;
    //普通文件桶
    @Value("${minio.bucket.files}")
    private String bucket_Files;

    public MediaFilesServiceImpl(MediaFilesMapper mediaFilesMapper, MinioClient minioClient) {
        this.mediaFilesMapper = mediaFilesMapper;
        this.minioClient = minioClient;
    }

    @Autowired
    public void setCurrentProxy(MediaFilesService currentProxy) {
        this.currentProxy = currentProxy;
    }

    @Override
    public UploadFileResultDto uploadFile(Long companyId, UploadFileParamsDto uploadFileParamsDto, String localFilePath) {
        File file = new File(localFilePath);
        if (!file.exists()) {
            XueChengPlusException.cast("文件不存在");
        }
        String fileName = uploadFileParamsDto.getFilename();
        String extension = fileName.substring(fileName.indexOf("."));
        String mimeType = getMimeType(extension);
        String defaultFolderPath = getDefaultFolderPath();
        String md5 = getFileMd5(file);
        String objectName = defaultFolderPath + md5 + extension;
        addMediaFilesToMinIO(localFilePath, mimeType, bucket_Files, objectName);
        uploadFileParamsDto.setFileSize(file.length());
//        代理对象调用事务对象,使事务正确处理
        MediaFiles mediaFiles = currentProxy.addMediaFilesToDb(companyId, md5, uploadFileParamsDto, bucket_Files, objectName);
        UploadFileResultDto uploadFileResultDto = new UploadFileResultDto();
        BeanUtils.copyProperties(mediaFiles, uploadFileResultDto);
        return uploadFileResultDto;
    }

    public void addMediaFilesToMinIO(String localFilePath, String mimeType, String bucket, String objectName) {
        try {
            UploadObjectArgs build = UploadObjectArgs.builder().bucket(bucket).object(objectName).filename(localFilePath).contentType(mimeType).build();
            minioClient.uploadObject(build);
            log.debug("上传文件到minio成功,bucket:{},objectName:{}", bucket, objectName);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            log.error("上传文件到minio出错,bucket:{},objectName:{},错误原因:{}", bucket, objectName, e.getMessage(), e);
            XueChengPlusException.cast("上传文件到文件系统失败");
        }
    }

    @Override
    @Transactional
    public MediaFiles addMediaFilesToDb(Long companyId, String fileMd5, UploadFileParamsDto uploadFileParamsDto, String bucket, String objectName) {
        MediaFiles mediaFiles = mediaFilesMapper.selectById(fileMd5);
        if (mediaFiles != null) {
            return mediaFiles;
        }
        mediaFiles = new MediaFiles();
        //拷贝基本信息
        BeanUtils.copyProperties(uploadFileParamsDto, mediaFiles);
        mediaFiles.setId(fileMd5);
        mediaFiles.setCompanyId(companyId);
        mediaFiles.setFileId(fileMd5);
        mediaFiles.setBucket(bucket);
        mediaFiles.setUrl("/" + bucket + "/" + objectName);
        mediaFiles.setFilePath(objectName);
        mediaFiles.setCreateDate(LocalDateTime.now());
        mediaFiles.setAuditStatus("002003");
        mediaFiles.setStatus("1");

        int insert = mediaFilesMapper.insert(mediaFiles);
        if (insert < 0) {
            log.error("保存文件信息到数据库失败,{}", mediaFiles);
            XueChengPlusException.cast("保存文件信息失败");
        }
        log.debug("保存文件信息到数据库成功,{}", mediaFiles);
        return mediaFiles;
    }

    @SneakyThrows
    @Override
    public RestResponse<Boolean> checkExists(String md5) {
        MediaFiles mediaFiles = mediaFilesMapper.selectById(md5);
        if (mediaFiles != null) {
            String bucket = mediaFiles.getBucket();
            String filePath = mediaFiles.getFilePath();
            GetObjectArgs objArgs = GetObjectArgs.builder().bucket(bucket).object(filePath).build();
            try (InputStream stream = minioClient.getObject(objArgs)) {
                if (stream != null) {
                    log.debug("存在");
                    return RestResponse.success(true);
                }
            } catch (Exception ignored) {
            }
        }
        log.debug("不存在");
        return RestResponse.success(false);
    }

    @SneakyThrows
    @Override
    public RestResponse<Boolean> checkChunk(String fileMd5, int chunkIndex) {
        String chunkPath = getChunkFileFolderPath(fileMd5);
        String filePath = chunkPath + chunkIndex;
        GetObjectArgs objArgs = GetObjectArgs.builder().bucket(bucket_videofiles).object(filePath).build();
        try (InputStream stream = minioClient.getObject(objArgs)) {
            if (stream != null) {
                return RestResponse.success(true);
            }
        } catch (Exception ignored) {
        }
        return RestResponse.success(false);
    }

    //得到分块文件的目录
    private String getChunkFileFolderPath(String fileMd5) {
        return fileMd5.charAt(0) + "/" + fileMd5.charAt(1) + "/" + fileMd5 + "/chunk/";
    }


    @SneakyThrows
    @Override
    public RestResponse<Boolean> uploadChunk(String fileMd5, int chunk, byte[] bytes) {
        String chunkFileFolderPath = getChunkFileFolderPath(fileMd5);
        String filePath = chunkFileFolderPath + chunk;
        String tmpFilePath = createTmpFile(bytes);
        addMediaFilesToMinIO(tmpFilePath, "video/mp4", bucket_videofiles, filePath);
        return checkExists(fileMd5);
    }

    @Override
    public RestResponse<Boolean> mergechunks(Long companyId, String fileMd5, int chunkTotal, UploadFileParamsDto uploadFileParamsDto) {
        final String chunkFileFolderPath = getChunkFileFolderPath(fileMd5);
        String filename = uploadFileParamsDto.getFilename();
        final String extension = filename.substring(filename.indexOf("."));
        final String objName = chunkFileFolderPath.substring(0, chunkFileFolderPath.length() - "/chunk/".length() )
                +extension;
//        /chunk/
        List<ComposeSource> sources = Stream.iterate(0, i -> ++i).limit(chunkTotal).map(i -> ComposeSource.builder().bucket(bucket_videofiles).object(chunkFileFolderPath.concat(Integer.toString(i))).build()).collect(Collectors.toList());
        ComposeObjectArgs composeObjectArgs = ComposeObjectArgs.builder().bucket(bucket_videofiles).object(objName)
                        .sources(sources).build();
        try {
            minioClient.composeObject(composeObjectArgs);
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                 InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException |
                 XmlParserException e) {
            log.error("合并失败{}", composeObjectArgs);
            XueChengPlusException.cast("合并失败:" + composeObjectArgs);
        }
//          MD5
        File file = downloadFromMinIo(bucket_videofiles, objName);
        try (FileInputStream input = new FileInputStream(file)) {
            String md5 = DigestUtils.md5DigestAsHex(input);
            if (!fileMd5.equals(md5)) {
                XueChengPlusException.cast("md5校验失败");
            }
            uploadFileParamsDto.setFileSize(Objects.requireNonNull(file).length());
        } catch (IOException e) {
            log.error("md5校验失败");
//            删除之前下载的
            deleteSplices(chunkTotal, chunkFileFolderPath);
            return RestResponse.success(false);
        } finally {
            file.delete();
        }
//DB
        currentProxy.addMediaFilesToDb(companyId, fileMd5, uploadFileParamsDto, bucket_videofiles, objName);
//删除小文件
        deleteSplices(chunkTotal, chunkFileFolderPath);
        return checkExists(fileMd5);
    }

    @Override
    public PageResult<MediaFiles> getMediasByPage(PageParams pages, MediaFilesDTO mediaFilesDTO) {
        LambdaQueryWrapper<MediaFiles> query = new LambdaQueryWrapper<MediaFiles>();
        String filename = mediaFilesDTO.getFilename();
        String fileType = mediaFilesDTO.getFileType();
        query.eq(StringUtils.hasText(fileType), MediaFiles::getFileType, fileType).like(StringUtils.hasText(filename), MediaFiles::getFilename, filename);
        Page<MediaFiles> courseBasePage = new Page<>(pages.getPageNo(), pages.getPageSize());
        Page<MediaFiles> pageResult = mediaFilesMapper.selectPage(courseBasePage, query);
        long counts = pageResult.getTotal();
        List<MediaFiles> records = pageResult.getRecords();
        return new PageResult<MediaFiles>(records, counts, pages.getPageNo(), pages.getPageSize());
    }

    private void deleteSplices(int chunkTotal, String chunkFileFolderPath) {
        List<DeleteObject> deleteObjects = Stream.iterate(0, i -> ++i).limit(chunkTotal).map(i -> new DeleteObject(chunkFileFolderPath.concat(Integer.toString(i)))).collect(Collectors.toList());
        RemoveObjectsArgs removeObjectsArgs = RemoveObjectsArgs.builder().bucket(bucket_videofiles).objects(deleteObjects).build();
        Iterable<Result<DeleteError>> results = minioClient.removeObjects(removeObjectsArgs);
        results.forEach(r -> {
            DeleteError deleteError = null;
            try {
                deleteError = r.get();
            } catch (Exception e) {
                log.error("删除失败:{}", deleteError);
            }
        });
    }

    @SneakyThrows
    private File downloadFromMinIo(String bucket, String objName) {
        File minioFile = File.createTempFile("minio", ".merge");
        try (FileOutputStream outputStream = new FileOutputStream(minioFile); InputStream input = minioClient
                .getObject(GetObjectArgs.builder().bucket(bucket).object(objName).build())) {
            IOUtils.copy(input, outputStream);
        }
        return minioFile;
    }


    private String createTmpFile(byte[] bytes) {
        File tempFile = null;
        try {
            tempFile = File.createTempFile("chunk", ".chunk");
            try (FileOutputStream output = new FileOutputStream(tempFile)) {
                output.write(bytes);
            } catch (IOException e) {
                XueChengPlusException.cast("tmpFile Output Error");
            }
        } catch (IOException e) {
            XueChengPlusException.cast("tmpFile create Error");
        }
        return tempFile.getAbsolutePath();
    }


    //获取文件默认存储目录路径 年/月/日
    private String getDefaultFolderPath() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        return sdf.format(new Date());
    }

    //获取文件的md5
    private String getFileMd5(File file) {
        try (FileInputStream input = new FileInputStream(file)) {
            return DigestUtils.md5DigestAsHex(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getMimeType(String extension) {
        if (extension == null) extension = "";
        //根据扩展名取出mimeType
        ContentInfo extensionMatch = ContentInfoUtil.findExtensionMatch(extension);
        //通用mimeType，字节流
        String mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        if (extensionMatch != null) {
            mimeType = extensionMatch.getMimeType();
        }
        return mimeType;
    }


}
