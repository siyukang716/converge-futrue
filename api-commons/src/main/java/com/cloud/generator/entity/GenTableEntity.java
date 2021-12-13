package com.cloud.generator.entity;

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
 * @ClassName: GenTableEntity
 * @Description: 代码生成业务表
 * @date 2021-09-14
 */


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("gen_table")
@ApiModel("代码生成业务表")
public class GenTableEntity extends Model<GenTableEntity> implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 编号
     */
    @TableId(value = "table_id", type = IdType.AUTO)
    @ApiModelProperty(value = "编号")
    private Long tableId;


    /**
     * 表名称
     */
    @TableField("table_name")
    @ApiModelProperty(value = "表名称")
    private String tableName;


    /**
     * 表描述
     */
    @TableField("table_comment")
    @ApiModelProperty(value = "表描述")
    private String tableComment;


    /**
     * 关联子表的表名
     */
    @TableField("sub_table_name")
    @ApiModelProperty(value = "关联子表的表名")
    private String subTableName;


    /**
     * 子表关联的外键名
     */
    @TableField("sub_table_fk_name")
    @ApiModelProperty(value = "子表关联的外键名")
    private String subTableFkName;


    /**
     * 实体类名称
     */
    @TableField("class_name")
    @ApiModelProperty(value = "实体类名称")
    private String className;


    /**
     * 使用的模板（crud单表操作 tree树表操作 sub主子表操作）
     */
    @TableField("tpl_category")
    @ApiModelProperty(value = "使用的模板（crud单表操作 tree树表操作 sub主子表操作）")
    private String tplCategory;


    /**
     * 生成包路径
     */
    @TableField("package_name")
    @ApiModelProperty(value = "生成包路径")
    private String packageName;


    /**
     * 生成模块名
     */
    @TableField("module_name")
    @ApiModelProperty(value = "生成模块名")
    private String moduleName;


    /**
     * 生成业务名
     */
    @TableField("business_name")
    @ApiModelProperty(value = "生成业务名")
    private String businessName;


    /**
     * 生成功能名
     */
    @TableField("function_name")
    @ApiModelProperty(value = "生成功能名")
    private String functionName;


    /**
     * 生成功能作者
     */
    @TableField("function_author")
    @ApiModelProperty(value = "生成功能作者")
    private String functionAuthor;


    /**
     * 生成代码方式（0zip压缩包 1自定义路径）
     */
    @TableField("gen_type")
    @ApiModelProperty(value = "生成代码方式（0zip压缩包 1自定义路径）")
    private String genType;


    /**
     * 生成路径（不填默认项目路径）
     */
    @TableField("gen_path")
    @ApiModelProperty(value = "生成路径（不填默认项目路径）")
    private String genPath;


    /**
     * 其它生成选项
     */
    @TableField("options")
    @ApiModelProperty(value = "其它生成选项")
    private String options;


    /**
     * 创建者
     */
    @TableField("create_by")
    @ApiModelProperty(value = "创建者")
    private String createBy;


    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date createTime;


    /**
     * 更新者
     */
    @TableField("update_by")
    @ApiModelProperty(value = "更新者")
    private String updateBy;


    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date updateTime;


    /**
     * 备注
     */
    @TableField("remark")
    @ApiModelProperty(value = "备注")
    private String remark;


}