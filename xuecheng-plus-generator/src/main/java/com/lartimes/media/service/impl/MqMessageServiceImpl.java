package com.lartimes.media.service.impl;

import com.lartimes.media.model.po.MqMessageDTO;
import com.lartimes.media.mapper.MqMessageMapper;
import com.lartimes.media.service.MqMessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author itcast
 */
@Slf4j
@Service
public class MqMessageServiceImpl extends ServiceImpl<MqMessageMapper, MqMessageDTO> implements MqMessageService {

}
