package com.cloud.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wjg
 * @ClassName: CustomerLoginLogEntity
 * @Description: TODO(这里用一句话描述这个类的作用)用户登陆日志表
 * @date 2021-08-05
 */


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("customer_login_log")
public class CustomerLoginLogEntity extends Model<CustomerLoginLogEntity> implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 登陆日志ID
     */
    @TableId(value = "login_id", type = IdType.AUTO)
    private Integer loginId;


    /**
     * 登陆用户ID
     */
    @TableField("customer_id")
    private Long customerId;


    /**
     * 用户登陆时间
     */
    @TableField("login_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date loginTime;


    /**
     * 登陆IP
     */
    @TableField("login_ip")
    private String loginIp;


    /**
     * 登陆类型：0未成功，1成功
     */
    @TableField("login_type")
    private Integer loginType;


}