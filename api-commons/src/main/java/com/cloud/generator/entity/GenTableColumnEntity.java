package com.cloud.generator.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.cloud.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wjg
 * @ClassName: GenTableColumnEntity
 * @Description: 代码生成业务表字段
 * @date 2021-09-14
 */


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("gen_table_column")
@ApiModel("代码生成业务表字段")
public class GenTableColumnEntity extends Model<GenTableColumnEntity> implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 编号
     */
    @TableId(value = "column_id", type = IdType.AUTO)
    @ApiModelProperty(value = "编号")
    private Long columnId;


    /**
     * 归属表编号
     */
    @TableField("table_id")
    @ApiModelProperty(value = "归属表编号")
    private String tableId;


    /**
     * 列名称
     */
    @TableField("column_name")
    @ApiModelProperty(value = "列名称")
    private String columnName;


    /**
     * 列描述
     */
    @TableField("column_comment")
    @ApiModelProperty(value = "列描述")
    private String columnComment;


    /**
     * 列类型
     */
    @TableField("column_type")
    @ApiModelProperty(value = "列类型")
    private String columnType;


    /**
     * JAVA类型
     */
    @TableField("java_type")
    @ApiModelProperty(value = "JAVA类型")
    private String javaType;


    /**
     * JAVA字段名
     */
    @TableField("java_field")
    @ApiModelProperty(value = "JAVA字段名")
    private String javaField;


    /**
     * 是否主键（1是）
     */
    @TableField("is_pk")
    @ApiModelProperty(value = "是否主键（1是）")
    private String isPk;


    /**
     * 是否自增（1是）
     */
    @TableField("is_increment")
    @ApiModelProperty(value = "是否自增（1是）")
    private String isIncrement;


    /**
     * 是否必填（1是）
     */
    @TableField("is_required")
    @ApiModelProperty(value = "是否必填（1是）")
    private String isRequired;


    /**
     * 是否为插入字段（1是）
     */
    @TableField("is_insert")
    @ApiModelProperty(value = "是否为插入字段（1是）")
    private String isInsert;


    /**
     * 是否编辑字段（1是）
     */
    @TableField("is_edit")
    @ApiModelProperty(value = "是否编辑字段（1是）")
    private String isEdit;


    /**
     * 是否列表字段（1是）
     */
    @TableField("is_list")
    @ApiModelProperty(value = "是否列表字段（1是）")
    private String isList;


    /**
     * 是否查询字段（1是）
     */
    @TableField("is_query")
    @ApiModelProperty(value = "是否查询字段（1是）")
    private String isQuery;


    /**
     * 查询方式（等于、不等于、大于、小于、范围）
     */
    @TableField("query_type")
    @ApiModelProperty(value = "查询方式（等于、不等于、大于、小于、范围）")
    private String queryType;


    /**
     * 显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）
     */
    @TableField("html_type")
    @ApiModelProperty(value = "显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）")
    private String htmlType;


    /**
     * 字典类型
     */
    @TableField("dict_type")
    @ApiModelProperty(value = "字典类型")
    private String dictType;


    /**
     * 排序
     */
    @TableField("sort")
    @ApiModelProperty(value = "排序")
    private Integer sort;


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

    public boolean isPk()
    {
        return isPk(this.isPk);
    }

    public boolean isPk(String isPk)
    {
        return isPk != null && StringUtils.equals("1", isPk);
    }
}