package com.cloud.product.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * @author wjg
 * @ClassName: ProductInfoEntity
 * @Description: TODO(这里用一句话描述这个类的作用)商品信息表
 * @date 2021-08-05
 */


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("product_info")
public class ProductInfoEntity extends Model<ProductInfoEntity> implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 商品ID
     */
    @TableId(value = "product_id", type = IdType.AUTO)
    private Integer productId;


    /**
     * 商品编码
     */
    @TableField("product_core")
    private String productCore;


    /**
     * 商品名称
     */
    @TableField("product_name")
    private String productName;


    /**
     * 国条码
     */
    @TableField("bar_code")
    private String barCode;


    /**
     * 品牌表的ID
     */
    @TableField("brand_id")
    private Integer brandId;


    /**
     * 一级分类ID
     */
    @TableField("one_category_id")
    private Integer oneCategoryId;


    /**
     * 二级分类ID
     */
    @TableField("two_category_id")
    private Integer twoCategoryId;


    /**
     * 三级分类ID
     */
    @TableField("three_category_id")
    private Integer threeCategoryId;


    /**
     * 商品的供应商ID
     */
    @TableField("supplier_id")
    private Integer supplierId;


    /**
     * 商品销售价格
     */
    @TableField("price")
    private BigDecimal price;


    /**
     * 商品加权平均成本
     */
    @TableField("average_cost")
    private BigDecimal averageCost;


    /**
     * 上下架状态：0下架1上架
     */
    @TableField("publish_status")
    private Integer publishStatus;


    /**
     * 审核状态：0未审核，1已审核
     */
    @TableField("audit_status")
    private Integer auditStatus;


    /**
     * 商品重量
     */
    @TableField("weight")
    private Float weight;


    /**
     * 商品长度
     */
    @TableField("length")
    private Float length;


    /**
     * 商品高度
     */
    @TableField("height")
    private Float height;


    /**
     * 商品宽度
     */
    @TableField("width")
    private Float width;


    @TableField("color_type")
    private String colorType;


    /**
     * 生产日期
     */
    @TableField("production_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date productionDate;


    /**
     * 商品有效期
     */
    @TableField("shelf_life")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date shelfLife;


    /**
     * 商品描述
     */
    @TableField("descript")
    private String descript;


    /**
     * 商品录入时间
     */
    @TableField("indate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date indate;


    /**
     * 最后修改时间
     */
    @TableField(value = "modified_time",fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date modifiedTime;

    @TableField(exist = false)
    private Map pic;
}