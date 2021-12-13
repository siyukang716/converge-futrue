package com.cloud.shiro;

import com.cloud.sys.UserEntity;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

/**
 * @Description: ShiroUtils
 * @Author lenovo
 * @Date: 2021/8/16 17:38
 * @Version 1.0
 */
public class ShiroUtils {

    public static UserEntity getLoginUser() {
        return (UserEntity) SecurityUtils.getSubject().getPrincipal();
    }

    public static Long getLoginUserId() {
        return getLoginUser().getId();
    }

    public static String getLoginUserName() {
        return getLoginUser().getUsername();
    }


    public static Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    public static String getIp() {
        return getSubject().getSession().getHost();
    }
}
