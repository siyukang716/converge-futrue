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
 * @ClassName: WorkBreak
 * @Description: 工作休息规则
 * @date 2021-10-27
 */


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("roster_work_break")
@ApiModel("工作休息规则")
public class WorkBreakEntity extends Model<WorkBreakEntity> implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 上班明细id
     */
    @TableId("work_details_id")
    @ApiModelProperty(value = "上班明细id")
    private Long workDetailsId;


    /**
     * 上班规则id
     */
    @TableField("arrange_rule_id")
    @ApiModelProperty(value = "上班规则id")
    private Long arrangeRuleId;


    /**
     * 开始时间
     */
    @TableField("start_time")
    @ApiModelProperty(value = "开始时间")
    private String startTime;


    /**
     * 截止时间
     */
    @TableField("end_time")
    @ApiModelProperty(value = "截止时间")
    private String endTime;


    /**
     * 休息时长
     */
    @TableField("rest_time")
    @ApiModelProperty(value = "休息时长")
    private Integer restTime;


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