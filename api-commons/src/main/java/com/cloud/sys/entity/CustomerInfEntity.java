package com.cloud.sys.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wjg
 * @ClassName: CustomerInfEntity
 * @Description: TODO(这里用一句话描述这个类的作用)用户信息表
 * @date 2021-08-05
 */


@Data
@TableName("customer_inf")
public class CustomerInfEntity extends Model<CustomerInfEntity> implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 自增主键ID
     */
    @TableId(value = "customer_inf_id", type = IdType.AUTO)
    private Integer customerInfId;


    /**
     * customer_login表的自增ID
     */
    @TableField("customer_id")
    private Long customerId;


    /**
     * 用户真实姓名
     */
    @TableField("customer_name")
    private String customerName;


    /**
     * 证件类型：1 身份证，2 军官证，3 护照
     */
    @TableField("identity_card_type")
    private Integer identityCardType;


    /**
     * 证件号码
     */
    @TableField("identity_card_no")
    private String identityCardNo;


    /**
     * 手机号
     */
    @TableField("mobile_phone")
    private String mobilePhone;


    /**
     * 邮箱
     */
    @TableField("customer_email")
    private String customerEmail;


    /**
     * 性别
     */
    @TableField("gender")
    private String gender;

    /**
     * 生日
     */

    @JsonFormat(pattern = "yyyy-MM-dd" ,timezone = "GMT+8")
    private Date birthday;


    /**
     * 年龄
     */
    @TableField(value = "age")
    private Integer age;


    /**
     * 职务/岗位
     */
    private String position;
    /**
     * 入职时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd" ,timezone = "GMT+8")
    @TableField(value = "entry_time")
    private Date entryTime;




    /**
     * 最后修改时间
     */
    @TableField(value = "modified_time",fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date modifiedTime;


}