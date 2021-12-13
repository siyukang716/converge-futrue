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
 * @ClassName: CompPerm
 * @Description: 公司权限
 * @date 2021-11-16
 */


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_comp_perm")
@ApiModel("公司权限")
public class CompPermEntity extends Model<CompPermEntity> implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 公司权限id
     */
    @TableId("comp_perm_id")
    @ApiModelProperty(value = "公司权限id")
    private Long compPermId;


    /**
     * 公司id
     */
    @TableField("company_id")
    @ApiModelProperty(value = "公司id")
    private Long companyId;


    /**
     * 菜单id
     */
    @TableField("perm_id")
    @ApiModelProperty(value = "菜单id")
    private Long permId;


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