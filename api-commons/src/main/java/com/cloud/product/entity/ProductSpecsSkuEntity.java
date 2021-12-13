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
 * @ClassName: ProductSpecsSkuEntity
 * @Description: 商品sku 具体属性值
 * @date 2021-08-26
 */


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("product_specs_sku")
@ApiModel("商品sku 具体属性值")
public class ProductSpecsSkuEntity extends Model<ProductSpecsSkuEntity> implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * id
     */
    @TableId(value = "psc_id", type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private Integer pscId;


    /**
     * 商品skuid
     */
    @TableField("specs_id")
    @ApiModelProperty(value = "商品skuid")
    private Integer specsId;


    /**
     * 商品sku具体名称
     */
    @TableField("`key`")
    @ApiModelProperty(value = "商品sku具体名称")
    private String key;


    /**
     * 商品sku具体值
     */
    @TableField("`value`")
    @ApiModelProperty(value = "商品sku具体值")
    private String value;


    @TableField(value = "add_time",fill = FieldFill.INSERT)
    @ApiModelProperty(value = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date addTime;


    @TableField(value = "modified_time",fill = FieldFill.UPDATE)
    @ApiModelProperty(value = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date modifiedTime;


}