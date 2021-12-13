package com.cloud.product.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wjg
 * @ClassName: ProductCategoryEntity
 * @Description: TODO(这里用一句话描述这个类的作用)商品分类表
 * @date 2021-08-05
 */


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("product_category")
public class ProductCategoryEntity extends Model<ProductCategoryEntity> implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 分类ID
     */
    @TableId(value = "category_id", type = IdType.AUTO)
    private Integer categoryId;


    /**
     * 分类名称
     */
    @TableField("category_name")
    private String categoryName;


    /**
     * 分类编码
     */
    @TableField("category_code")
    private String categoryCode;


    /**
     * 父分类ID
     */
    @TableField("parent_id")
    private Integer parentId;


    /**
     * 分类层级
     */
    @TableField("category_level")
    private Integer categoryLevel;


    /**
     * 分类状态
     */
    //@TableLogic
    @TableField(value = "category_status")
    private Integer categoryStatus;


    /**
     * 最后修改时间
     */
    @TableField(value = "modified_time",fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date modifiedTime;


}