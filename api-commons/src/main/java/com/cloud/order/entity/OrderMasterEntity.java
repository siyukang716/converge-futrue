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
 * @ClassName: OrderMasterEntity
 * @Description: 订单主表
 * @date 2021-08-29
 */


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("order_master")
@ApiModel("订单主表")
public class OrderMasterEntity extends Model<OrderMasterEntity> implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 订单ID
     */
    @TableId(value = "order_id", type = IdType.AUTO)
    @ApiModelProperty(value = "订单ID")
    private Integer orderId;


    /**
     * 订单编号 yyyymmddnnnnnnnn
     */
    @TableField("order_sn")
    @ApiModelProperty(value = "订单编号 yyyymmddnnnnnnnn")
    private Long orderSn;


    /**
     * 下单人ID
     */
    @TableField("customer_id")
    @ApiModelProperty(value = "下单人ID")
    private Long customerId;


    /**
     * 收货人姓名
     */
    @TableField("shipping_user")
    @ApiModelProperty(value = "收货人姓名")
    private String shippingUser;


    /**
     * 省
     */
    @TableField("province")
    @ApiModelProperty(value = "省")
    private Integer province;


    /**
     * 市
     */
    @TableField("city")
    @ApiModelProperty(value = "市")
    private Integer city;


    /**
     * 区
     */
    @TableField("district")
    @ApiModelProperty(value = "区")
    private Integer district;


    /**
     * 地址
     */
    @TableField("address")
    @ApiModelProperty(value = "地址")
    private String address;


    /**
     * 支付方式：1现金，2余额，3网银，4支付宝，5微信
     */
    @TableField("payment_method")
    @ApiModelProperty(value = "支付方式：1现金，2余额，3网银，4支付宝，5微信")
    private Integer paymentMethod;


    /**
     * 订单金额
     */
    @TableField("order_money")
    @ApiModelProperty(value = "订单金额")
    private BigDecimal orderMoney;


    /**
     * 优惠金额
     */
    @TableField("district_money")
    @ApiModelProperty(value = "优惠金额")
    private BigDecimal districtMoney;


    /**
     * 运费金额
     */
    @TableField("shipping_money")
    @ApiModelProperty(value = "运费金额")
    private BigDecimal shippingMoney;


    /**
     * 支付金额
     */
    @TableField("payment_money")
    @ApiModelProperty(value = "支付金额")
    private BigDecimal paymentMoney;


    /**
     * 快递公司名称
     */
    @TableField("shipping_comp_name")
    @ApiModelProperty(value = "快递公司名称")
    private String shippingCompName;


    /**
     * 快递单号
     */
    @TableField("shipping_sn")
    @ApiModelProperty(value = "快递单号")
    private String shippingSn;


    /**
     * 下单时间
     */
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "下单时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date createTime;


    /**
     * 发货时间
     */
    @TableField("shipping_time")
    @ApiModelProperty(value = "发货时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date shippingTime;


    /**
     * 支付时间
     */
    @TableField("pay_time")
    @ApiModelProperty(value = "支付时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date payTime;


    /**
     * 收货时间
     */
    @TableField("receive_time")
    @ApiModelProperty(value = "收货时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date receiveTime;


    /**
     * 订单状态
     */
    @TableField("order_status")
    @ApiModelProperty(value = "订单状态")
    private Integer orderStatus;


    /**
     * 订单积分
     */
    @TableField("order_point")
    @ApiModelProperty(value = "订单积分")
    private Integer orderPoint;


    /**
     * 发票抬头
     */
    @TableField("invoice_time")
    @ApiModelProperty(value = "发票抬头")
    private String invoiceTime;


    /**
     * 最后修改时间
     */
    @TableField(value = "modified_time",fill = FieldFill.UPDATE)
    @ApiModelProperty(value = "最后修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date modifiedTime;


}