package com.lartimes.media.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
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
@ApiModel(value="MqMessageHistoryDTO", description="")
public class MqMessageHistoryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "消息id")
    private String id;

    @ApiModelProperty(value = "消息类型代码")
    private String messageType;

    @ApiModelProperty(value = "关联业务信息")
    private String businessKey1;

    @ApiModelProperty(value = "关联业务信息")
    private String businessKey2;

    @ApiModelProperty(value = "关联业务信息")
    private String businessKey3;

    @ApiModelProperty(value = "消息队列主机")
    private String mqHost;

    @ApiModelProperty(value = "消息队列端口")
    private Integer mqPort;

    @ApiModelProperty(value = "消息队列虚拟主机")
    private String mqVirtualhost;

    @ApiModelProperty(value = "队列名称")
    private String mqQueue;

    @ApiModelProperty(value = "通知次数")
    private Integer informNum;

    @ApiModelProperty(value = "处理状态，0:初始，1:成功，2:失败")
    private Integer state;

    @ApiModelProperty(value = "回复失败时间")
    private LocalDateTime returnfailureDate;

    @ApiModelProperty(value = "回复成功时间")
    private LocalDateTime returnsuccessDate;

    @ApiModelProperty(value = "回复失败内容")
    private String returnfailureMsg;

    @ApiModelProperty(value = "最近通知时间")
    private LocalDateTime informDate;

    private String stageState1;

    private String stageState2;

    private String stageState3;

    private String stageState4;


}
