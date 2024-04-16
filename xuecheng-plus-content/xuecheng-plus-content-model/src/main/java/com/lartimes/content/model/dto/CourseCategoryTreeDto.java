package com.lartimes.content.model.dto;

import com.lartimes.content.model.po.CourseCategory;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author Lartimes
 * @version 1.0
 * @description:
 * @since 2024/2/7 11:55
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class CourseCategoryTreeDto extends CourseCategory implements Serializable {
    List<CourseCategory>  childrenTreeNodes;

    @Override
    public String toString() {
        return "CourseCategoryTreeDto{" +
                "childrenTreeNodes=" + childrenTreeNodes +
                "} " + super.toString();
    }
}
