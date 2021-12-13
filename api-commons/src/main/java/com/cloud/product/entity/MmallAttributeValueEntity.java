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
 * @ClassName: MmallAttributeValueEntity
 * @Description: TODO(这里用一句话描述这个类的作用)商品 sku   属性value表
 * @date 2021-08-24
 */


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mmall_attribute_value")
@ApiModel("商品 sku   属性value表")
public class MmallAttributeValueEntity extends Model<MmallAttributeValueEntity> implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 主键id
     */
    @TableId(value = "mav_id", type = IdType.AUTO)
    @ApiModelProperty(value = "主键id")
    private Integer mavId;


    /**
     * key_id
     */
    @TableField("mak_id")
    @ApiModelProperty(value = "key_id")
    private Integer makId;


    /**
     * 属性选项值
     */
    @TableField("attribute_value")
    @ApiModelProperty(value = "属性选项值")
    private String attributeValue;


    /**
     * 排序
     */
    @TableField("value_sort")
    @ApiModelProperty(value = "排序")
    private String valueSort;


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