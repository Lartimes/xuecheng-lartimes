package com.lartimes.content.model.dto;

import com.lartimes.content.exception.ValidationGroups;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Lartimes
 * @version 1.0
 * @description: 添加课程Dto封装类
 * @since 2024/2/7 18:28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddCourseInfoDto {
    @NotEmpty(message = "课程名称不能为空")
    @Schema(name = "课程名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @NotEmpty(message = "适用人群不能为空" , groups = ValidationGroups.Insert.class)
    @NotEmpty(message = "适用人群不能为空" , groups = ValidationGroups.Update.class)
    @Size(message = "适用人群内容过少",min = 10)
    @Schema(name = "适用人群", requiredMode = Schema.RequiredMode.REQUIRED)
    private String users;

    @NotEmpty(groups = ValidationGroups.Update.class)
    @Schema(name = "课程标签", requiredMode = Schema.RequiredMode.REQUIRED)
    private String tags;


    @NotEmpty(message = "课程分类不能为空")
    @Schema(name = "大分类", requiredMode = Schema.RequiredMode.REQUIRED)
    private String mt;

    @NotEmpty(message = "课程分类不能为空")
    @Schema(name = "小分类", requiredMode = Schema.RequiredMode.REQUIRED)
    private String st;

    @NotEmpty(message = "课程等级不能为空")
    @Schema(name = "课程等级", requiredMode = Schema.RequiredMode.REQUIRED)
    private String grade;

    @Schema(name = "课程等级", requiredMode = Schema.RequiredMode.REQUIRED)
    private String teachmode;

    @Schema(name = "课程介绍")
    private String description;

    @Schema(name = "课程图片")
    private String pic;

    @NotEmpty(message = "收费规则不能为空")
    @Schema(name = "收费规则，对应数据字典", requiredMode = Schema.RequiredMode.REQUIRED)
    private String charge;

    @Schema(name = "价格")
    private Float price;
    @Schema(name= "原价")
    private Float originalPrice;


    @Schema(name= "qq")
    private String qq;

    @Schema(name= "微信")
    private String wechat;
    @Schema(name= "电话")
    private String phone;

    @Schema(name= "有效期")
    private Integer validDays;
}
