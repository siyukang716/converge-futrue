package com.cloud.cosmetology.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author 豆芽菜
 * @ClassName: CUser
 * @Description: 美容院-顾客
 * @date 2022-10-06
 */


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("c_user")
@ApiModel("美容院-顾客")
public class CUserEntity extends Model<CUserEntity> implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 用户编号
     */
    @TableId(value = "user_id", type = IdType.AUTO)
    @ApiModelProperty(value = "用户编号")
    private Long userId;


    /**
     * 用户名
     */
    @TableField("username")
    @ApiModelProperty(value = "用户名")
    private String username;


    /**
     * 别称
     */
    @TableField("nick_name")
    @ApiModelProperty(value = "别称")
    private String nickName;


    /**
     * 手机号
     */
    @TableField("mobile")
    @ApiModelProperty(value = "手机号")
    private String mobile;


    /**
     * 生日
     */
    @TableField("birthday")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "生日")
    private Date birthday;


    /**
     * 邮箱
     */
    @TableField("email")
    @ApiModelProperty(value = "邮箱")
    private String email;


    /**
     * 密码
     */
    @TableField("password")
    @ApiModelProperty(value = "密码")
    private String password;


    /**
     * 余额
     */
    @TableField("amt")
    @ApiModelProperty(value = "余额")
    private BigDecimal amt;


    /**
     * 公司id
     */
    @TableField("company_id")
    @ApiModelProperty(value = "公司id")
    private Long companyId;


    /**
     * 用户id
     */
    @TableField(value = "insert_uid", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "用户id")
    private Long insertUid;


    /**
     * 注册时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "insert_time", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "注册时间")
    private Date insertTime;


    /**
     * 用户id
     */
    @TableField(value = "update_uid", fill = FieldFill.UPDATE)
    @ApiModelProperty(value = "用户id")
    private Long updateUid;


    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;


    /**
     * 是否删除（0：正常；1：已删）
     */
    @TableField(value = "is_del", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "是否删除（0：正常；1：已删）")
    private Integer isDel;


}