package com.cloud.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author
 * @ClassName: UploadFile
 * @Description: 断点续传
 * @date 2021-09-18
 */


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("upload_file")
@ApiModel("断点续传")
public class UploadFileEntity extends Model<UploadFileEntity> implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * up_file_id
     */
    @TableId(value = "up_file_id", type = IdType.AUTO)
    @ApiModelProperty(value = "up_file_id")
    private Long upFileId;


    /**
     * 相对路径
     */
    @TableField("path")
    @ApiModelProperty(value = "相对路径")
    private String path;


    /**
     * 文件名
     */
    @TableField("file_name")
    @ApiModelProperty(value = "文件名")
    private String fileName;


    /**
     * 文件后缀
     */
    @TableField("suffix")
    @ApiModelProperty(value = "文件后缀")
    private String suffix;


    /**
     * 文件大小|字节B
     */
    @TableField("size")
    @ApiModelProperty(value = "文件大小|字节B")
    private Long size;


    /**
     * 文件创建时间
     */
    @TableField("created_at")
    @ApiModelProperty(value = "文件创建时间")
    private Long createdAt;


    /**
     * 文件修改时间
     */
    @TableField("updated_at")
    @ApiModelProperty(value = "文件修改时间")
    private Long updatedAt;


    /**
     * 已上传分片
     */
    @TableField("shard_index")
    @ApiModelProperty(value = "已上传分片")
    private Long shardIndex;


    /**
     * 分片大小|B
     */
    @TableField("shard_size")
    @ApiModelProperty(value = "分片大小|B")
    private Long shardSize;


    /**
     * 分片总数
     */
    @TableField("shard_total")
    @ApiModelProperty(value = "分片总数")
    private Long shardTotal;


    /**
     * 文件标识
     */
    @TableField("file_key")
    @ApiModelProperty(value = "文件标识")
    private String fileKey;


}