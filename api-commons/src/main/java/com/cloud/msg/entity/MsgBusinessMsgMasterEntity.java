package com.cloud.msg.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 小可爱
 * @ClassName: MsgBusinessMsgMaster
 * @Description: 消息模块, 业务消息
 * @date 2021-10-14
 */


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("msg_business_msg_master")
@ApiModel("消息模块,业务消息")
public class MsgBusinessMsgMasterEntity extends Model<MsgBusinessMsgMasterEntity> implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 消息主键
     */
    @TableId("msg_id")
    @ApiModelProperty(value = "消息主键")
    private Long msgId;


    /**
     * 消息内容
     */
    @TableField("msg_content")
    @ApiModelProperty(value = "消息内容")
    private String msgContent;


    /**
     * 消息标题
     */
    @TableField("msg_title")
    @ApiModelProperty(value = "消息标题")
    private String msgTitle;


    @TableField("business_id")
    @ApiModelProperty(value = "")
    private Long businessId;


    /**
     * 业务路径
     */
    @TableField("business_url")
    @ApiModelProperty(value = "业务路径")
    private String businessUrl;


    /**
     * 业务类型/来源
     */
    @TableField("business_type")
    @ApiModelProperty(value = "业务类型/来源")
    private String businessType;


    /**
     * 消息状态  1必读  2 非必读
     */
    @TableField("msg_status")
    @ApiModelProperty(value = "消息状态  1必读  2 非必读")
    private String msgStatus;


    /**
     * 消息发布时间
     */
    @TableField("msg_post_time")
    @ApiModelProperty(value = "消息发布时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date msgPostTime;


}