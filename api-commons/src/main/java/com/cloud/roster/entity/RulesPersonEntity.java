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
 * @ClassName: RulesPerson
 * @Description: 排版人员规则关联
 * @date 2021-10-27
 */


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("roster_rules_person")
@ApiModel("排版人员规则关联")
public class RulesPersonEntity extends Model<RulesPersonEntity> implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 主键
     */
    @TableId("rules_person_id")
    @ApiModelProperty(value = "主键")
    private Long rulesPersonId;


    /**
     * 排班规则id
     */
    @TableField("arrange_rule_id")
    @ApiModelProperty(value = "排班规则id")
    private Long arrangeRuleId;


    /**
     * 人员id
     */
    @TableField("user_id")
    @ApiModelProperty(value = "人员id")
    private Long userId;
    /**
     * 人员名称
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "人员名称")
    private String userName;


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