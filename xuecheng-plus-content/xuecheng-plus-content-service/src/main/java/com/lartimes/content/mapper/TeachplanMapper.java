package com.lartimes.content.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lartimes.content.model.dto.TeachPlanTreeDto;
import com.lartimes.content.model.po.Teachplan;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 课程计划 Mapper 接口
 * </p>
 *
 * @author itcast
 */
@Mapper
public interface TeachplanMapper extends BaseMapper<Teachplan> {

    List<TeachPlanTreeDto> selectAllCourses(Long id);

    Integer getMaxOrder(Teachplan teachplan);



}
