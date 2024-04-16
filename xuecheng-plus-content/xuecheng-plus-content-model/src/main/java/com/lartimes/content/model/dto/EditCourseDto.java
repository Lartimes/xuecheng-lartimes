package com.lartimes.content.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Lartimes
 * @version 1.0
 * @description:
 * @since 2024/2/8 12:32
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class EditCourseDto extends AddCourseInfoDto{
    private  Long id;
}
