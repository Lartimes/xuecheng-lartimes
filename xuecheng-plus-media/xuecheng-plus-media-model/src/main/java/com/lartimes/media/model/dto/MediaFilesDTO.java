package com.lartimes.media.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 媒资信息
 * </p>
 *
 * @author itcast
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MediaFilesDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "文件id,md5值")
    private String id;

    @Schema(name = "机构ID")
    private Long companyId;

    @Schema(name = "机构名称")
    private String companyName;

    @Schema(name= "文件名称")
    private String filename;

    @Schema(name = "文件类型（图片、文档，视频）")
    private String fileType;

    @Schema(name = "标签")
    private String tags;

    @Schema(name = "存储目录")
    private String bucket;

    @Schema(name = "存储路径")
    private String filePath;

    @Schema(name = "文件id")
    private String fileId;

    @Schema(name = "媒资文件访问地址")
    private String url;

    @Schema(name = "上传人")
    private String username;

    @Schema(name = "上传时间")
    private LocalDateTime createDate;

    @Schema(name = "修改时间")
    private LocalDateTime changeDate;

    @Schema(name = "状态,1:正常，0:不展示")
    private String status;

    @Schema(name = "备注")
    private String remark;

    @Schema(name = "审核状态")
    private String auditStatus;

    @Schema(name = "审核意见")
    private String auditMind;

    @Schema(name = "文件大小")
    private Long fileSize;


}
