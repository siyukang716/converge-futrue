package com.cloud.sys.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wjg
 * @ClassName: CustomerLoginEntity
 * @Description: TODO(这里用一句话描述这个类的作用)用户登录表
 * @date 2021-08-05
 */


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("customer_login")
public class CustomerLoginEntity extends Model<CustomerLoginEntity> implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 用户ID
     */
    @TableId(value = "customer_id", type = IdType.ID_WORKER)
    private Integer customerId;


    /**
     * 用户登录名
     */
    @TableField("login_name")
    private String loginName;


    /**
     * md5加密的密码
     */
    @TableField("password")
    private String password;


    /**
     * 用户状态
     */
    @TableField("user_stats")
    private Integer userStats;


    /**
     * 最后修改时间
     */
    @TableField(value = "modified_time",fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date modifiedTime;


}