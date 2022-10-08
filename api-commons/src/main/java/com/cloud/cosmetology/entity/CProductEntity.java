package com.cloud.cosmetology.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author 豆芽菜
 * @ClassName: CProduct
 * @Description: 美容院-商品
 * @date 2022-10-06
 */


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("c_product")
@ApiModel("美容院-商品")
public class CProductEntity extends Model<CProductEntity> implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 商品ID
     */
    @TableId("prod_id")
    @ApiModelProperty(value = "商品ID")
    private Long prodId;


    /**
     * 商品名称
     */
    @TableField("prod_name")
    @ApiModelProperty(value = "商品名称")
    private String prodName;


    /**
     * 商品标题
     */
    @TableField("prod_title")
    @ApiModelProperty(value = "商品标题")
    private String prodTitle;


    /**
     * 商品描述
     */
    @TableField("prod_desc")
    @ApiModelProperty(value = "商品描述")
    private String prodDesc;


    /**
     * 商品金额
     */
    @TableField("prod_amt")
    @ApiModelProperty(value = "商品金额")
    private BigDecimal prodAmt;


    /**
     * 用户id
     */
    @TableField(value = "insert_uid", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "用户id")
    private Long insertUid;


    /**
     * 注册时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "insert_time", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "注册时间")
    private Date insertTime;


    /**
     * 用户id
     */
    @TableField(value = "update_uid", fill = FieldFill.UPDATE)
    @ApiModelProperty(value = "用户id")
    private Long updateUid;


    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;


    /**
     * 是否删除（0：正常；1：已删）
     */
    @TableField(value = "is_del", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "是否删除（0：正常；1：已删）")
    private Integer isDel;


}