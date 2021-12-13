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
 * @ClassName: CustomerAddrEntity
 * @Description: TODO(这里用一句话描述这个类的作用)用户地址表
 * @date 2021-08-05
 */


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("customer_addr")
public class CustomerAddrEntity extends Model<CustomerAddrEntity> implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 自增主键ID
     */
    @TableId(value = "customer_addr_id", type = IdType.AUTO)
    private Integer customerAddrId;


    /**
     * customer_login表的自增ID
     */
    @TableField("customer_id")
    private Long customerId;


    /**
     * 邮编
     */
    @TableField("zip")
    private Integer zip;


    /**
     * 地区表中省份的ID
     */
    @TableField("province")
    private Integer province;


    /**
     * 地区表中城市的ID
     */
    @TableField("city")
    private Integer city;


    /**
     * 地区表中的区ID
     */
    @TableField("district")
    private Integer district;


    /**
     * 具体的地址门牌号
     */
    @TableField("address")
    private String address;


    /**
     * 是否默认
     */
    @TableField("is_default")
    private Integer isDefault;


    /**
     * 最后修改时间
     */
    @TableField(value = "modified_time",fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date modifiedTime;


}