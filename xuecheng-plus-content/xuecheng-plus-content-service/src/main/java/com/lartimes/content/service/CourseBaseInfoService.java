package com.lartimes.content.service;

import com.lartimes.content.model.PageParams;
import com.lartimes.content.model.PageResult;
import com.lartimes.content.model.dto.AddCourseInfoDto;
import com.lartimes.content.model.dto.CourseBaseInfoDto;
import com.lartimes.content.model.dto.EditCourseDto;
import com.lartimes.content.model.dto.QueryCourseParamsDto;
import com.lartimes.content.model.po.CourseBase;

/**
 * @author Lartimes
 * @version 1.0
 * @description:
 * @since 2024/2/6 14:39
 */
public interface CourseBaseInfoService {
    /**
     * @param pages 分页参数
     * @param courseParamsDto 查询条件
     * @return
     */
    PageResult<CourseBase> getContentsByPage(PageParams pages , QueryCourseParamsDto courseParamsDto);

    CourseBaseInfoDto addCourse(AddCourseInfoDto addCourseInfoDto);

    CourseBaseInfoDto selectCourseById(String id);

    CourseBaseInfoDto updateCourse(Long companyId , EditCourseDto editCourseDto);

}
