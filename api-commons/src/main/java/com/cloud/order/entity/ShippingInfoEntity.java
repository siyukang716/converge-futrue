package com.cloud.order.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author wjg
 * @ClassName: ShippingInfoEntity
 * @Description: TODO(这里用一句话描述这个类的作用)物流公司信息表
 * @date 2021-08-05
 */


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("shipping_info")
@ApiModel("物流公司信息表")
public class ShippingInfoEntity extends Model<ShippingInfoEntity> implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 主键ID
     */
    @TableId(value = "ship_id", type = IdType.AUTO)
    @ApiModelProperty(value = "主键ID")
    private Integer shipId;


    /**
     * 物流公司名称
     */
    @TableField("ship_name")
    @ApiModelProperty(value = "物流公司名称")
    private String shipName;


    /**
     * 物流公司联系人
     */
    @TableField("ship_contact")
    @ApiModelProperty(value = "物流公司联系人")
    private String shipContact;


    /**
     * 物流公司联系电话
     */
    @TableField("telephone")
    @ApiModelProperty(value = "物流公司联系电话")
    private String telephone;


    /**
     * 配送价格
     */
    @TableField("price")
    @ApiModelProperty(value = "配送价格")
    private BigDecimal price;


    /**
     * 最后修改时间
     */
    @TableField(value = "modified_time",fill = FieldFill.UPDATE)
    @ApiModelProperty(value = "最后修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date modifiedTime;


}