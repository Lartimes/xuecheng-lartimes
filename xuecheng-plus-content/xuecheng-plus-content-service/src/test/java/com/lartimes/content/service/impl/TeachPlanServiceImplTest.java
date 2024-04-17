package com.lartimes.content.service.impl;

import com.lartimes.content.service.TeachPlanService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Lartimes
 * @version 1.0
 * @description:
 * @since 2024/2/7 22:47
 */
@SpringBootTest
class TeachPlanServiceImplTest {

    @Autowired
    TeachPlanService teachPlanService;
    @Test
    void getTeachPlansTree() {
        System.out.println(teachPlanService.getPlanTree(1L));
    }


}