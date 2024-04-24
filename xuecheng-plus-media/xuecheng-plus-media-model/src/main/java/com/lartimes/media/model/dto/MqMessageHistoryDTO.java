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
public class MqMessageHistoryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "消息id")
    private String id;

    @Schema(name = "消息类型代码")
    private String messageType;

    @Schema(name = "关联业务信息")
    private String businessKey1;

    @Schema(name = "关联业务信息")
    private String businessKey2;

    @Schema(name = "关联业务信息")
    private String businessKey3;

    @Schema(name = "消息队列主机")
    private String mqHost;

    @Schema(name = "消息队列端口")
    private Integer mqPort;

    @Schema(name = "消息队列虚拟主机")
    private String mqVirtualhost;

    @Schema(name = "队列名称")
    private String mqQueue;

    @Schema(name = "通知次数")
    private Integer informNum;

    @Schema(name = "处理状态，0:初始，1:成功，2:失败")
    private Integer state;

    @Schema(name = "回复失败时间")
    private LocalDateTime returnfailureDate;

    @Schema(name = "回复成功时间")
    private LocalDateTime returnsuccessDate;

    @Schema(name = "回复失败内容")
    private String returnfailureMsg;

    @Schema(name = "最近通知时间")
    private LocalDateTime informDate;

    private String stageState1;

    private String stageState2;

    private String stageState3;

    private String stageState4;


}
