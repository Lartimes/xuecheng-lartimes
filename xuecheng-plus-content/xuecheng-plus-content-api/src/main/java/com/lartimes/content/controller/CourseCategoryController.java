package com.lartimes.content.controller;

import com.lartimes.content.model.dto.CourseCategoryTreeDto;
import com.lartimes.content.service.CourseCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Lartimes
 * @version 1.0
 * @description:
 * @since 2024/2/7 11:35
 */
@RestController
@Slf4j
@Tag(name = "CourseCategoryController", description = "课程分类controller")
public class CourseCategoryController {
    // /course-category/tree-nodes

    private final CourseCategoryService courseCategoryService;

    public CourseCategoryController(CourseCategoryService courseCategoryService) {
        this.courseCategoryService = courseCategoryService;
    }

    @Operation(summary = "获取树形结构", description = "获取课程分类树形结构")
    @Parameters({
            @Parameter(name = "topId" ,description = "该表最顶级ID")
    })
    @GetMapping("/course-category/tree-nodes")
    public List<CourseCategoryTreeDto> getCourseTree(String topId){
        if (!StringUtils.hasText(topId)){
            topId = "1";
        }
        return courseCategoryService.getCourseTree(topId);
    }
}
