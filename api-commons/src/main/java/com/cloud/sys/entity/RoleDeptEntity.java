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
 * @author 小可爱
 * @ClassName: RoleDept
 * @Description: 角色部门数据权限
 * @date 2021-11-18
 */


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_role_dept")
@ApiModel("角色部门数据权限")
public class RoleDeptEntity extends Model<RoleDeptEntity> implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 主键
     */
    @TableId("r_d_purview")
    @ApiModelProperty(value = "主键")
    private Long rDPurview;


    /**
     * 角色id
     */
    @TableField("role_id")
    @ApiModelProperty(value = "角色id")
    private Long roleId;


    /**
     * 部门id
     */
    @TableField("dept_id")
    @ApiModelProperty(value = "部门id")
    private Long deptId;


    /**
     * 业务类型
     */
    @TableField("business_type")
    @ApiModelProperty(value = "业务类型")
    private String businessType;


    /**
     * 新增时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "insert_time", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "新增时间")
    private Date insertTime;


    /**
     * 新增操作人
     */
    @TableField(value = "insert_uid", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "新增操作人")
    private Long insertUid;


    /**
     * 修改操作人
     */
    @TableField(value = "update_uid", fill = FieldFill.UPDATE)
    @ApiModelProperty(value = "修改操作人")
    private Long updateUid;


    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;


}