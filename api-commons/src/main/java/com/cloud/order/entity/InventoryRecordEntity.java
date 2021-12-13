package com.cloud.order.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wjg
 * @ClassName: InventoryRecordEntity
 * @Description: 库存记录
 * @date 2021-08-29
 */


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("inventory_record")
@ApiModel("库存记录")
public class InventoryRecordEntity extends Model<InventoryRecordEntity> implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 主键
     */
    @TableId(value = "inv_record_id", type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Integer invRecordId;


    /**
     * 业务id
     */
    @TableField("business_id")
    @ApiModelProperty(value = "业务id")
    private Integer businessId;


    /**
     * 业务类型 1 订单 2 进货
     */
    @TableField("business_type")
    @ApiModelProperty(value = "业务类型 1 订单 2 进货")
    private String businessType;


    /**
     * 库存操作 1 加 2 减
     */
    @TableField("inventory_operations")
    @ApiModelProperty(value = "库存操作 1 加 2 减")
    private String inventoryOperations;


    /**
     * 数额
     */
    @TableField("amount")
    @ApiModelProperty(value = "数额")
    private Integer amount;


    @TableField(value = "add_time", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date addTime;


    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date updateTime;


}