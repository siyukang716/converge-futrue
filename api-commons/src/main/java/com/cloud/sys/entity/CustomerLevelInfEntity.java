package com.cloud.sys.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wjg
 * @ClassName: CustomerLevelInfEntity
 * @Description: TODO(这里用一句话描述这个类的作用)用户级别信息表
 * @date 2021-08-05
 */


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("customer_level_inf")
public class CustomerLevelInfEntity extends Model<CustomerLevelInfEntity> implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 会员级别ID
     */
    @TableId(value = "customer_level", type = IdType.AUTO)
    private Integer customerLevel;


    /**
     * 会员级别名称
     */
    @TableField("level_name")
    private String levelName;


    /**
     * 该级别最低积分
     */
    @TableField("min_point")
    private Integer minPoint;


    /**
     * 该级别最高积分
     */
    @TableField("max_point")
    private Integer maxPoint;


    /**
     * 最后修改时间
     */
    @TableField(value = "modified_time",fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date modifiedTime;


}