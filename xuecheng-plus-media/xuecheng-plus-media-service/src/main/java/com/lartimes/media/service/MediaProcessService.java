package com.lartimes.media.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lartimes.media.model.po.MediaProcess;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author itcast
 * @since 2024-04-18
 */
public interface MediaProcessService extends IService<MediaProcess> {


     void castVideoFile(int shardTotal , int shardIndex);
}