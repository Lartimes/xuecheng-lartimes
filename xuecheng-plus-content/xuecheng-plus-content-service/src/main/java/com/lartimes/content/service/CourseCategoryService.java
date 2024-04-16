package com.lartimes.content.service;

import com.lartimes.content.model.dto.CourseCategoryTreeDto;

import java.util.List;

/**
 * @author Lartimes
 * @version 1.0
 * @description:
 * @since 2024/2/7 17:39
 */
public interface CourseCategoryService {
    /**
     * @param topId 数据库中顶级元素id
     * @return
     */
    List<CourseCategoryTreeDto>  getCourseTree(String topId);
}
