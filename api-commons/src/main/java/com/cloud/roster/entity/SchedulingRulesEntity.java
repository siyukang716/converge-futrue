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
 * @ClassName: SchedulingRules
 * @Description: 排班规则
 * @date 2021-10-27
 */


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("roster_scheduling_rules")
@ApiModel("排班规则")
public class SchedulingRulesEntity extends Model<SchedulingRulesEntity> implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 排班规则id
     */
    @TableId("arrange_rule_id")
    @ApiModelProperty(value = "排班规则id")
    private Long arrangeRuleId;


    /**
     * 规则名称
     */
    @TableField("arrange_rule_name")
    @ApiModelProperty(value = "规则名称")
    private String arrangeRuleName;


    /**
     * 排班类型
     */
    @TableField("arrange_rule_type")
    @ApiModelProperty(value = "排班类型")
    private String arrangeRuleType;


    /**
     * 上班时间
     */
    @TableField("work_start_time")
    @ApiModelProperty(value = "上班时间")
    private String workStartTime;


    /**
     * 工时
     */
    @TableField("working_hours")
    @ApiModelProperty(value = "工时")
    private Integer workingHours;
    /**
     * 是否排班 1否  2 是
     */
    @TableField("is_roster")
    @ApiModelProperty(value = "是否排班")
    private Integer isRoster;


    /**
     * 下班时间
     */
    @TableField("work_end_time")
    @ApiModelProperty(value = "下班时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date workEndTime;


    /**
     * 发布状态 1未发布  2 发布
     */
    @TableField("post_status")
    @ApiModelProperty(value = "发布状态 1未发布  2 发布")
    private Integer postStatus;


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


}