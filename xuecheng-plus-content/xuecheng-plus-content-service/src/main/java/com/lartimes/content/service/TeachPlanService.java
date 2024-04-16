package com.lartimes.content.service;

import com.lartimes.content.model.dto.TeachPlanTreeDto;

import java.util.List;

/**
 * @author Lartimes
 * @version 1.0
 * @description:
 * @since 2024/2/7 22:44
 */
public interface TeachPlanService {
    List<TeachPlanTreeDto> getPlanTree(Long id);

}
