package com.cloud.sys;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 功能菜单配置表
 */
@Data
@TableName("permission")
public class PermissionEntity extends Model<PermissionEntity> {
    @TableId
    private Long id;
    /*菜单名称*/
    private String name;
    /*父菜单id*/
    private Long pid;
    /*菜单排序*/
    private Integer zindex;
    /*权限分类（0 菜单；1 功能）*/
    private Integer istype;
    /*描述*/
    private String descpt;
    /*菜单编号*/
    private String code;
    /*菜单图标名称*/
    private String icon;
    /*菜单url*/
    private String page;
    /*添加时间*/
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date insertTime;
    /*更新时间*/
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date updateTime;
    /*删除标识字段*/
    @TableLogic
    @TableField(value = "is_del",select = false,fill = FieldFill.INSERT)
    private Integer isDel;

}
