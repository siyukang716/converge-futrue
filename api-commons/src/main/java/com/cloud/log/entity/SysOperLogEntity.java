package com.cloud.log.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 豆芽菜
 * @ClassName: SysOperLog
 * @Description: 操作日志记录
 * @date 2021-11-09
 */


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_oper_log")
@ApiModel("操作日志记录")
public class SysOperLogEntity extends Model<SysOperLogEntity> implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 日志主键
     */
    @TableId("oper_id")
    @ApiModelProperty(value = "日志主键")
    private Long operId;


    /**
     * 模块标题
     */
    @TableField("title")
    @ApiModelProperty(value = "模块标题")
    private String title;


    /**
     * 业务类型（0其它 1新增 2修改 3删除）
     */
    @TableField("business_type")
    @ApiModelProperty(value = "业务类型（0其它 1新增 2修改 3删除）")
    private Integer businessType;


    /**
     * 方法名称
     */
    @TableField("method")
    @ApiModelProperty(value = "方法名称")
    private String method;


    /**
     * 请求方式
     */
    @TableField("request_method")
    @ApiModelProperty(value = "请求方式")
    private String requestMethod;


    /**
     * 操作类别（0其它 1后台用户 2手机端用户）
     */
    @TableField("operator_type")
    @ApiModelProperty(value = "操作类别（0其它 1后台用户 2手机端用户）")
    private Integer operatorType;


    /**
     * 操作人员
     */
    @TableField("oper_name")
    @ApiModelProperty(value = "操作人员")
    private String operName;


    /**
     * 部门名称
     */
    @TableField("dept_name")
    @ApiModelProperty(value = "部门名称")
    private String deptName;


    /**
     * 请求URL
     */
    @TableField("oper_url")
    @ApiModelProperty(value = "请求URL")
    private String operUrl;


    /**
     * 主机地址
     */
    @TableField("oper_ip")
    @ApiModelProperty(value = "主机地址")
    private String operIp;


    /**
     * 操作地点
     */
    @TableField("oper_location")
    @ApiModelProperty(value = "操作地点")
    private String operLocation;


    /**
     * 请求参数
     */
    @TableField("oper_param")
    @ApiModelProperty(value = "请求参数")
    private String operParam;


    /**
     * 返回参数
     */
    @TableField("json_result")
    @ApiModelProperty(value = "返回参数")
    private String jsonResult;


    /**
     * 操作状态（0正常 1异常）
     */
    @TableField("status")
    @ApiModelProperty(value = "操作状态（0正常 1异常）")
    private Integer status;


    /**
     * 错误消息
     */
    @TableField("error_msg")
    @ApiModelProperty(value = "错误消息")
    private String errorMsg;


    /**
     * 操作时间
     */
    @TableField("oper_time")
    @ApiModelProperty(value = "操作时间")
    private Date operTime;


}