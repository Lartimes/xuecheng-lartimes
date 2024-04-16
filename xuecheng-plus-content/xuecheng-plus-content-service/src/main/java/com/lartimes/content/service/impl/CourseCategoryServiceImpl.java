package com.lartimes.content.service.impl;

import com.lartimes.content.mapper.CourseCategoryMapper;
import com.lartimes.content.model.dto.CourseCategoryTreeDto;
import com.lartimes.content.model.po.CourseCategory;
import com.lartimes.content.service.CourseCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Lartimes
 * @version 1.0
 * @description:
 * @since 2024/2/7 17:52
 */
@Slf4j
@Service
public class CourseCategoryServiceImpl implements CourseCategoryService {

    @Autowired
    private CourseCategoryMapper  courseCategoryMapper;
    @Override
    public List<CourseCategoryTreeDto> getCourseTree(String topId) {
        List<CourseCategoryTreeDto> treeDtos = courseCategoryMapper.selectTreeNodes(topId);
//
        List<CourseCategoryTreeDto> fathers = treeDtos.stream()
                .filter(node -> topId.equals(node.getParentid()))
                .toList();
        fathers.forEach(father->{
            List<CourseCategory> sonNodes = treeDtos.stream()
                    .filter(node -> father.getId().equals(node.getParentid()))
                    .map(son -> (CourseCategory) son)
                    .collect(Collectors.toList());
            father.setChildrenTreeNodes(sonNodes);
        });
        return fathers;
    }

}
