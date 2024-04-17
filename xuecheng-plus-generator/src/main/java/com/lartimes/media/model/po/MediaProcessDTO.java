package com.lartimes.media.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value="MediaProcessDTO", description="")
public class MediaProcessDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @ApiModelProperty(value = "文件标识")
    private String fileId;

    @ApiModelProperty(value = "文件名称")
    private String filename;

    @ApiModelProperty(value = "存储桶")
    private String bucket;

    @ApiModelProperty(value = "存储路径")
    private String filePath;

    @ApiModelProperty(value = "状态,1:未处理，2：处理成功  3处理失败")
    private String status;

    @ApiModelProperty(value = "上传时间")
    private LocalDateTime createDate;

    @ApiModelProperty(value = "完成时间")
    private LocalDateTime finishDate;

    @ApiModelProperty(value = "媒资文件访问地址")
    private String url;

    @ApiModelProperty(value = "失败原因")
    private String errormsg;


}
