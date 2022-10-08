package com.cloud.sys;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * 用户表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user")
@ApiModel("用户表")
@NoArgsConstructor //无参构造参数
@AllArgsConstructor //全参构造参数
public class UserEntity extends Model<UserEntity> implements Serializable {


    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "")
    private Long id;

    /**
     * 用户名
     */
    @TableField("username")
    @ApiModelProperty(value = "用户名")
    private String username;

    /**
     * 邮箱
     */
    @TableField("email")
    @ApiModelProperty(value = "邮箱")
    private String email;

    /**
     * 手机号
     */
    @TableField("mobile")
    @ApiModelProperty(value = "手机号")
    private String mobile;

    /**
     * 密码
     */
    @TableField("password")
    @ApiModelProperty(value = "密码")
    private String password;

    /**
     * 提成率
     */
    @TableField("commission_rate")
    @ApiModelProperty(value = "提成率")
    private Long commissionRate;


    /**
     * 用户真实姓名
     */
    @TableField("customer_name")
    @ApiModelProperty(value = "用户真实姓名")
    private String customerName;


    /**
     * 证件类型：1 身份证，2 军官证，3 护照
     */
    @TableField("identity_card_type")
    @ApiModelProperty(value = "证件类型：1 身份证，2 军官证，3 护照")
    private Long identityCardType;


    /**
     * 证件号码
     */
    @TableField("identity_card_no")
    @ApiModelProperty(value = "证件号码")
    private String identityCardNo;


    /**
     * 性别
     */
    @TableField("gender")
    @ApiModelProperty(value = "性别")
    private String gender;


    /**
     * 生日
     */
    @TableField("birthday")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "生日")
    private Date birthday;


    /**
     * 年龄
     */
    @TableField("age")
    @ApiModelProperty(value = "年龄")
    private Integer age;


    /**
     * 职位
     */
    @TableField("position")
    @ApiModelProperty(value = "职位")
    private String position;


    /**
     * 入职时间
     */
    @TableField("entry_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "入职时间")
    private Date entryTime;


    /**
     * 公司id
     */
    @TableField("company_id")
    @ApiModelProperty(value = "公司id")
    private Long companyId;

    /**
     * 创建人ID
     */
    @TableField(value = "insert_uid", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建人ID")
    private Long insertUid;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "insert_time", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private Date insertTime;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;


    /**
     * 是否删除（0：正常；1：已删）
     */
    @TableField(value = "is_del", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "是否删除（0：正常；1：已删）")
    private Integer isDel;


    /**
     * 是否在职（0：正常；1，离职）
     */
    @TableField("is_job")
    @ApiModelProperty(value = "是否在职（0：正常；1，离职）")
    private Integer isJob;


    /**
     * 短信验证码
     */
    @TableField("mcode")
    @ApiModelProperty(value = "短信验证码")
    private String mcode;


    /**
     * 短信发送时间
     */
    @TableField("send_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "短信发送时间")
    private Date sendTime;


    /**
     * 更新版本
     */
    @TableField("version")
    @ApiModelProperty(value = "更新版本")
    private Integer version;


    //    @TableField(exist = false)
//    private List<UserRoleEntity> userRoles;
    //@NotNull(message = "图片验证码不能为空，请您先填写验证码")
    //@MatchPattern(pattern = "\\w{4}$", message = "图片验证码有误，请您重新填写")
    @TableField(exist = false)
    private String code;

    // @ValidateWithMethod(methodName = "isValidateSmsCode", message = "验证码有误，请您重新填写", parameterType = String.class)
    @TableField(exist = false)
    private String smsCode;
    @TableField(exist = false)
    private String roleName;

    /**
     * 部门id
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "部门id")
    private Long deptId;

    private static final Pattern CODE = Pattern.compile("[0-9]{6}$");

    /**
     * 是否为6位短信验证码
     *
     * @param smsCode
     * @return
     */
    private boolean isValidateSmsCode(String smsCode) {
        if (StringUtils.isEmpty(smsCode)) {
            if (!CODE.matcher(smsCode).matches()) {
                return false;
            }
        }
        return true;
    }

    public UserEntity(Long id, String username, String mobile, String email, String password, Long insertUid) {
        this.id = id;
        this.username = username;
        this.mobile = mobile;
        this.email = email;
        this.password = password;
        this.insertUid = insertUid;
    }


    /**
     * 相似度
     */
    @TableField(exist = false)
    private Integer similarValue;
}
