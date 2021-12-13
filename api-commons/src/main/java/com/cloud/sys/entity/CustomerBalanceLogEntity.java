package com.cloud.sys.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * @author wjg
 * @ClassName: CustomerBalanceLogEntity
 * @Description: TODO(这里用一句话描述这个类的作用)用户余额变动表
 * @date 2021-08-05
 */


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("customer_balance_log")
public class CustomerBalanceLogEntity extends Model<CustomerBalanceLogEntity> implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 余额日志ID
     */
    @TableId(value = "balance_id", type = IdType.AUTO)
    private Integer balanceId;


    /**
     * 用户ID
     */
    @TableField("customer_id")
    private Integer customerId;


    /**
     * 记录来源：1订单，2退货单
     */
    @TableField("source")
    private Integer source;


    /**
     * 相关单据ID
     */
    @TableField("source_sn")
    private Integer sourceSn;


    /**
     * 记录生成时间
     */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date createTime;


    /**
     * 变动金额
     */
    @TableField("amount")
    private BigDecimal amount;


}