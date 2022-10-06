package com.cloud.applets.entity;

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
 * @ClassName: AppletsUser
 * @Description: 小程序用户表
 * @date 2021-11-13
 */


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_applets_user")
@ApiModel("小程序用户表")
public class AppletsUserEntity extends Model<AppletsUserEntity> implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 微信用户主键
     */
    @TableId("wx_user_id")
    @ApiModelProperty(value = "微信用户主键")
    private Long wxUserId;


    /**
     * 小程序id
     */
    @TableField("app_id")
    @ApiModelProperty(value = "小程序id")
    private String appId;


    /**
     * 用户唯一标识
     */
    @TableField("open_id")
    @ApiModelProperty(value = "用户唯一标识")
    private String openId;


    /**
     * 会话密钥
     */
    @TableField("session_key")
    @ApiModelProperty(value = "会话密钥")
    private String sessionKey;


    /**
     * 用户在开放平台的唯一标识符
     */
    @TableField("union_id")
    @ApiModelProperty(value = "用户在开放平台的唯一标识符")
    private String unionId;


    /**
     * 微信用户名称
     */
    @TableField("nick_name")
    @ApiModelProperty(value = "微信用户名称")
    private String nickName;


    /**
     * 性别
     */
    @TableField("gender")
    @ApiModelProperty(value = "性别")
    private Integer gender;


    /**
     * 城市
     */
    @TableField("language")
    @ApiModelProperty(value = "语言")
    private String language;


    /**
     * 微信头像地址
     */
    @TableField("avatar_url")
    @ApiModelProperty(value = "微信头像地址")
    private String avatarUrl;


    /**
     * 新增时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "insert_time", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "新增时间")
    private Date insertTime;


    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;


}