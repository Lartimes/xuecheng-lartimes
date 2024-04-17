package com.lartimes.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lartimes.content.mapper.CourseTeacherMapper;
import com.lartimes.content.mapper.TeachplanMapper;
import com.lartimes.content.model.dto.SaveTeachPlanDto;
import com.lartimes.content.model.dto.TeachPlanTreeDto;
import com.lartimes.content.model.po.CourseTeacher;
import com.lartimes.content.model.po.Teachplan;
import com.lartimes.content.service.TeachPlanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Lartimes
 * @version 1.0
 * @description:
 * @since 2024/2/7 22:44
 */
@Service
@Slf4j
public class TeachPlanServiceImpl implements TeachPlanService {

    private final TeachplanMapper teachplanMapper;
    private final CourseTeacherMapper teacherMapper;


    public TeachPlanServiceImpl(TeachplanMapper teachplanMapper, CourseTeacherMapper teacherMapper) {
        this.teachplanMapper = teachplanMapper;
        this.teacherMapper = teacherMapper;
    }

    /**
     * 循环递归，实现递归树
     *
     * @return
     */
    @Override
    public List<TeachPlanTreeDto> getPlanTree(Long id) {
        List<TeachPlanTreeDto> teachPlanTreeDtos = teachplanMapper.selectAllCourses(id);
        if (teachPlanTreeDtos != null && !teachPlanTreeDtos.isEmpty()) {
            return teachPlanTreeDtos;
        }
        LambdaQueryWrapper<Teachplan> query = new LambdaQueryWrapper<>();
        query.eq(id != null, Teachplan::getCourseId, id);
        List<Teachplan> teachplans = teachplanMapper.selectList(query);
        return teachplans.stream().map(TeachPlanTreeDto::cast).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void  addTeachPlan(SaveTeachPlanDto teachplan) {
        Long id = teachplan.getId();
        Teachplan plan = new Teachplan();
        plan.setStatus(1);
        BeanUtils.copyProperties(teachplan, plan);
        if (id != null) {
            teachplanMapper.updateById(plan);
            return;
        }
        Integer orderBy = teachplanMapper.getMaxOrder(plan);
        if (orderBy == null) {
            orderBy = 0;
        }
        plan.setOrderby(orderBy + 1);
        teachplanMapper.insert(plan);
    }

    @Override
    public List<CourseTeacher> getTeachers(Long courseId) {
        LambdaQueryWrapper<CourseTeacher> query = new LambdaQueryWrapper<>();
        query.eq(courseId != null, CourseTeacher::getCourseId, courseId);
        return teacherMapper.selectList(query);
    }

    @Transactional
    @Override
    public CourseTeacher insertTeacher(CourseTeacher teacher) {
        Long id = teacher.getId();
        if (id != null) {
            teacherMapper.updateById(teacher);
            return teacher;
        }
        teacher.setCreateDate(LocalDateTime.now());
        teacherMapper.insert(teacher);
        return teacher;
    }

    @Transactional
    @Override
    /**
     * 获取该课程，parentId ，
     * 根据course_id 获取相同级别List,
     * 根据{moveup} 选出相邻的两个
     * 更新
     */ public void moveUpAndDown(Long id, String move) {
        Teachplan nowPlan = teachplanMapper.selectById(id);
        LambdaQueryWrapper<Teachplan> query = new LambdaQueryWrapper<>();
        Long courseId = nowPlan.getCourseId();
        Integer grade = nowPlan.getGrade();
        Long parentId = nowPlan.getParentid();
        query.eq(courseId != null, Teachplan::getCourseId, courseId);
        query.eq(grade != null, Teachplan::getGrade, grade);
        query.eq(parentId != null, Teachplan::getParentid, parentId);
        List<Teachplan> samePlans = teachplanMapper.selectList(query);
        samePlans = samePlans.stream().sorted(Comparator.comparingInt(Teachplan::getOrderby)).toList();
        int index = 0;
        for (int i = 0; i < samePlans.size(); i++) {
            if (Objects.equals(samePlans.get(i).getId(), id)) {
                index = i;
                break;
            }
        }
        Teachplan other = null;
        if ("moveup".equals(move)) {
            if (index == 0) {
                return;
            }
            other = samePlans.get(index - 1);
            int tmp = other.getOrderby();
            other.setOrderby(nowPlan.getOrderby());
            nowPlan.setOrderby(tmp);
        } else {
            if (index == samePlans.size() - 1) {
                return;
            }
            other = samePlans.get(index + 1);
            int tmp = other.getOrderby();
            other.setOrderby(nowPlan.getOrderby());
            nowPlan.setOrderby(tmp);
        }
        teachplanMapper.updateById(other);
        teachplanMapper.updateById(nowPlan);
    }

    @Override
    public void deletePlans(Long id) {
        Objects.requireNonNull(id);
        teachplanMapper.deleteById(id);
        LambdaQueryWrapper<Teachplan> query = new LambdaQueryWrapper<>();
        query.eq(true , Teachplan::getParentid , id);
        teachplanMapper.delete(query);
    }

    @Override
    public void deleteTeacher(Long courseId, Long teacherId) {

        LambdaQueryWrapper<CourseTeacher> query = new LambdaQueryWrapper<>();
        query.eq(teacherId != null, CourseTeacher::getId , teacherId)
                .eq(courseId != null , CourseTeacher::getCourseId , courseId);
        teacherMapper.delete(query);
    }

    @Override
    public void deletePlansByCourseId(Long id) {
        LambdaQueryWrapper<Teachplan> query = new LambdaQueryWrapper<>();
        query.eq(id != null , Teachplan::getCourseId , id);
        teachplanMapper.delete(query);
    }
}
