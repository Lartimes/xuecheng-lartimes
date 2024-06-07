package com.lartimes.content.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Lartimes
 * @version 1.0
 * @description:
 * @since 2024/6/7 9:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeachPlanMediaDto {
        private String fileName;
        private String mediaId;
        private Long teachplanId;
}
