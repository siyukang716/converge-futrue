package com.cloud.roster.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 小可爱
 * @ClassName: WorkRecordVo
 * @Description: 排班规则人员类
 * @date 2021-10-27
 */


@Data
@ApiModel("排班记录业务类")
public class WorkRecordVo{

    private static final long serialVersionUID = 1L;





    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Long userId;

    /**
     * 用户名称
     */
    @ApiModelProperty(value = "用户名称")
    private String userName;


    /**
     * 排班规则id
     */
    @ApiModelProperty(value = "排班规则id")
    private Long arrangeRuleId;


    /**
     * 上班时间
     */
    @ApiModelProperty(value = "上班时间")
    private String workStartTime;
    /**
     * 上班时间
     */
    @ApiModelProperty(value = "工时")
    private Integer workingHours;
}