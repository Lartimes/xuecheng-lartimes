package com.lartimes.media.service.jobhandler;

import com.lartimes.content.utils.Mp4VideoUtil;
import com.lartimes.media.model.po.MediaProcess;
import com.lartimes.media.service.MediaFilesService;
import com.lartimes.media.service.MediaProcessService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Lartimes
 * @version 1.0
 * @description:
 * @since 2024/6/6 13:10
 */
@Slf4j
@Component
public class VideoTaskHandler {

    private final MediaProcessService mediaProcessService;
    private final MediaFilesService mediaFilesService;

    public VideoTaskHandler(MediaFilesService mediaFilesService, MediaProcessService mediaProcessService) {
        this.mediaFilesService = mediaFilesService;
        this.mediaProcessService = mediaProcessService;
    }


    @XxlJob("videoJobHandler")
    public void videoJobHandler() throws InterruptedException {
        System.out.println("==================");
        XxlJobHelper.log("XXL-JOB, Hello World.");
        int shardIndex = XxlJobHelper.getShardIndex();
        int shardTotal = XxlJobHelper.getShardTotal();
        XxlJobHelper.log("processes 为空 not");
        List<MediaProcess> processes = null;
        try {
            int count = Runtime.getRuntime().availableProcessors();
            processes = mediaProcessService.getMediaProcessList(shardIndex, shardTotal, count);
            if (processes.isEmpty()) {
                log.debug("processes 为空");
                XxlJobHelper.log("processes 为空");
                return;
            }
            processes = processes.stream().filter(process -> process.getFilename().endsWith(".avi")).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        int size = processes.size();
        System.out.println("size" + size);
        ExecutorService threadPool = Executors.newFixedThreadPool(size);
        CountDownLatch countDownLatch = new CountDownLatch(size);
        processes.forEach(mediaProcess -> {
            threadPool.execute(() -> {
                try {
                    Long id = mediaProcess.getId();
                    if (!mediaProcessService.startTask(id)) {
                        return;
                    }
                    log.debug("开始执行任务:{}", mediaProcess);
                    String bucket = mediaProcess.getBucket();
                    String fileId = mediaProcess.getFileId();
                    String filePath = mediaProcess.getFilePath();
                    String objName = getFilePath(fileId, ".avi");
                    File originalFile = mediaFilesService.downloadFromMinIo(bucket, objName);
                    if (originalFile == null) {
                        log.debug("下载待处理文件失败,originalFile:{}", mediaProcess.getBucket().concat(mediaProcess.getFilePath()));
                        mediaProcessService.saveProcessFinishStatus(id, "3", fileId, null, "下载待处理文件失败");
                        return;
                    }
                    log.debug("开始创建临时文件");
                    File mp4File = null;
                    try {
                        mp4File = File.createTempFile("mp4", ".mp4");
                    } catch (IOException e) {
                        log.error("创建mp4临时文件失败");
                        mediaProcessService.saveProcessFinishStatus(id, "3", fileId, null, "创建mp4临时文件失败");
                        return;
                    }
                    log.debug("开始执行任务-格式转换:{}", mediaProcess);
                    try {
                        Mp4VideoUtil mp4VideoUtil = new Mp4VideoUtil(null, originalFile.getAbsolutePath(), mp4File.getName(), mp4File.getAbsolutePath());
                        String result = mp4VideoUtil.generateMp4();
                        if (!"success".equals(result)) {
                            //记录错误信息
                            log.error("处理视频失败,视频地址:{},错误信息:{}", bucket + filePath, result);
                            mediaProcessService.saveProcessFinishStatus(id, "3", fileId, null, result);
                            return;
                        }
                    } catch (Exception e) {
                        log.error("处理视频失败: bucket : {} , filePath: {}", bucket, filePath);
                        mediaProcessService.saveProcessFinishStatus(id, "3", fileId, null, "转码错误");
                        return;
                    }
//                上传

                    String objectName = getFilePath(fileId, ".mp4");
                    //访问url
                    String url = "/" + bucket + "/" + objectName;
                    try {
                        mediaFilesService.addMediaFilesToMinIO(mp4File.getAbsolutePath(), "video/mp4", bucket, objName);
                    } catch (Exception e) {
                        log.debug("上传失败 : {}", mp4File);
                        mediaProcessService.saveProcessFinishStatus(id, "3", fileId, url, "上传失败");
                        return;
                    }
                    mediaProcessService.saveProcessFinishStatus(id, "2", fileId, url, "");
                } finally {
                    countDownLatch.countDown();
                }
            });
        });
        countDownLatch.await(30, TimeUnit.MINUTES);
    }

    private String getFilePath(String fileMd5, String fileExt) {
        return fileMd5.charAt(0) + "/" + fileMd5.charAt(1) + "/" + fileMd5 + "/" + fileMd5 + fileExt;
    }


}
