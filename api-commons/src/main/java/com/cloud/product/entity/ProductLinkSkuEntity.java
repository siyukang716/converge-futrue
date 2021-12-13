package com.cloud.product.entity;

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
 * @ClassName: ProductLinkSkuEntity
 * @Description: 商品 属性关联表
 * @date 2021-08-25
 */


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("product_link_sku")
@ApiModel("商品 属性关联表")
public class ProductLinkSkuEntity extends Model<ProductLinkSkuEntity> implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * id
     */
    @TableId(value = "pm_id", type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private Integer pmId;


    /**
     * sku属性id
     */
    @TableField("mak_id")
    @ApiModelProperty(value = "sku属性id")
    private Integer makId;


    /**
     * 商品id
     */
    @TableField("product_id")
    @ApiModelProperty(value = "商品id")
    private Integer productId;


    /**
     * 新增时间
     */
    @TableField(value = "add_time",fill = FieldFill.UPDATE)
    @ApiModelProperty(value = "新增时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date addTime;


    /**
     * 修改时间
     */
    @TableField(value = "modified_time",fill = FieldFill.UPDATE)
    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date modifiedTime;


}