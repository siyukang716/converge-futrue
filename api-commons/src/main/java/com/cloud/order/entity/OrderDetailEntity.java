package com.cloud.order.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author wjg
 * @ClassName: OrderDetailEntity
 * @Description: 订单详情表
 * @date 2021-08-29
 */


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("order_detail")
@ApiModel("订单详情表")
public class OrderDetailEntity extends Model<OrderDetailEntity> implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 订单详情表ID
     */
    @TableId(value = "order_detail_id", type = IdType.AUTO)
    @ApiModelProperty(value = "订单详情表ID")
    private Integer orderDetailId;


    /**
     * 订单表ID
     */
    @TableField("order_id")
    @ApiModelProperty(value = "订单表ID")
    private Integer orderId;


    /**
     * 订单商品ID
     */
    @TableField("product_id")
    @ApiModelProperty(value = "订单商品ID")
    private Integer productId;


    /**
     * 商品名称
     */
    @TableField("product_name")
    @ApiModelProperty(value = "商品名称")
    private String productName;


    /**
     * 购买商品数量
     */
    @TableField("product_cnt")
    @ApiModelProperty(value = "购买商品数量")
    private Integer productCnt;


    /**
     * 购买商品单价
     */
    @TableField("product_price")
    @ApiModelProperty(value = "购买商品单价")
    private BigDecimal productPrice;


    /**
     * 平均成本价格
     */
    @TableField("average_cost")
    @ApiModelProperty(value = "平均成本价格")
    private BigDecimal averageCost;


    /**
     * 商品重量
     */
    @TableField("weight")
    @ApiModelProperty(value = "商品重量")
    private Float weight;


    /**
     * 优惠分摊金额
     */
    @TableField("fee_money")
    @ApiModelProperty(value = "优惠分摊金额")
    private BigDecimal feeMoney;


    /**
     * 仓库ID
     */
    @TableField("w_id")
    @ApiModelProperty(value = "仓库ID")
    private Integer wId;


    /**
     * 最后修改时间
     */
    @TableField(value = "modified_time",fill = FieldFill.UPDATE)
    @ApiModelProperty(value = "最后修改时间")
    private Date modifiedTime;


}