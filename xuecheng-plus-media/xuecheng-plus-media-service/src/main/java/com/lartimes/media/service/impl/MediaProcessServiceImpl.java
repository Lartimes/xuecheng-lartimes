package com.lartimes.media.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lartimes.media.mapper.MediaProcessMapper;
import com.lartimes.media.model.po.MediaProcess;
import com.lartimes.media.service.MediaProcessService;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author itcast
 */
@Slf4j
@Service
public class MediaProcessServiceImpl extends ServiceImpl<MediaProcessMapper, MediaProcess> implements MediaProcessService {


    private final MediaProcessMapper mediaProcessMapper;

    public MediaProcessServiceImpl(MediaProcessMapper mediaProcessMapper, MinioClient minioClient) {
        this.mediaProcessMapper = mediaProcessMapper;
        this.minioClient = minioClient;
    }

    /**
     * 查询待处理的mediaProcess
     * @param shardTotal
     */
    @Override
    public void castVideoFile(int shardTotal , int shardIndex) {
        Page<MediaProcess> mediaProcessPage = mediaProcessMapper.selectPage(new Page<>(0, shardTotal),
                new LambdaQueryWrapper<MediaProcess>()
                        .eq(true, MediaProcess::getStatus, "1"));
        MediaProcess mediaProcess = mediaProcessPage.getRecords().stream().filter(
                (record) -> record.getId() % shardTotal == shardIndex).findFirst().orElse(null);
        if(mediaProcess != null){
            try {
                mediaProcess.setStatus("2");
                mediaProcessMapper.updateById(mediaProcess);
                File file = downLoadVideo(mediaProcess);
//                进行格式转换，
//                TODO 明天写
//                Mp4VideoUtil videoUtil = new Mp4VideoUtil(null , video_path,mp4_name,mp4_path);
//                开始视频转换，成功将返回success
//                String s = videoUtil.generateMp4();
//                System.out.println(s);
//                进行上传

//                更新记录
                log.debug("处理成功");
            }catch (Exception e){
                mediaProcess.setStatus("3");
                mediaProcess.setErrormsg(e.getMessage());
                mediaProcessMapper.updateById(mediaProcess);
                log.error("处理失败");
            }

        }
    }


    private final MinioClient minioClient;
    @SneakyThrows
    private File downLoadVideo(MediaProcess mediaProcess) {
        String bucket = mediaProcess.getBucket();
        String filePath = mediaProcess.getFilePath();
        String filename = mediaProcess.getFilename();
        File minioFile = File.createTempFile("minio", filename.substring(filename.indexOf(".")));
        try (FileOutputStream outputStream = new FileOutputStream(minioFile); InputStream input = minioClient
                .getObject(GetObjectArgs.builder().bucket(bucket).object(filePath).build())) {
            IOUtils.copy(input, outputStream);
        }
        return minioFile;
    }
}
