package com.lartimes.content.controller;

import com.lartimes.content.service.TeachPlanService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/teachplan/{id}/tree-nodes")
    public void teachPlanTree(@PathVariable Long id){
        System.out.println(id);
        teachPlanService.getPlanTree(id);

    }
}
