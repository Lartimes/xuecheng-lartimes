package com.lartimes.media.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author itcast
 */
@Data
public class MediaProcessHistoryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @Schema(name = "文件标识")
    private String fileId;

    @Schema(name = "文件名称")
    private String filename;

    @Schema(name = "存储源")
    private String bucket;

    @Schema(name = "状态,1:未处理，2：处理成功  3处理失败")
    private String status;

    @Schema(name = "上传时间")
    private LocalDateTime createDate;

    @Schema(name = "完成时间")
    private LocalDateTime finishDate;

    @Schema(name = "媒资文件访问地址")
    private String url;

    @Schema(name = "文件路径")
    private String filePath;

    @Schema(name = "失败原因")
    private String errormsg;


}
