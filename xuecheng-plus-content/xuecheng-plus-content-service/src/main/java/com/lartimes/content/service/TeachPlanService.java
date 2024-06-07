package com.lartimes.content.service;

import com.lartimes.content.model.dto.SaveTeachPlanDto;
import com.lartimes.content.model.dto.TeachPlanMediaDto;
import com.lartimes.content.model.dto.TeachPlanTreeDto;
import com.lartimes.content.model.po.CourseTeacher;
import com.lartimes.content.model.po.TeachplanMedia;

import java.util.List;

/**
 * @author Lartimes
 * @version 1.0
 * @description:
 * @since 2024/2/7 22:44
 */
public interface TeachPlanService {
    /**
     * 根据课程ID获取教学计划树
     *
     * @param id
     * @return
     */
    List<TeachPlanTreeDto> getPlanTree(Long id);


    /**
     * 添加课程计划
     *
     * @param teachplan
     */
    void addTeachPlan(SaveTeachPlanDto teachplan);

    /**
     * 根据课程ID获取老师
     *
     * @param courseID
     * @return
     */
    List<CourseTeacher> getTeachers(Long courseID);


    /**
     * 对某个课程添加Teacher
     *
     * @param teacher
     * @return
     */
    CourseTeacher insertTeacher(CourseTeacher teacher);

    /**
     * 课程计划上下移动
     * @param id
     * @param moveup
     */
    void moveUpAndDown(Long id, String moveup);


    /**
     * 根据id删除课程计划
     * @param id
     */
    void deletePlans(Long id);

    /**
     * 删除教师
     * @param courseId
     * @param teacherId
     */
    void deleteTeacher(Long courseId, Long teacherId);

    /**
     * 根据课程ID删除所有课程计划
     * @param id
     */
    void deletePlansByCourseId(Long id);

    /**
     * 为课程计划绑定媒资
     * @param teachPlanMediaDto
     * @return
     */
    TeachplanMedia bindMediaPlan(TeachPlanMediaDto teachPlanMediaDto);


    /**
     * 取消绑定
     * @param planId
     * @param mediaId
     */
    void unbindMediaPlan(Integer planId, String mediaId);


}
