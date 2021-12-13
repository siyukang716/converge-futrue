package com.cloud.product.entity;

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
 * @ClassName: ProductSpecsEntity
 * @Description: 商品 sku信息
 * @date 2021-08-26
 */


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("product_specs")
@ApiModel("商品 sku信息")
public class ProductSpecsEntity extends Model<ProductSpecsEntity> implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * id
     */
    @TableId(value = "specs_id", type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private Integer specsId;


    /**
     * sku 描述
     */
    @TableField("sku_depict")
    @ApiModelProperty(value = "sku 描述")
    private String skuDepict; /**
     * 商品id
     */
    @TableField("product_id")
    @ApiModelProperty(value = "商品id")
    private Integer productId;


    /**
     * 商品价格
     */
    @TableField("product_price")
    @ApiModelProperty(value = "商品价格")
    private BigDecimal productPrice;


    /**
     * insert时间
     */
    @TableField(value = "add_tiem",fill = FieldFill.INSERT)
    @ApiModelProperty(value = "insert时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date addTiem;


    /**
     * update时间
     */
    @TableField(value = "modified_time",fill = FieldFill.UPDATE)
    @ApiModelProperty(value = "update时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date modifiedTime;

    @TableField(exist = false)
    private String productName;

}