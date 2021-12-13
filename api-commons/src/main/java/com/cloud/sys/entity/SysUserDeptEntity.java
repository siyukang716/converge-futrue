package com.cloud.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author 小可爱
 * @ClassName: SysUserDept
 * @Description: 部门用户关联
 * @date 2021-10-11
 */


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user_dept")
@ApiModel("部门用户关联")
public class SysUserDeptEntity extends Model<SysUserDeptEntity> implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 关联主键
     */
    @TableId("link_id")
    @ApiModelProperty(value = "关联主键")
    private Long linkId;


    /**
     * 部门id
     */
    @TableField("dept_id")
    @ApiModelProperty(value = "部门id")
    private Long deptId;


    /**
     * 用户id
     */
    @TableField("user_id")
    @ApiModelProperty(value = "用户id")
    private Long userId;

    public SysUserDeptEntity() {
    }

    public SysUserDeptEntity(Long linkId, Long deptId, Long userId) {
        this.linkId = linkId;
        this.deptId = deptId;
        this.userId = userId;
    }
}