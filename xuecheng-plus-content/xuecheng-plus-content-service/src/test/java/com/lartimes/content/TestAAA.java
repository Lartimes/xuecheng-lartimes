package com.lartimes.content;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lartimes.content.mapper.CourseBaseMapper;
import com.lartimes.content.model.PageResult;
import com.lartimes.content.model.dto.QueryCourseParamsDto;
import com.lartimes.content.model.po.CourseBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;

import java.util.List;

;

/**
 * @author Lartimes
 * @version 1.0
 * @description:
 * @since 2024/2/6 15:56
 */
@SpringBootTest
public class TestAAA {
    @Autowired
    private CourseBaseMapper courseBaseMapper;


    @Test
    public void testMappers(){
        CourseBase courseBase = courseBaseMapper.selectById(1L);
        System.out.println(courseBase);

//        分页查询
        LambdaQueryWrapper<CourseBase> queryWrapper = new LambdaQueryWrapper<>();
        QueryCourseParamsDto paramsDto = new QueryCourseParamsDto("", "java", "");
        queryWrapper.like(StringUtils.isNotEmpty(paramsDto.getCourseName()) ,
                        CourseBase::getName , paramsDto.getCourseName())
                .eq(StringUtils.isNotEmpty(paramsDto.getAuditStatus()) ,
                            CourseBase::getAuditStatus , paramsDto.getAuditStatus())
                .eq(StringUtils.isNotEmpty(paramsDto.getPublishStatus()) ,
                        CourseBase::getStatus , paramsDto.getPublishStatus());
        Page<CourseBase> courseBasePage = new Page<>(1, 2);
        Page<CourseBase> pageResult = courseBaseMapper.selectPage(courseBasePage, queryWrapper);

        long counts = pageResult.getTotal();
        List<CourseBase> records = pageResult.getRecords();
        PageResult<CourseBase> courseBasePageResult = new PageResult<>();
        courseBasePageResult.setPage(1);
        courseBasePageResult.setPageSize(2);
        courseBasePageResult.setCounts(counts);
        courseBasePageResult.setItems(records);

        System.out.println(courseBasePageResult);
    }
}
