package com.lartimes.media.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lartimes.media.model.po.MediaProcess;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author itcast
 */
@Mapper
public interface MediaProcessMapper extends BaseMapper<MediaProcess> {

    @Select({"""
                    select * from media_process 
                        where `status` = '1' and  `fail_count` < 3
                         and `id` % #{shardTotal} = #{startIndex}  
                         limit #{count}
            """})
    List<MediaProcess> getToResolvedMediaFiles(@Param("startIndex") Integer startIndex,
                                               @Param("shardTotal")Integer shardTotal ,
                                               @Param("count") int count);
    @Update("""
                update media_process m set m.status = '4' 
                 where (m.status = '1' or m.status = '3') and
                m.fail_count <3  
                  and m.id = #{id}
            """)
    int startTask(Long id);


}
