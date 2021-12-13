package com.cloud.order.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author wjg
 * @ClassName: WarehouseInfoEntity
 * @Description: TODO(这里用一句话描述这个类的作用)仓库信息表
 * @date 2021-08-05
 */


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("warehouse_info")
public class WarehouseInfoEntity extends Model<WarehouseInfoEntity> implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 仓库ID
     */
    @TableId(value = "w_id", type = IdType.AUTO)
    private Integer warehouseId;


    /**
     * 仓库编码
     */
    @TableField("warehouse_sn")
    private String warehouseSn;


    /**
     * 仓库名称
     */
    @TableField("warehoust_name")
    private String warehoustName;


    /**
     * 仓库电话
     */
    @TableField("warehouse_phone")
    private String warehousePhone;


    /**
     * 仓库联系人
     */
    @TableField("contact")
    private String contact;


    /**
     * 省
     */
    @TableField("province")
    private Integer province;


    /**
     * 市
     */
    @TableField("city")
    private Integer city;


    /**
     * 区
     */
    @TableField("distrct")
    private Integer distrct;


    /**
     * 仓库地址
     */
    @TableField("address")
    private String address;


    /**
     * 仓库状态：0禁用，1启用
     */
    @TableField("warehouse_status")
    private Integer warehouseStatus;


    /**
     * 最后修改时间
     */
    @TableField(value = "modified_time",fill = FieldFill.UPDATE)
    private Date modifiedTime;


}