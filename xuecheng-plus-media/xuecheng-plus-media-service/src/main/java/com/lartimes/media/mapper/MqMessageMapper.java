package com.lartimes.media.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lartimes.media.model.po.MqMessage;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author itcast
 */
@Mapper
public interface MqMessageMapper extends BaseMapper<MqMessage> {

}
