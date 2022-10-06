package com.cloud.common.entity;

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
 * @ClassName: SysPubFile
 * @Description: 公共文件
 * @date 2021-10-13
 */


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_pub_file")
@ApiModel("公共文件")
public class SysPubFileEntity extends Model<SysPubFileEntity> implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 文件编号
     */
    @TableId("upfile_id")
    @ApiModelProperty(value = "文件编号")
    private Long upfileId;


    /**
     * 文件类型
     */
    @TableField("upfile_type")
    @ApiModelProperty(value = "文件类型")
    private String upfileType;


    /**
     * 文件名称
     */
    @TableField("upfile_name")
    @ApiModelProperty(value = "文件名称")
    private String upfileName;


    /**
     * 文件地址
     */
    @TableField("upfile_address")
    @ApiModelProperty(value = "文件地址")
    private String upfileAddress;


    /**
     * 业务编号
     */
    @TableField("business_id")
    @ApiModelProperty(value = "业务编号")
    private Long businessId;


    /**
     * 业务名称
     */
    @TableField("business_name")
    @ApiModelProperty(value = "业务名称")
    private String businessName;


    /**
     * 业务关联id
     */
    @TableField("table_primarykey")
    @ApiModelProperty(value = "业务关联id")
    private String tablePrimarykey;


    /**
     * 业务表名称
     */
    @TableField("business_tablename")
    @ApiModelProperty(value = "业务表名称")
    private String businessTablename;


    /**
     * 业务文件类型
     */
    @TableField("business_file_type")
    @ApiModelProperty(value = "业务文件类型")
    private String businessFileType;


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
    @TableField(value ="is_del", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "删除标识")
    private Integer isDel;


}