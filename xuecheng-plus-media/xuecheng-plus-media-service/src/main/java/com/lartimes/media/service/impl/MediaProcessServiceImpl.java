package com.lartimes.media.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lartimes.media.mapper.MediaFilesMapper;
import com.lartimes.media.mapper.MediaProcessHistoryMapper;
import com.lartimes.media.mapper.MediaProcessMapper;
import com.lartimes.media.model.po.MediaFiles;
import com.lartimes.media.model.po.MediaProcess;
import com.lartimes.media.model.po.MediaProcessHistory;
import com.lartimes.media.service.MediaProcessService;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author itcast
 */
@Slf4j
@Service
public class MediaProcessServiceImpl extends ServiceImpl<MediaProcessMapper, MediaProcess> implements MediaProcessService {

    private final MediaFilesMapper mediaFilesMapper;
    private final MediaProcessMapper mediaProcessMapper;
    private final MinioClient minioClient;

    private final MediaProcessHistoryMapper mediaProcessHistoryMapper;


    public MediaProcessServiceImpl(MediaProcessMapper mediaProcessMapper, MinioClient minioClient, MediaFilesMapper mediaFilesMapper, MediaProcessHistoryMapper mediaProcessHistoryMapper) {
        this.mediaProcessMapper = mediaProcessMapper;
        this.minioClient = minioClient;
        this.mediaFilesMapper = mediaFilesMapper;
        this.mediaProcessHistoryMapper = mediaProcessHistoryMapper;
    }

    @SneakyThrows
    private File downLoadVideo(MediaProcess mediaProcess) {
        String bucket = mediaProcess.getBucket();
        String filePath = mediaProcess.getFilePath();
        String filename = mediaProcess.getFilename();
        File minioFile = File.createTempFile("minio", filename.substring(filename.indexOf(".")));
        try (FileOutputStream outputStream = new FileOutputStream(minioFile); InputStream input = minioClient.getObject(GetObjectArgs.builder().bucket(bucket).object(filePath).build())) {
            IOUtils.copy(input, outputStream);
        }
        return minioFile;
    }



    @Override
    public boolean startTask(Long id) {
        int result = mediaProcessMapper.startTask(id);
        return result == 1;
    }

    @Override
    public void saveProcessFinishStatus(Long taskId, String status, String fileId, String url, String errorMsg) {
        MediaProcess mediaProcess = mediaProcessMapper.selectById(taskId);
        if (mediaProcess == null) {
            return;
        }
        LambdaQueryWrapper<MediaProcess> mediaError = new LambdaQueryWrapper<MediaProcess>().eq(MediaProcess::getId, taskId);
        mediaProcess.setFinishDate(LocalDateTime.now());
        if("3".equals(status)){
            mediaProcess.setFailCount(mediaProcess.getFailCount()+1);
            mediaProcess.setErrormsg(errorMsg);
            mediaProcessMapper.update(mediaProcess, mediaError);
            log.debug("更新任务处理状态为失败，任务信息:{}",mediaProcess);
            return;
        }
//         String fileId, String url
        MediaFiles mediaFiles = mediaFilesMapper.selectById(fileId);
        if(mediaFiles != null){
            mediaFiles.setUrl(url);
            mediaFiles.setChangeDate(LocalDateTime.now());
            mediaFilesMapper.updateById(mediaFiles);
            return;
        }
        mediaProcess.setStatus("2");
        mediaProcess.setUrl(url);
        mediaProcessMapper.updateById(mediaProcess);

        MediaProcessHistory mediaProcessHistory = new MediaProcessHistory();
        BeanUtils.copyProperties(mediaProcess , mediaProcessHistory);
        mediaProcessHistoryMapper.insert(mediaProcessHistory);
        //删除mediaProcess
        mediaProcessMapper.deleteById(mediaProcess.getId());
    }

    @Override
    public List<MediaProcess> getMediaProcessList(int shardIndex, int shardTotal , int count ) {
        return mediaProcessMapper.getToResolvedMediaFiles(shardIndex, shardTotal , count);
    }


}
