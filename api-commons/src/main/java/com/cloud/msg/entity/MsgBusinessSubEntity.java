package com.cloud.msg.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
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
 * @author 豆芽菜
 * @ClassName: MsgBusinessSub
 * @Description: 消息订阅表
 * @date 2021-10-14
 */


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("msg_business_sub")
@ApiModel("消息订阅表")
public class MsgBusinessSubEntity extends Model<MsgBusinessSubEntity> implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 主键
     */
    @TableId("msg_link_id")
    @ApiModelProperty(value = "主键")
    private Long msgLinkId;


    /**
     * 业务消息主键
     */
    @TableField("msg_id")
    @ApiModelProperty(value = "业务消息主键")
    private Long msgId;


    /**
     * 用户id
     */
    @TableField("user_id")
    @ApiModelProperty(value = "用户id")
    private Long userId;


    /**
     * 阅读状态 1 未读 2 已读
     */
    @TableField("msg_status")
    @ApiModelProperty(value = "阅读状态 1 未读 2 已读")
    private Long msgStatus;


    /**
     * 阅读时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "阅读时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date updateTime;


}