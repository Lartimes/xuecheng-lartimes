package com.lartimes.content.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Lartimes
 * @version 1.0
 * @description: 课程查询参数
 * @since 2024/2/5 21:42
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryCourseParamsDto {
    //审核状态
    private String auditStatus;
    //课程名称
    private String courseName;
    //发布状态
    private String publishStatus;

}
