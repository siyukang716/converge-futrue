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
 * @ClassName: SysCommonDict
 * @Description: 公共字典
 * @date 2021-10-12
 */


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_common_dict")
@ApiModel("公共字典")
public class SysCommonDictEntity extends Model<SysCommonDictEntity> implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 字典主键
     */
    @TableId(value = "dict_id", type = IdType.AUTO)
    @ApiModelProperty(value = "字典主键")
    private Long dictId;


    /**
     * 字典名称
     */
    @TableField("dict_name")
    @ApiModelProperty(value = "字典名称")
    private String dictName;


    /**
     * 字典类型
     */
    @TableField("dict_type")
    @ApiModelProperty(value = "字典类型")
    private String dictType;


    /**
     * 备注
     */
    @TableField("dict_mome")
    @ApiModelProperty(value = "备注")
    private String dictMome;


    /**
     * 插入时间
     */
    @TableField(value = "insert_time", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "插入时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date insertTime;


    /**
     * 操作人
     */
    @TableField("insert_uid")
    @ApiModelProperty(value = "操作人")
    private Long insertUid;


    /**
     * 修改时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date updateTime;


}