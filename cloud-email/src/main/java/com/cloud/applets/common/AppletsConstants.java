package com.cloud.applets.common;

/**
 * @Description: AppletsConstants  小程序常量
 * @Author lenovo
 * @Date: 2021/11/9 10:15
 * @Version 1.0
 */
public class AppletsConstants {

    private AppletsConstants() {

    }

    /**
     * AppSecret(小程序密钥)
     */
    public static final String secret = "8b3236263c2d3e6d045db2cc103ca139";
    /**
     * AppID(小程序ID)
     */
    public static final String appid = "wx26d94d69dea48cc0";

    /**
     * auth.code2Session  微信登录接口
     * 属性	类型	默认值	必填	说明
     * appid	string		是	小程序 appId
     * secret	string		是	小程序 appSecret
     * js_code	string		是	登录时获取的 code
     * grant_type	string		是	授权类型，此处只需填写 authorization_code
     * 返回值
     * Object
     * 返回的 JSON 数据包
     * 属性	类型	说明
     * openid	string	用户唯一标识
     * session_key	string	会话密钥
     * unionid	string	用户在开放平台的唯一标识符，若当前小程序已绑定到微信开放平台帐号下会返回，详见 UnionID 机制说明。
     * errcode	number	错误码
     * errmsg	string	错误信息
     */
    public static final String code2Session = "https://api.weixin.qq.com/sns/jscode2session?appid={}&secret={}&js_code={}&grant_type=authorization_code";//"https://api.weixin.qq.com/sns/jscode2session";


    /**
     * 接口调用凭证
     * 请求参数
     * 属性	类型	默认值	必填	说明
     * grant_type	string		是	填写 client_credential
     * appid	string		是	小程序唯一凭证，即 AppID，可在「微信公众平台 - 设置 - 开发设置」页中获得。（需要已经成为开发者，且帐号没有异常状态）
     * secret	string		是	小程序唯一凭证密钥，即 AppSecret，获取方式同 appid
     * 返回值
     * Object
     * 返回的 JSON 数据包
     *
     * 属性	类型	说明
     * access_token	string	获取到的凭证
     * expires_in	number	凭证有效时间，单位：秒。目前是7200秒之内的值。
     * errcode	number	错误码
     * errmsg	string	错误信息
     */
    public static final String getAccessToken = "https://api.weixin.qq.com/cgi-bin/token";



}
