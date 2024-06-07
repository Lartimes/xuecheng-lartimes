package com.lartimes.content.controller;

import com.lartimes.content.model.dto.SaveTeachPlanDto;
import com.lartimes.content.model.dto.TeachPlanMediaDto;
import com.lartimes.content.model.dto.TeachPlanTreeDto;
import com.lartimes.content.model.po.CourseTeacher;
import com.lartimes.content.service.TeachPlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Lartimes
 * @version 1.0
 * @description:
 * @since 2024/2/7 22:41
 */
@RestController
@Slf4j
@Tag(name = "TeachPlanController", description = "教学计划controller")
public class TeachPlanController {

    @Autowired
    private TeachPlanService teachPlanService;


    @Operation(summary = "获取课程计划树")
    @GetMapping("/teachplan/{id}/tree-nodes")
    @Parameters({@Parameter(name = "id", description = "顶级TeachPlanID")})
    public List<TeachPlanTreeDto> teachPlanTree(@PathVariable Long id) {
        return teachPlanService.getPlanTree(id);
    }

    @Operation(summary = "添加章/小节")
    @PostMapping("/teachplan")
    @Parameters({@Parameter(name = "teachplan", description = "新增TeachPlan")})
    public void addMedia(@RequestBody SaveTeachPlanDto teachplan) {
        teachPlanService.addTeachPlan(teachplan);
    }

    @Operation(summary = "删除本章/小节")
    @DeleteMapping("/teachplan/{id}")
    @Parameters({@Parameter(name = "id", description = "教学计划ID")})
    public void deletePlan(@PathVariable Long id) {
        teachPlanService.deletePlans(id);
    }

    @Operation(summary = "删除教师")
    @DeleteMapping("/courseTeacher/course/{courseId}/{teacherId}")
    @Parameters({@Parameter(name = "courseId", description = "课程ID"), @Parameter(name = "teacherId", description = "教师ID")})
    public void deleteTeacherByCourse(@PathVariable("courseId") Long courseId, @PathVariable("teacherId") Long teacherId) {
        teachPlanService.deleteTeacher(courseId, teacherId);
    }

    @Operation(summary = "视频课程content-media 绑定")
    @PostMapping("/teachplan/association/media")
    @Parameters({@Parameter(name = "teachPlanMediaDto", description = "mediaId-teachplanID_dto")})
    public void mediaPlanAssociation(@RequestBody TeachPlanMediaDto teachPlanMediaDto) {
        teachPlanService.bindMediaPlan(teachPlanMediaDto);
    }


    @DeleteMapping("/teachplan/association/media/{teachplanId}/{mediaId}")
    @Parameters({@Parameter(name = "teachPlanMediaDto", description = "mediaId-teachplanID_dto")})
    public void unbindTeachMedia(@PathVariable("teachplanId") Integer planId, @PathVariable("mediaId") String mediaId) {

        teachPlanService.unbindMediaPlan(planId, mediaId);
    }


    @Operation(summary = "所选课程讲课老师查询")
    @GetMapping("/courseTeacher/list/{courseID}")
    @Parameters({@Parameter(name = "courseID", description = "课程ID")})
    public List<CourseTeacher> listTeachers(@PathVariable("courseID") Long courseID) {
        return teachPlanService.getTeachers(courseID);
    }

    @Operation(summary = "为所选课程添加教师/修改教师信息")
    @PostMapping("/courseTeacher")
    @Parameters({@Parameter(name = "CourseTeacher", description = "加入的老师")})
    public CourseTeacher addCourseTeacher(@RequestBody CourseTeacher teacher) {
        return teachPlanService.insertTeacher(teacher);
    }


    @Operation(summary = "teachPlan上下移动")
    @PostMapping("/teachplan/moveup/{id}")
    @Parameters({@Parameter(name = "id", description = "teachPlan ID")})
    public void moveUp(@PathVariable Long id) {
        teachPlanService.moveUpAndDown(id, "moveup");
    }

    @Operation(summary = "teachPlan上下移动")
    @PostMapping("/teachplan/movedown/{id}")
    @Parameters({@Parameter(name = "id", description = "teachPlan ID")})
    public void moveDown(@PathVariable Long id) {
        teachPlanService.moveUpAndDown(id, "");
    }
}
