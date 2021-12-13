package com.cloud.service.weixin;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.thread.AccessTokenThread;
import com.cloud.mapper.wx.WX_UserMapper;
import com.cloud.util.WeixinUtil;
import com.spring.commons.entity.weixin.entity.W_UserEntity;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WX_UserService extends ServiceImpl<WX_UserMapper,W_UserEntity> {
    /**
     * 获取用户列表
     * http请求方式: GET（请使用https协议）
     */
    public static String user_gets_url = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN&next_openid=";

    public static String user_getOne_url="https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";

    /**
     * 获取用户列表
     */
    public void getPageList(){
        String at = AccessTokenThread.accessToken.getToken();
        // 拼装创建菜单的url
        String url = user_gets_url.replace("ACCESS_TOKEN", at);
        // 调用接口查询菜单
        JSONObject jsonObject = WeixinUtil.httpRequest(url, "GET", null);
        log.info(jsonObject.toString());
    }

    /**
     * 获取用户信息
     */
    public W_UserEntity getUser(String openid){
        String at = AccessTokenThread.accessToken.getToken();
        // 拼装创建菜单的url
        String url = user_getOne_url.replace("ACCESS_TOKEN", at).replace("OPENID",openid);
        // 调用接口查询菜单
        JSONObject jsonObject = WeixinUtil.httpRequest(url, "GET", null);
        W_UserEntity w_userEntity = JSONObject.parseObject(jsonObject.toString(), W_UserEntity.class);
        log.info(w_userEntity.toString());
        return w_userEntity;
    }
}
