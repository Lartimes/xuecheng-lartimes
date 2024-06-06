package com.lartimes.media.service.jobhandler;

import com.lartimes.media.service.MediaFilesService;
import com.lartimes.media.service.MediaProcessService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Lartimes
 * @version 1.0
 * @description:
 * @since 2024/4/24 23:40
 */
@Component
@Slf4j
public class BroadCastHandler {



    @Autowired
    private MediaFilesService mediaFilesService;


    @Autowired
    private MediaProcessService mediaProcessService;

    @XxlJob("shardingJobHandler")
    public void shardingJobHandler() throws Exception {
        // 分片参数
        int shardIndex = XxlJobHelper.getShardIndex();
        int shardTotal = XxlJobHelper.getShardTotal();
        XxlJobHelper.log("分片参数：当前分片序号 = {}, 总分片数 = {}", shardIndex, shardTotal);
        // 业务逻辑
        for (int i = 0; i < shardTotal; i++) {
            if (i == shardIndex) {
                XxlJobHelper.log("第 {} 片, 命中分片开始处理", i);
            } else {
                XxlJobHelper.log("第 {} 片, 忽略", i);
            }
        }

    }
}
