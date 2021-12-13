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
 * @ClassName: BrandInfoEntity
 * @Description: TODO(这里用一句话描述这个类的作用)品牌信息表
 * @date 2021-08-05
 */


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("brand_info")
public class BrandInfoEntity extends Model<BrandInfoEntity> implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 品牌ID
     */
    @TableId(value = "brand_id", type = IdType.AUTO)
    private Integer brandId;


    /**
     * 品牌名称
     */
    @TableField("brand_name")
    private String brandName;


    /**
     * 联系电话
     */
    @TableField("telephone")
    private String telephone;


    /**
     * 品牌网络
     */
    @TableField("brand_web")
    private String brandWeb;


    /**
     * 品牌logo URL
     */
    @TableField("brand_logo")
    private String brandLogo;


    /**
     * 品牌描述
     */
    @TableField("brand_desc")
    private String brandDesc;


    /**
     * 品牌状态,0禁用,1启用
     */
    @TableField("brand_status")
    private Integer brandStatus;


    /**
     * 排序
     */
    @TableField("brand_order")
    private Integer brandOrder;


    /**
     * 最后修改时间
     */
    @TableField(value = "modified_time",fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date modifiedTime;


}