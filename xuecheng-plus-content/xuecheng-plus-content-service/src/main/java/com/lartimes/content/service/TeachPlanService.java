package com.lartimes.content.service;

import com.lartimes.content.model.dto.SaveTeachPlanDto;
import com.lartimes.content.model.dto.TeachPlanTreeDto;
import com.lartimes.content.model.po.CourseTeacher;

import java.util.List;

/**
 * @author Lartimes
 * @version 1.0
 * @description:
 * @since 2024/2/7 22:44
 */
public interface TeachPlanService {
    List<TeachPlanTreeDto> getPlanTree(Long id);

    void addTeachPlan(SaveTeachPlanDto teachplan);

    List<CourseTeacher> getTeachers(Long courseID);

    CourseTeacher insertTeacher(CourseTeacher teacher);

    void moveUpAndDown(Long id, String moveup);


    void deletePlans(Long id);

    void deleteTeacher(Long courseId, Long teacherId);
    void deletePlansByCourseId(Long id);

}
