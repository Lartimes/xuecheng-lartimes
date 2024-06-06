package com.lartimes.media.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lartimes.media.model.po.MediaProcess;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author itcast
 * @since 2024-04-18
 */
public interface MediaProcessService extends IService<MediaProcess> {

      boolean startTask(Long id);

      void saveProcessFinishStatus(Long taskId,String status,String fileId,String url,String errorMsg);




      List<MediaProcess> getMediaProcessList(int shardIndex, int shardTotal , int count );
}
