package com.cloud.sys;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 角色表
 */
@Data
@TableName("role")
public class RoleEntity extends Model<RoleEntity>{
    @TableId
    private Long id;
    private Long pid;
    /*角色名称*/
    private String roleName;
    /*角色描述*/
    private String descpt;
    /*角色编号*/
    private String code;
    /*操作用户id*/
    private Long insertUid;
    /*添加时间*/
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date insertTime;
    /*更新时间*/
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date updateTime;
    /*是否删除（0：正常；1：已删）*/
    @TableLogic
    @TableField(select = false,fill = FieldFill.INSERT)
    private Integer isDel;
    /**
     * 公司主键
     */
    @TableField("company_id")
    @ApiModelProperty(value = "公司主键")
    private Long companyId;

}
