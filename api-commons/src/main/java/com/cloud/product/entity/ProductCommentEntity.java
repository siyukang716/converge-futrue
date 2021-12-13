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
 * @ClassName: ProductCommentEntity
 * @Description: TODO(这里用一句话描述这个类的作用)商品评论表
 * @date 2021-08-05
 */


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("product_comment")
public class ProductCommentEntity extends Model<ProductCommentEntity> implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 评论ID
     */
    @TableId(value = "comment_id", type = IdType.ID_WORKER)
    private Integer commentId;


    /**
     * 商品ID
     */
    @TableField("product_id")
    private Integer productId;


    /**
     * 订单ID
     */
    @TableField("order_id")
    private Long orderId;


    /**
     * 用户ID
     */
    @TableField("customer_id")
    private Integer customerId;


    /**
     * 评论标题
     */
    @TableField("title")
    private String title;


    /**
     * 评论内容
     */
    @TableField("content")
    private String content;


    /**
     * 审核状态：0未审核，1已审核
     */
    @TableField("audit_status")
    private Integer auditStatus;


    /**
     * 评论时间
     */
    @TableField(value = "audit_time",fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date auditTime;


    /**
     * 最后修改时间
     */
    @TableField(value = "modified_time",fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date modifiedTime;


}