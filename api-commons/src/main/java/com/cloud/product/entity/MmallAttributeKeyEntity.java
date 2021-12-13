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
 * @ClassName: MmallAttributeKeyEntity
 * @Description: TODO(这里用一句话描述这个类的作用)商品spu 属性key表
 * @date 2021-08-24
 */


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mmall_attribute_key")
@ApiModel("商品spu 属性key表")
public class MmallAttributeKeyEntity extends Model<MmallAttributeKeyEntity> implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 主键
     */
    @TableId(value = "mak_id", type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Integer makId;


    /**
     * 商品id
     */
    /*@TableField("product_id")
    @ApiModelProperty(value = "商品id")
    private Integer productId;*/


    /**
     * 属性名称
     */
    @TableField("attribute_name")
    @ApiModelProperty(value = "属性名称")
    private String attributeName;
    /**
     * 类型名称
     */
    @TableField("type_name")
    @ApiModelProperty(value = "类型名称")
    private String typeName;
    /**
     * 类型名称
     */
    @TableField("is_select")
    @ApiModelProperty(value = "是否下拉框")
    private String isSelect;


    /**
     * 排序
     */
    @TableField("name_sort")
    @ApiModelProperty(value = "排序")
    private Integer nameSort;


    /**
     * 新增时间
     */
    @TableField(value = "add_time",fill = FieldFill.INSERT)
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