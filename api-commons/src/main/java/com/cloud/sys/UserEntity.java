package com.cloud.sys;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
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
@TableName(value = "user")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity extends Model<UserEntity> implements Serializable {
    @TableId
    private  Long id;
    /**
     * 用户名
     */
    //@NotNull(message = "用户名不能为空，请您先填写用户名")

    private String username;
    /**
     *手机号
     */
    private String mobile;
    private String email;
    private String password;
    /*添加该用户的用户id*/
    private Long insertUid;
    /*是否删除（0：正常；1：已删）*/
    @TableLogic
    @TableField(select = false,fill = FieldFill.INSERT)
    private Integer isDel;
    /*是否在职（0：正常；1，离职）*/
    private Integer isJob;
    /*短信验证码*/
    private String mcode;
    /*短信发送时间*/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date sendTime;
    /*更新版本*/
    @Version
    private Integer version;
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date insertTime;
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date updateTime;
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
     *	是否为6位短信验证码
     * @param smsCode
     * @return
     */
    private boolean isValidateSmsCode(String smsCode){
        if(StringUtils.isEmpty(smsCode)){
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

    /**
     * 公司主键
     */
    @TableField(value = "company_id",fill = FieldFill.INSERT)
    @ApiModelProperty(value = "公司主键")
    private Long companyId;
}
