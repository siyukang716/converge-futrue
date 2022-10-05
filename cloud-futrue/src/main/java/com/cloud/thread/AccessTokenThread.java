package com.cloud.thread;

import com.cloud.entity.AccessToken;
import com.cloud.util.WeixinUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 定时获取微信access_token的线程
 *在WechatMpDemoApplication中注解@EnableScheduling，在程序启动时就开启定时任务。
 * 每7200秒执行一次
 */
@Component
@Slf4j
public class AccessTokenThread {
    //private static Logger log = LoggerFactory.getLogger(AccessTokenThread.class);

    // 第三方用户唯一凭证
    public static String appid = "wxcf58ea49d16b48b9";

    // 第三方用户唯一凭证密钥
    public static String appsecret = "82a89d39edbc9ac574cf078743b3c52f";
    // 第三方用户唯一凭证
    public static AccessToken accessToken = null;

    //@Scheduled(fixedDelay = 2*3600*1000)
    //7200秒执行一次
    public void gettoken(){
        accessToken= WeixinUtil.getAccessToken(appid,appsecret);
        if(null!=accessToken){
            log.info("获取成功，accessToken:"+accessToken.getToken());
        }else {
            log.error("获取token失败");
        }
    }
}
