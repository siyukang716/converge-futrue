package com.cloud.sys;

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
 * @author
 * @ClassName: SysDept
 * @Description: 部门
 * @date 2021-09-20
 */


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_dept")
@ApiModel("部门")
public class SysDeptEntity extends Model<SysDeptEntity> implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 部门id
     */
    @TableId("dept_id")
    @ApiModelProperty(value = "部门id")
    private Long deptId;


    /**
     * 部门名称
     */
    @TableField("dept_name")
    @ApiModelProperty(value = "部门名称")
    private String deptName;


    /**
     * 上级部门
     */
    @TableField("parent_dept_id")
    @ApiModelProperty(value = "上级部门")
    private Long parentDeptId;
    /**
     * 公司主键
     */
    @TableField("company_id")
    @ApiModelProperty(value = "公司主键")
    private Long companyId;

    /**
     * 电话
     */
    @TableField("tel")
    @ApiModelProperty(value = "电话")
    private String tel;

    /**
     * 编码
     */
    @TableField("coding")
    @ApiModelProperty(value = "编码")
    private String coding;


    /**
     * 删除标识
     */
    @TableField(value = "delflag",fill = FieldFill.INSERT)
    @ApiModelProperty(value = "删除标识")
    private Integer delflag = 1;


    /**
     * 新增时间
     */
    @TableField(value = "insert_time", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "新增时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date insertTime;


    /**
     * 修改时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date updateTime;


}