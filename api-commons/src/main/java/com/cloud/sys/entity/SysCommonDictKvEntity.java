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
 * @author 豆芽菜
 * @ClassName: SysCommonDictKv
 * @Description: 字典管理
 * @date 2021-10-12
 */


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_common_dict_kv")
@ApiModel("字典管理")
public class SysCommonDictKvEntity extends Model<SysCommonDictKvEntity> implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 主键
     */
    @TableId(value = "dict_child_id", type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Long dictChildId;


    /**
     * 字典编码
     */
    @TableField("dict_id")
    @ApiModelProperty(value = "字典编码")
    private String dictId;



    /**
     * 字典名称
     */
    @TableField("dict_code")
    @ApiModelProperty(value = "字典编码")
    private String dictCode;


    /**
     * 字典名称
     */
    @TableField("dict_tag")
    @ApiModelProperty(value = "字典名称")
    private String dictTag;


    /**
     * 排序
     */
    @TableField("dict_sort")
    @ApiModelProperty(value = "排序")
    private Long dictSort;


    /**
     * 备注
     */
    @TableField("dict_memo")
    @ApiModelProperty(value = "备注")
    private String dictMemo;


    /**
     * 状态
     */
    @TableField("dict_status")
    @ApiModelProperty(value = "状态")
    private String dictStatus;


    /**
     * 插入时间
     */
    @TableField(value = "insert_time", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "插入时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date insertTime;


    /**
     * 修改时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date updateTime;


    /**
     * 删除标识
     */
    @TableField("is_del")
    @ApiModelProperty(value = "删除标识")
    private Integer isDel;



    /**---------------传参------------------*/
    /**
     * 确定人ID
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "确定人")
    private String confirmPerson;
    /**
     * 确定人名称
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "确定人名称")
    private String confirmPersonName;

}