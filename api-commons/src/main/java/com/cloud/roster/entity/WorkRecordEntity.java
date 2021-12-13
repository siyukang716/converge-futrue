package com.cloud.roster.entity;

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
 * @ClassName: WorkRecord
 * @Description: 排班记录
 * @date 2021-10-27
 */


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("roster_work_record")
@ApiModel("排班记录")
public class WorkRecordEntity extends Model<WorkRecordEntity> implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 主键
     */
    @TableId("work_id")
    @ApiModelProperty(value = "主键")
    private Long workId;


    /**
     * 用户id
     */
    @TableField("user_id")
    @ApiModelProperty(value = "用户id")
    private Long userId;

    /**
     * 用户id
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "用户名称")
    private String userName;


    /**
     * 排班规则id
     */
    @TableField("arrange_rule_id")
    @ApiModelProperty(value = "排班规则id")
    private Long arrangeRuleId;


    /**
     * 上班时间
     */
    @TableField("work_s_time")
    @ApiModelProperty(value = "上班时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date workSTime;


    /**
     * 下班时间
     */
    @TableField("work_e_time")
    @ApiModelProperty(value = "下班时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date workETime;


    /**
     * 工作日
     */
    @TableField("work_date")
    @ApiModelProperty(value = "工作日")
    @JsonFormat(pattern = "yyyy-MM-dd" ,timezone = "GMT+8")
    private Date workDate;


    /**
     * 工作状态  1正常班/ 3代班/2请假
     */
    @TableField("work_type")
    @ApiModelProperty(value = "工作状态  1正常班/ 3代班/2请假")
    private Integer workType;


    /**
     * 补班id workid
     */
    @TableField("make_up_id")
    @ApiModelProperty(value = "补班id workid")
    private Long makeUpId;


    /**
     * 补班人员
     */
    @TableField("make_up_user")
    @ApiModelProperty(value = "补班人员")
    private Long makeUpUser;


    /**
     * 新增时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;


    /**
     * 删除标识
     */
    @TableField(value = "is_del", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "删除标识")
    private Integer isDel;


}