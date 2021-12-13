package com.cloud.sys.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author wjg
 * @ClassName: CustomerPointLogEntity
 * @Description: TODO(这里用一句话描述这个类的作用)用户积分日志表
 * @date 2021-08-05
 */


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("customer_point_log")
public class CustomerPointLogEntity extends Model<CustomerPointLogEntity> implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 积分日志ID
     */
    @TableId(value = "point_id", type = IdType.AUTO)
    private Integer pointId;


    /**
     * 用户ID
     */
    @TableField("customer_id")
    private Integer customerId;


    /**
     * 积分来源：0订单，1登陆，2活动
     */
    @TableField("source")
    private Integer source;


    /**
     * 积分来源相关编号
     */
    @TableField("refer_number")
    private Integer referNumber;


    /**
     * 变更积分数
     */
    @TableField("change_point")
    private Integer changePoint;


    /**
     * 积分日志生成时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;


}