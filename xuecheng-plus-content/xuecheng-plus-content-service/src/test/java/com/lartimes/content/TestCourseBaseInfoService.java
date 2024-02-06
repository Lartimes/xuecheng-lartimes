package com.lartimes.content;

import com.lartimes.content.model.PageParams;
import com.lartimes.content.model.PageResult;
import com.lartimes.content.model.dto.QueryCourseParamsDto;
import com.lartimes.content.model.po.CourseBase;
import com.lartimes.content.service.CourseBaseInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author Lartimes
 * @version 1.0
 * @description:
 * @since 2024/2/6 19:34
 */
@SpringBootTest
public class TestCourseBaseInfoService {
    @Autowired
    CourseBaseInfoService courseBaseInfoService;

    @Test
    public void testGetCourses(){

        PageParams pages = new PageParams(1L,2L);
        QueryCourseParamsDto paramsDto = new QueryCourseParamsDto("" , "java" , "");
        PageResult<CourseBase> contentsByPage = courseBaseInfoService.getContentsByPage(pages , paramsDto);
        List<CourseBase> items = contentsByPage.getItems();
        items.forEach(System.out::println);


    }
}
