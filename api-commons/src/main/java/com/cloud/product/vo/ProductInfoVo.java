package com.cloud.product.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author wjg
 * @ClassName: ProductInfoEntity
 * @Description: TODO(这里用一句话描述这个类的作用)商品信息表
 * @date 2021-08-05
 */


@Data
public class ProductInfoVo implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 商品ID
     */
    private Integer productId;


    /**
     * 商品编码
     */
    private String productCore;


    /**
     * 商品名称
     */
    private String productName;


    /**
     * 国条码
     */
    private String barCode;


    /**
     * 品牌表的ID
     */
    private Integer brandId;
    private String brandName;


    /**
     * 一级分类ID
     */
    private Integer oneCategoryId;
    private String oneCategoryName;


    /**
     * 二级分类ID
     */
    private Integer twoCategoryId;
    private String twoCategoryName;

    /**
     * 三级分类ID
     */
    private Integer threeCategoryId;
    private String threeCategoryName;

    /**
     * 商品的供应商ID
     */
    private Integer supplierId;
    private String supplierName;


    /**
     * 商品销售价格
     */
    private BigDecimal price;


    /**
     * 商品加权平均成本
     */
    private BigDecimal averageCost;


    /**
     * 上下架状态：0下架1上架
     */
    private Integer publishStatus;


    /**
     * 审核状态：0未审核，1已审核
     */
    private Integer auditStatus;


    /**
     * 商品重量
     */
    private Float weight;


    /**
     * 商品长度
     */
    private Float length;


    /**
     * 商品高度
     */
    private Float height;


    /**
     * 商品宽度
     */
    private Float width;


    private String colorType;


    /**
     * 生产日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date productionDate;


    /**
     * 商品有效期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date shelfLife;


    /**
     * 商品描述
     */
    private String descript;


    /**
     * 商品录入时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date indate;


    /**
     * 最后修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date modifiedTime;


}