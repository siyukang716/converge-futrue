package com.cloud.order.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author wjg
 * @ClassName: OrderCartEntity
 * @Description: TODO(这里用一句话描述这个类的作用)购物车表
 * @date 2021-08-05
 */


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("order_cart")
public class OrderCartEntity extends Model<OrderCartEntity> implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 购物车ID
     */
    @TableId(value = "cart_id", type = IdType.AUTO)
    private Integer cartId;


    /**
     * 用户ID
     */
    @TableField("customer_id")
    private Integer customerId;


    /**
     * 商品ID
     */
    @TableField("product_id")
    private Integer productId;


    /**
     * 加入购物车商品数量
     */
    @TableField("product_amount")
    private Integer productAmount;


    /**
     * 商品价格
     */
    @TableField("price")
    private BigDecimal price;


    /**
     * 加入购物车时间
     */
    @TableField(value = "add_time",fill = FieldFill.INSERT)
    private Date addTime;


    /**
     * 最后修改时间
     */
    @TableField(value = "modified_time",fill = FieldFill.UPDATE)
    private LocalDateTime modifiedTime;


}