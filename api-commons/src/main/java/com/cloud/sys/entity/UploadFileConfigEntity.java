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
 * @ClassName: UploadFileConfig
 * @Description: 文件上传路径配置
 * @date 2021-09-23
 */


@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "upload_file_config")
@ApiModel("文件上传路径配置")
public class UploadFileConfigEntity extends Model<UploadFileConfigEntity> implements Serializable {

    private static final long serialVersionUID = 1L;


    @TableId("config_id")
    @ApiModelProperty(value = "")
    private Long configId;



    /**
     * 访问路径
     */
    @TableField("windows_call")
    @ApiModelProperty(value = "访问路径")
    private String windowsCall;


    /**
     * window上传路径
     */
    @TableField("windows_path")
    @ApiModelProperty(value = "window上传路径")
    private String windowsPath;


    /**
     * 访问路径
     */
    @TableField("linux_call")
    @ApiModelProperty(value = "访问路径")
    private String linuxCall;


    /**
     * linux上传路径
     */
    @TableField("linux_path")
    @ApiModelProperty(value = "linux上传路径")
    private String linuxPath;


    /**
     * 上传业务key
     */
    @TableField("business_name")
    @ApiModelProperty(value = "业务描述")
    private String businessName;
/**
     * 上传业务key
     */
    @TableField("business_type")
    @ApiModelProperty(value = "上传业务key")
    private String businessType;


    @TableField(value = "insert_time", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date insertTime;


    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date updateTime;


}