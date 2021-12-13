package com.cloud.sys.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wjg
 * @ClassName: SysLogoinfoConfigEntity
 * @Description: 系统logo 配置
 * @date 2021-09-06
 */


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_logoinfo_config")
@ApiModel("系统logo 配置")
public class SysLogoinfoConfigEntity extends Model<SysLogoinfoConfigEntity> implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * id
     */
    @TableId(value = "sys_fonfig_id", type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private Long sysFonfigId;


    /**
     * 系统名称
     */
    @TableField("title")
    @ApiModelProperty(value = "系统名称")
    private String title;


    /**
     * 系统图标路径
     */
    @TableField("image")
    @ApiModelProperty(value = "系统图标路径")
    private String image;

    @TableField("href")
    @ApiModelProperty(value = "")
    private String href;


    @TableField(value = "add_time",fill = FieldFill.INSERT)
    @ApiModelProperty(value = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date addTime;


    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date updateTime;


}