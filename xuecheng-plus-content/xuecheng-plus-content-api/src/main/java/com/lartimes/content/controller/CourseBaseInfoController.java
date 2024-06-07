package com.lartimes.content.controller;

import com.lartimes.content.model.PageParams;
import com.lartimes.content.model.PageResult;
import com.lartimes.content.model.dto.AddCourseInfoDto;
import com.lartimes.content.model.dto.CourseBaseInfoDto;
import com.lartimes.content.model.dto.EditCourseDto;
import com.lartimes.content.model.dto.QueryCourseParamsDto;
import com.lartimes.content.model.po.CourseBase;
import com.lartimes.content.service.CourseBaseInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @Operation(summary = "查询课程")
    @GetMapping("/course/{id}")
    @Parameters(
            @Parameter(name = "id" ,description = "课程ID")
    )
    public CourseBaseInfoDto getCourse(@PathVariable("id") String id) {
        return courseBaseInfoService.selectCourseById(id);
    }

    @Operation(summary = "修改课程")
    @PutMapping("/course")
    @Parameters(
            @Parameter(name = "EditCourseDto" ,description = "修改课程DTO")
    )
    public CourseBaseInfoDto editCourse(@Validated @RequestBody
                                            EditCourseDto editCourseDto) {
        Long companyId = 1232141425L;
        return courseBaseInfoService.updateCourse(companyId, editCourseDto);
    }

    @Operation(summary = "新增课程")
    @PostMapping("/course")
    @Parameters(
            @Parameter(name = "AddCourseInfoDto" ,description = "新增课程Info")
    )
    public CourseBaseInfoDto addCourse(@Validated @RequestBody
                                           AddCourseInfoDto courseInfoDto) {
        return courseBaseInfoService.addCourse(courseInfoDto);
    }

    @Operation(summary = "删除课程")
    @DeleteMapping("/course/{id}")
    @Parameters(
            @Parameter(name = "id" ,description = "课程ID")
    )
    public void deleteCourse(@PathVariable("id") Long id ){
        courseBaseInfoService.deleteCourse(id);
    }

    @Operation(summary = "查询接口", description = "对内容进行分页查询")
    @PostMapping("/course/list")
    @Parameters({@Parameter(name = "params" , description = "分页参数"),
            @Parameter(name = "QueryCourseParamsDto", description = "查询课程其他筛选参数")})
    public PageResult<CourseBase> toDo(PageParams params,
                                       @RequestBody(required = false)
                                       QueryCourseParamsDto courseParamsDto) {
        return courseBaseInfoService.getContentsByPage(params, courseParamsDto);
    }
}
