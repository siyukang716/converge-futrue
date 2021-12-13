package com.cloud.sys.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 豆芽菜
 * @ClassName: IpConfig
 * @Description: 系统ip配置
 * @date 2021-11-21
 */


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_ip_config")
@ApiModel("系统ip配置")
public class IpConfigEntity extends Model<IpConfigEntity> implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * ip地址主键ID
     */
    @TableId("ip_id")
    @ApiModelProperty(value = "ip地址主键ID")
    private Long ipId;


    /**
     * ip地址
     */
    @TableField("ip_address")
    @ApiModelProperty(value = "ip地址")
    private String ipAddress;


    /**
     * 操作人员
     */
    @TableField(value = "insert_uid", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "操作人员")
    private Long insertUid;


    /**
     * 新增时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "insert_time", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "新增时间")
    private Date insertTime;


    /**
     * 修改人员
     */
    @TableField(value = "update_uid", fill = FieldFill.UPDATE)
    @ApiModelProperty(value = "修改人员")
    private Long updateUid;


    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;


}