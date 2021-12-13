package com.cloud.util;


import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

/**
 * permission 工具类
 *
 * @author
 */
@Slf4j
public class PermissionUtils {

    /**
     * 返回用户属性值
     *
     * @param property 属性名称
     * @return 用户属性值
     */
    public static Object getPrincipalProperty(String property) {
        Subject subject = SecurityUtils.getSubject();
        if (subject != null) {
            Object principal = subject.getPrincipal();
            try {
                BeanInfo bi = Introspector.getBeanInfo(principal.getClass());
                for (PropertyDescriptor pd : bi.getPropertyDescriptors()) {
                    if (pd.getName().equals(property) == true) {
                        return pd.getReadMethod().invoke(principal, (Object[]) null);
                    }
                }
            } catch (Exception e) {
                log.error("Error reading property [{}] from principal of type [{}]", property,
                        principal.getClass().getName());
            }
        }
        return null;
    }
}
