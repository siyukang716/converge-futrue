package com.cloud.applets.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cloud.applets.common.AppletsConstants;
import com.cloud.applets.entity.AppletsUserEntity;
import com.cloud.applets.entity.AppletsUserRelationEntity;
import com.cloud.sys.UserEntity;
import com.cloud.sys.service.UserServiceImpl;
import com.cloud.util.IStatusMessage;
import com.cloud.util.Result;
import com.cloud.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: AppletsLoginService
 * @Author lenovo
 * @Date: 2021/11/12 15:21
 * @Version 1.0
 */

@Service
@Slf4j
public class AppletsLoginService {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private AppletsUserService appletsUserService;
    @Autowired
    private AppletsUserRelationService appletsUserRelationService;
    @Autowired
    private UserServiceImpl userService;


    /**
     * auth.code2Session  微信登录接口
     *
     * https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=authorization_code
     * 请求类型  get
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
    public JSONObject code2Session(String code){
        try {
            String url = StringUtils.format(AppletsConstants.code2Session,AppletsConstants.appid,AppletsConstants.secret,code);
            HttpEntity<String> jsonObject= restTemplate.getForEntity(url, String.class);

            log.info("返回：{}",jsonObject.getBody());
            return JSONObject.parseObject(jsonObject.getBody());
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return null;

    }

    /**
     * getAccessToken  接口调用凭证
     * 请求方式 get
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
    public JSONObject getAccessToken(){
        try {
            Map<String,Object> params = new HashMap();
            params.put("appid",AppletsConstants.appid);
            params.put("secret",AppletsConstants.secret);
            params.put("grant_type","authorization_code");
            HttpEntity<JSONObject> jsonObject= restTemplate.getForEntity(AppletsConstants.getAccessToken, JSONObject.class,params);

            log.info("返回：{}",jsonObject.getBody());
            return jsonObject.getBody();
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public Result appletsUservVerify(String openid,String unionid,String appId,String nickName,Integer gender
                    ,String language) {
        Result result = Result.getInstance();

       //String session_key = String.valueOf(app.get("session_key"));
        // 查询微信用户是否已注册后台系统
        AppletsUserEntity oneCustomize = appletsUserService.getOneCustomize(openid, unionid);

        if (null == oneCustomize){
            oneCustomize = new AppletsUserEntity();
            oneCustomize.setAppId(appId);
            oneCustomize.setOpenId(openid);
            //oneCustomize.setSessionKey(session_key);
            oneCustomize.setUnionId(unionid);
            oneCustomize.setNickName(nickName);
            oneCustomize.setGender(gender);
            oneCustomize.setLanguage(language);
            appletsUserService.saveOrUpdate(oneCustomize);
            /*result.setMessage("请关联系统用户后登录");
            result.setStatus(40028);
            return result;*/
        }
        AppletsUserRelationEntity link_app = appletsUserRelationService.getOneCustomize(oneCustomize.getWxUserId());
        if (null == link_app){
            result.setMessage("请关联系统用户后登录");
            result.setStatus(40028);
            return result;
        }
        // 1、 封装用户名、密码、是否记住我到token令牌对象 [支持记住我]
        AuthenticationToken token = new UsernamePasswordToken(link_app.getUserId()+"", "applets");
        //AuthenticationToken token = new UsernamePasswordToken(user.getMobile(), user.getPassword(),rememberMe);
        // 2、 Subject调用login
        Subject subject = SecurityUtils.getSubject();
        // 在调用了login方法后,SecurityManager会收到AuthenticationToken,并将其发送给已配置的Realm执行必须的认证检查
        // 每个Realm都能在必要时对提交的AuthenticationTokens作出反应
        // 所以这一步在调用login(token)方法时,它会走到MyRealm.doGetAuthenticationInfo()方法中,具体验证方式详见此方法
        log.debug("用户登录，用户验证开始！微信用户id=" + openid);
        subject.login(token);
        log.info("用户登录，用户验证通过！微信用户id=" + openid);
        result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
        result.setMessage("登录成功");

        return result;


    }
    /**
     * 微信注册接口
     * @param userName
     * @param password
     * @param openid
     * @param unionid
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Result register(String userName, String password, String openid,String unionid) {
        Result result = Result.getInstance();
        LambdaQueryWrapper<UserEntity> lambda = new QueryWrapper<UserEntity>().lambda();
        lambda.eq(UserEntity::getMobile, userName);
        UserEntity user = userService.getOne(lambda);
        if (null == user){
            result.setMessage("账号不存在");
            result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
            return result;
        }
        if (!user.getPassword().equals(new SimpleHash("MD5", password, "", 1).toString())){
            result.setMessage("密码错误");
            result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
            return result;
        }
        AppletsUserEntity oneCustomize = appletsUserService.getOneCustomize(openid, unionid);
        AppletsUserRelationEntity appletsUserRelationEntity = new AppletsUserRelationEntity();
        appletsUserRelationEntity.setUserId(user.getId());
        appletsUserRelationEntity.setWxUserId(oneCustomize.getWxUserId());
        appletsUserRelationService.save(appletsUserRelationEntity);
        result.setMessage("注册成功");
        result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
        return result;
    }
}
