package com.lartimes.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lartimes.content.mapper.CourseBaseMapper;
import com.lartimes.content.model.PageParams;
import com.lartimes.content.model.PageResult;
import com.lartimes.content.model.dto.QueryCourseParamsDto;
import com.lartimes.content.model.po.CourseBase;
import com.lartimes.content.service.CourseBaseInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Lartimes
 * @version 1.0
 * @description:
 * @since 2024/2/6 19:17
 */
@Slf4j
@Service
public class CourseBaseInfoServiceImpl implements CourseBaseInfoService {

    private final CourseBaseMapper courseBaseMapper;

    public CourseBaseInfoServiceImpl(CourseBaseMapper courseBaseMapper) {
        this.courseBaseMapper = courseBaseMapper;
    }


    /***
     * 1.写出参数
     * 2.分页查询
     * 3.返回数据
     * @param pages 分页参数
     * @param courseParamsDto 查询条件
     * @return
     */
    @Transactional
    @Override
    public PageResult<CourseBase> getContentsByPage(PageParams pages, QueryCourseParamsDto courseParamsDto) {
        LambdaQueryWrapper<CourseBase> queryWrapper = new LambdaQueryWrapper<>();
        QueryCourseParamsDto paramsDto = new QueryCourseParamsDto(
                courseParamsDto.getAuditStatus(), courseParamsDto.getCourseName(), courseParamsDto.getPublishStatus());
        queryWrapper.like(StringUtils.isNotEmpty(paramsDto.getCourseName()) ,
                        CourseBase::getName , paramsDto.getCourseName());

        queryWrapper.eq(StringUtils.isNotEmpty(paramsDto.getAuditStatus()) ,
                        CourseBase::getAuditStatus , paramsDto.getAuditStatus());

        queryWrapper.eq(StringUtils.isNotEmpty(paramsDto.getPublishStatus()) ,
                        CourseBase::getStatus , paramsDto.getPublishStatus());

        Page<CourseBase> courseBasePage = new Page<>(pages.getPageNo(), pages.getPageSize());
        Page<CourseBase> pageResult = courseBaseMapper.selectPage(courseBasePage, queryWrapper);
        long counts = pageResult.getTotal();
        List<CourseBase> records = pageResult.getRecords();
        return new PageResult<CourseBase>(
                records , counts , pages.getPageNo() , pages.getPageSize()
        );
    }
}
