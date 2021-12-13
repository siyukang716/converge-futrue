package com.cloud.encry;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.DES;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

/**
 * 请求参数 解密操作
 *
 * @Author: Java碎碎念
 * @Date: 2019/10/24 21:31
 *
 */
@Component
@ControllerAdvice(basePackages = "com.cloud.springbootencry.web")
@Slf4j
public class DecryptRequestBodyAdvice implements RequestBodyAdvice {


    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> selectedConverterType) throws IOException {
        return inputMessage;
    }

    @Override
    public Object afterBodyRead(Object obj, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        String dealData = null;
        try {
            log.info("==================================解密操作===================================");
            //解密操作
            String srcData = JSON.toJSONString(obj);
            DES des = SecureUtil.des("哈哈哈哈哈w".getBytes(StandardCharsets.UTF_8));
            //解密为原字符串
            String decryptStr = des.decryptStr("14cd73f43f3927ccdadab0d508cc4a0c");
            dealData = decryptStr;//DesUtil.decrypt(srcData);
            log.info("原始数据={},解密后数据={}", srcData, dealData);
        } catch (Exception e) {
            log.error("异常！", e);
        }
        return dealData;
    }


    @Override
    public Object handleEmptyBody(@Nullable Object var1, HttpInputMessage var2, MethodParameter var3, Type var4, Class<? extends HttpMessageConverter<?>> var5) {
        log.info("3333");
        return var1;
    }


}