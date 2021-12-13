package com.cloud.product.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wjg
 * @ClassName: SupplierInfoEntity
 * @Description: TODO(这里用一句话描述这个类的作用)供应商信息表
 * @date 2021-08-05
 */


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("supplier_info")
public class SupplierInfoEntity extends Model<SupplierInfoEntity> implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 供应商ID
     */
    @TableId(value = "supplier_id", type = IdType.AUTO)
    private Integer supplierId;


    /**
     * 供应商编码
     */
    @TableField("supplier_code")
    private String supplierCode;


    /**
     * 供应商名称
     */
    @TableField("supplier_name")
    private String supplierName;


    /**
     * 供应商类型：1.自营，2.平台
     */
    @TableField("supplier_type")
    private Integer supplierType;


    /**
     * 供应商联系人
     */
    @TableField("link_man")
    private String linkMan;


    /**
     * 联系电话
     */
    @TableField("phone_number")
    private String phoneNumber;


    /**
     * 供应商开户银行名称
     */
    @TableField("bank_name")
    private String bankName;


    /**
     * 银行账号
     */
    @TableField("bank_account")
    private String bankAccount;


    /**
     * 供应商地址
     */
    @TableField("address")
    private String address;


    /**
     * 状态：0禁止，1启用
     */
    @TableField("supplier_status")
    private Integer supplierStatus;


    /**
     * 最后修改时间
     */
    @TableField(value = "modified_time",fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date modifiedTime;


}