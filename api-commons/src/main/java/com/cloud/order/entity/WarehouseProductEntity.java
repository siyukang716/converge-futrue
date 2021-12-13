package com.cloud.order.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author wjg
 * @ClassName: WarehouseProductEntity
 * @Description: TODO(这里用一句话描述这个类的作用)商品库存表
 * @date 2021-08-05
 */


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("warehouse_product")
public class WarehouseProductEntity extends Model<WarehouseProductEntity> implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 商品库存ID
     */
    @TableId(value = "wp_id", type = IdType.AUTO)
    private Integer wpId;


    /**
     * 商品ID
     */
    @TableField("product_id")
    private Integer productId;


    /**
     * 仓库ID
     */
    @TableField("w_id")
    private Integer wid;


    /**
     * 当前商品数量
     */
    @TableField("current_cnt")
    private Integer currentCnt;


    /**
     * 当前占用数据
     */
    @TableField("lock_cnt")
    private Integer lockCnt;


    /**
     * 在途数据
     */
    @TableField("in_transit_cnt")
    private Integer inTransitCnt;


    /**
     * 移动加权成本
     */
    @TableField("average_cost")
    private BigDecimal averageCost;


    /**
     * 最后修改时间
     */
    @TableField(value = "modified_time",fill = FieldFill.UPDATE)
    private Date modifiedTime;


}