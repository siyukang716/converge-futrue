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
 * @ClassName: CCashier
 * @Description: 美容院-收银管理
 * @date 2022-10-06
 */


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("c_cashier")
@ApiModel("美容院-收银管理")
public class CCashierEntity extends Model<CCashierEntity> implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 收银编号
     */
    @TableId("cashier_id")
    @ApiModelProperty(value = "收银编号")
    private Long cashierId;


    /**
     * 商品ID
     */
    @TableField("prod_id")
    @ApiModelProperty(value = "商品ID")
    private Long prodId;


    /**
     * 用户ID
     */
    @TableField("user_id")
    @ApiModelProperty(value = "用户ID")
    private Long userId;



    /**
     * 收益ID
     */
    @TableField("staff_id")
    @ApiModelProperty(value = "收益ID")
    private Long staffId;

    /**
     * 提成金
     */
    @TableField("royalty")
    @ApiModelProperty(value = "提成金")
    private BigDecimal royalty;

    /****************************************************************************/

    /**
     * 顾客名称
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "用户名")
    private String username;
    /**
     * 提成人名称
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "提成人名称")
    private String staffName;

    /**
     * 别称
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "别称")
    private String nickName;


    /**
     * 商品名称
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "商品名称")
    private String prodName;


    /**
     * 商品标题
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "商品标题")
    private String prodTitle;


    /**
     * 商品描述
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "商品描述")
    private String prodDesc;


    /**
     * 商品金额
     */
    @TableField(exist = false)
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