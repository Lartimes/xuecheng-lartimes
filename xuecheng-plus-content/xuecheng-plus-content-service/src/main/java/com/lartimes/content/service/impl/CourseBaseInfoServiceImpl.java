package com.lartimes.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lartimes.content.exception.XueChengPlusException;
import com.lartimes.content.mapper.CourseBaseMapper;
import com.lartimes.content.mapper.CourseMarketMapper;
import com.lartimes.content.model.PageParams;
import com.lartimes.content.model.PageResult;
import com.lartimes.content.model.dto.AddCourseInfoDto;
import com.lartimes.content.model.dto.CourseBaseInfoDto;
import com.lartimes.content.model.dto.EditCourseDto;
import com.lartimes.content.model.dto.QueryCourseParamsDto;
import com.lartimes.content.model.po.CourseBase;
import com.lartimes.content.model.po.CourseMarket;
import com.lartimes.content.service.CourseBaseInfoService;
import com.lartimes.content.service.TeachPlanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

    private final CourseMarketMapper courseMarketMapper;

    private final CourseBaseMapper courseBaseMapper;
    private final TeachPlanService teachPlanService;


    public CourseBaseInfoServiceImpl(CourseBaseMapper courseBaseMapper, CourseMarketMapper courseMarketMapper, TeachPlanService teachPlanService) {
        this.courseBaseMapper = courseBaseMapper;
        this.courseMarketMapper = courseMarketMapper;
        this.teachPlanService = teachPlanService;
    }

    /***
     * 1.写出参数
     * 2.分页查询
     * 3.返回数据
     * @param pages 分页参数
     * @param courseParamsDto 查询条件
     * @return
     */
    @Override
    public PageResult<CourseBase> getContentsByPage(PageParams pages, QueryCourseParamsDto courseParamsDto) {
        LambdaQueryWrapper<CourseBase> queryWrapper = new LambdaQueryWrapper<>();
        QueryCourseParamsDto paramsDto = new QueryCourseParamsDto(courseParamsDto.getAuditStatus(), courseParamsDto.getCourseName(), courseParamsDto.getPublishStatus());
        queryWrapper.like(StringUtils.isNotEmpty(paramsDto.getCourseName()), CourseBase::getName, paramsDto.getCourseName());

        queryWrapper.eq(StringUtils.isNotEmpty(paramsDto.getAuditStatus()), CourseBase::getAuditStatus, paramsDto.getAuditStatus());

        queryWrapper.eq(StringUtils.isNotEmpty(paramsDto.getPublishStatus()), CourseBase::getStatus, paramsDto.getPublishStatus());

        Page<CourseBase> courseBasePage = new Page<>(pages.getPageNo(), pages.getPageSize());
        Page<CourseBase> pageResult = courseBaseMapper.selectPage(courseBasePage, queryWrapper);
        long counts = pageResult.getTotal();
        List<CourseBase> records = pageResult.getRecords();
        return new PageResult<CourseBase>(records, counts, pages.getPageNo(), pages.getPageSize());
    }

    /**
     * 1.JSR303合法性检测
     * 2.copy属性，插入数据，获取id
     * 3.更新market
     * 4.返回基础信息
     *
     * @param addCourseInfoDto
     * @return
     */
    @Transactional
    @Override
    public CourseBaseInfoDto addCourse(AddCourseInfoDto addCourseInfoDto) {
        CourseBaseInfoDto baseInfoDtoNew = new CourseBaseInfoDto();
        CourseBase baseInfoNew = new CourseBase();
        BeanUtils.copyProperties(addCourseInfoDto, baseInfoNew);
//        TODO company_id audit_status 后续数据字典redis 进行更改
        baseInfoNew.setCompanyId(1232141425L);
        baseInfoNew.setAuditStatus("202001");
        baseInfoNew.setCreateDate(LocalDateTime.now());
        baseInfoDtoNew.setStatus("203001");
        int count = courseBaseMapper.insert(baseInfoNew);
        if (count < 1) {
            throw new XueChengPlusException("插入基本课程Error");
        }
        BeanUtils.copyProperties(addCourseInfoDto, baseInfoDtoNew);
        CourseMarket courseMarket = new CourseMarket();
        BeanUtils.copyProperties(addCourseInfoDto, courseMarket);
        courseMarket.setId(baseInfoNew.getId());
        baseInfoDtoNew.setId(baseInfoNew.getId());
        count += courseMarketMapper.insert(courseMarket);
        if (count != 2) {
            throw new XueChengPlusException("插入课程销售信息Error");
        }
        return baseInfoDtoNew;
    }

    @Override
    public CourseBaseInfoDto selectCourseById(String id) {
        CourseBaseInfoDto returnDto = new CourseBaseInfoDto();
        LambdaQueryWrapper<CourseBase> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(id), CourseBase::getId, id);
        CourseBase course = courseBaseMapper.selectOne(queryWrapper);
        if (course == null) {
            throw new XueChengPlusException("未查询到该课程");
        }
        LambdaQueryWrapper<CourseMarket> queryMarket = new LambdaQueryWrapper<>();
        queryMarket.eq(StringUtils.isNotBlank(id), CourseMarket::getId, id);
        CourseMarket courseMarket = courseMarketMapper.selectOne(queryMarket);
        if (courseMarket != null) {
            BeanUtils.copyProperties(courseMarket, returnDto);
        }
        BeanUtils.copyProperties(course, returnDto);
        return returnDto;
    }

    @Transactional
    @Override
    public CourseBaseInfoDto updateCourse(Long companyId, EditCourseDto editCourseDto) {
        Long id = editCourseDto.getId();
        CourseBase courseBase = courseBaseMapper.selectById(id);
        if (courseBase == null) {
            throw new XueChengPlusException("不存在该课程");
        }
        if (!courseBase.getCompanyId().equals(companyId)) {
            throw new XueChengPlusException("该课程不属于(company_ID)" + companyId);
        }
        CourseBase editCourse = new CourseBase();
        CourseMarket editMarket = new CourseMarket();
        BeanUtils.copyProperties(editCourseDto, editMarket);
        BeanUtils.copyProperties(editCourseDto, editCourse);
        int count = courseBaseMapper.updateById(editCourse);
        count += courseMarketMapper.updateById(editMarket);
        if (count != 2) {
            throw new XueChengPlusException("更新失败");
        }
        return this.selectCourseById(String.valueOf(editCourseDto.getId()));
    }

    @Transactional
    @Override
    public void deleteCourse(Long id) {
        CourseBase courseBase = courseBaseMapper.selectById(id);
        if ("202001".equals(courseBase.getAuditStatus())) {
            return;
        }
        teachPlanService.deletePlansByCourseId(id);
        teachPlanService.deleteTeacher(id , null);
        courseMarketMapper.deleteById(id);
        courseBaseMapper.deleteById(id);
    }
}
