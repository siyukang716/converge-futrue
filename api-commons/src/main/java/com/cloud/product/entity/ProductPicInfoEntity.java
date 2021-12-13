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
 * @ClassName: ProductPicInfoEntity
 * @Description: TODO(这里用一句话描述这个类的作用)商品图片信息表
 * @date 2021-08-05
 */


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("product_pic_info")
public class ProductPicInfoEntity extends Model<ProductPicInfoEntity> implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 商品图片ID
     */
    @TableId(value = "product_pic_id", type = IdType.AUTO)
    private Integer productPicId;


    /**
     * 商品ID
     */
    @TableField("product_id")
    private Integer productId;


    /**
     * 图片描述
     */
    @TableField("pic_desc")
    private String picDesc;


    /**
     * 图片URL
     */
    @TableField("pic_url")
    private String picUrl;


    /**
     * 是否主图：0.非主图1.主图
     */
    @TableField("is_master")
    private Integer isMaster;


    /**
     * 图片排序
     */
    @TableField("pic_order")
    private Integer picOrder;


    /**
     * 图片是否有效：0无效 1有效
     */
    @TableField("pic_status")
    private Integer picStatus;


    /**
     * 最后修改时间
     */
    @TableField(value = "modified_time",fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date modifiedTime;


}