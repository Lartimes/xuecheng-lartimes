package com.lartimes.content.service.impl;

import com.lartimes.content.mapper.TeachplanMapper;
import com.lartimes.content.model.dto.TeachPlanTreeDto;
import com.lartimes.content.service.TeachPlanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Lartimes
 * @version 1.0
 * @description:
 * @since 2024/2/7 22:44
 */
@Service
@Slf4j
public class TeachPlanServiceImpl implements TeachPlanService {

    private final TeachplanMapper teachplanMapper;

    public TeachPlanServiceImpl(TeachplanMapper teachplanMapper) {
        this.teachplanMapper = teachplanMapper;
    }


    @Override
    public List<TeachPlanTreeDto> getPlanTree(Long id) {
        return  null;
    }
}
