package com.lartimes.content.controller;

import com.lartimes.content.model.dto.SaveTeachPlanDto;
import com.lartimes.content.model.dto.TeachPlanTreeDto;
import com.lartimes.content.model.po.CourseTeacher;
import com.lartimes.content.service.TeachPlanService;
import io.swagger.v3.oas.annotations.Operation;
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
    public List<TeachPlanTreeDto> teachPlanTree(@PathVariable Long id){
        return  teachPlanService.getPlanTree(id);
    }

    @Operation(summary = "添加章/小节")
    @PostMapping("/teachplan")
    public void addMedia(@RequestBody  SaveTeachPlanDto teachplan){
        teachPlanService.addTeachPlan(teachplan);
    }

    @Operation(summary = "删除本章/小节")
    @DeleteMapping("/teachplan/{id}")
    public void deletePlan(@PathVariable Long id ){
        teachPlanService.deletePlans(id);
    }

    @Operation(summary = "删除教师")
    @DeleteMapping("/courseTeacher/course/{courseId}/{teacherId}")
    public void deleteTeacherByCourse( @PathVariable("courseId") Long courseId ,
                                       @PathVariable("teacherId") Long teacherId){
        teachPlanService.deleteTeacher(courseId , teacherId);
    }





    @Operation(summary = "所选课程讲课老师查询")
    @GetMapping("/courseTeacher/list/{courseID}")
    public List<CourseTeacher> listTeachers(@PathVariable("courseID") Long courseID){
        return  teachPlanService.getTeachers(courseID);
    }

    @Operation(summary =  "为所选课程添加教师/修改教师信息")
    @PostMapping("/courseTeacher")
    public  CourseTeacher addCourseTeacher(@RequestBody CourseTeacher teacher){
        return  teachPlanService.insertTeacher(teacher);
    }


    @Operation(summary = "teachPlan上下移动")
    @PostMapping("/teachplan/moveup/{id}")
    public void  moveUp(@PathVariable Long id){
        teachPlanService.moveUpAndDown(id , "moveup");
    }
    @Operation(summary = "teachPlan上下移动")
    @PostMapping("/teachplan/movedown/{id}")
    public void  moveDown(@PathVariable Long id){
        teachPlanService.moveUpAndDown(id , "");
    }
}
