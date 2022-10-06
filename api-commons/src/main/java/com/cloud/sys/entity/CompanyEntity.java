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
 * @ClassName: Company
 * @Description: 公司维护
 * @date 2021-11-15
 */


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_company")
@ApiModel("公司维护")
public class CompanyEntity extends Model<CompanyEntity> implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 主键
     */
    @TableId("company_id")
    @ApiModelProperty(value = "主键")
    private Long companyId;


    /**
     * 公司名称
     */
    @TableField("company_name")
    @ApiModelProperty(value = "公司名称")
    private String companyName;


    /**
     * 公司简介
     */
    @TableField("company_info")
    @ApiModelProperty(value = "公司简介")
    private String companyInfo;


    /**
     * 新增时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "insert_time", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "新增时间")
    private Date insertTime;


    /**
     * 操作人
     */
    @TableField(value = "update_uid", fill = FieldFill.UPDATE)
    @ApiModelProperty(value = "操作人")
    private Long updateUid;


    /**
     * 修改人
     */
    @TableField(value = "insert_uid", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "修改人")
    private Long insertUid;


    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;


}