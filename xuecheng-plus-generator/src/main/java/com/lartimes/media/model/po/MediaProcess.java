package com.lartimes.media.model.po;

import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * <p>
 * 
 * </p>
 *
 * @author itcast
 */
@Data
@TableName("media_process")
public class MediaProcess implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 文件标识
     */
    private String fileId;

    /**
     * 文件名称
     */
    private String filename;

    /**
     * 存储桶
     */
    private String bucket;

    /**
     * 存储路径
     */
    private String filePath;

    /**
     * 状态,1:未处理，2：处理成功  3处理失败
     */
    private String status;

    /**
     * 上传时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createDate;

    /**
     * 完成时间
     */
    private LocalDateTime finishDate;

    /**
     * 媒资文件访问地址
     */
    private String url;

    /**
     * 失败原因
     */
    private String errormsg;


}
