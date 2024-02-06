package com.lartimes.content.controller;

import com.lartimes.content.model.PageParams;
import com.lartimes.content.model.PageResult;
import com.lartimes.content.model.dto.QueryCourseParamsDto;
import com.lartimes.content.model.po.CourseBase;
import com.lartimes.content.service.CourseBaseInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Lartimes
 * @version 1.0
 * @description:
 * @since 2024/2/5 21:46
 */
@Tag(name = "CourseBaseInfoController", description = "课程基本内容controller")
@RestController
public class CourseBaseInfoController {


    private final CourseBaseInfoService courseBaseInfoService;

    public CourseBaseInfoController(CourseBaseInfoService courseBaseInfoService) {
        this.courseBaseInfoService = courseBaseInfoService;
    }


    @Operation(summary = "查询接口", description = "对内容进行分页查询")
    @PostMapping("/course/list")
    @Parameters({
            @Parameter(name = "QueryCourseParamsDto")
    })
    public PageResult<CourseBase> toDo(PageParams params,
                                       @RequestBody(required = false) QueryCourseParamsDto courseParamsDto) {

        return courseBaseInfoService.getContentsByPage(params , courseParamsDto);
    }
}
