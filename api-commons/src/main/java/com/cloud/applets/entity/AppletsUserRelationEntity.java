package com.cloud.applets.entity;

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
 * @ClassName: AppletsUserRelation
 * @Description: 小程序系统用户关联
 * @date 2021-11-13
 */


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_applets_user_relation")
@ApiModel("  小程序系统用户关联")
public class AppletsUserRelationEntity extends Model<AppletsUserRelationEntity> implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 关联键
     */
    @TableId("link_user_id")
    @ApiModelProperty(value = "关联键")
    private Long linkUserId;


    /**
     * 系统用户id
     */
    @TableField("user_id")
    @ApiModelProperty(value = "系统用户id")
    private Long userId;


    /**
     * 微信用户id
     */
    @TableField("wx_user_id")
    @ApiModelProperty(value = "微信用户id")
    private Long wxUserId;


    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;


}