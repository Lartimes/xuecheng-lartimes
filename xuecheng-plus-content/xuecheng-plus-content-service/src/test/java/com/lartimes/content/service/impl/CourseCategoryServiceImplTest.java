package com.lartimes.content.service.impl;

import com.lartimes.content.model.dto.CourseCategoryTreeDto;
import com.lartimes.content.service.CourseCategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Lartimes
 * @version 1.0
 * @description:
 * @since 2024/2/7 18:04
 */
@SpringBootTest
class CourseCategoryServiceImplTest {

    @Autowired
    CourseCategoryService courseCategoryService;
    @Test
    public void testGetTree() {
    List<CourseCategoryTreeDto> ret = courseCategoryService.getCourseTree("1");
    ret.forEach(System.out::println);
    }
}