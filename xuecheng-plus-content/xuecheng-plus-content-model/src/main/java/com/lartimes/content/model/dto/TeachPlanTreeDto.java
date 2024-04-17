package com.lartimes.content.model.dto;

import com.lartimes.content.model.po.Teachplan;
import com.lartimes.content.model.po.TeachplanMedia;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * @author Lartimes
 * @version 1.0
 * @description:
 * @since 2024/2/8 13:14
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeachPlanTreeDto extends Teachplan {

    private TeachplanMedia teachplanMedia;
    private List<TeachPlanTreeDto> teachPlanTreeNodes;

    public static TeachPlanTreeDto cast(Teachplan teachplan) {
        TeachPlanTreeDto teachPlanTreeDto = new TeachPlanTreeDto();
        BeanUtils.copyProperties(teachplan , teachPlanTreeDto);
        teachplan = null;
        return teachPlanTreeDto;
    }
}
