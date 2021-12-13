package com.cloud.service.weixin;

import com.alibaba.fastjson.JSONObject;
import com.cloud.thread.AccessTokenThread;
import com.cloud.util.Result;
import com.cloud.util.WeixinUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信模板消息接口
 */
@Service
@Slf4j
public class WX_TemplateMessageService {
    /**
     * 设置所属行业
     * http请求方式: POST https://api.weixin.qq.com/cgi-bin/template/api_set_industry?access_token=ACCESS_TOKEN
     */
    private static String set_industry_url = "https://api.weixin.qq.com/cgi-bin/template/api_set_industry?access_token=ACCESS_TOKEN";
    /**
     * 获取设置的行业信息
     * GET
     */
    private static String get_set_industry_information_url = "https://api.weixin.qq.com/cgi-bin/template/get_industry?access_token=ACCESS_TOKEN";
    /**
     * 获得模板ID
     * POST
     */
    private static String get_template_id_url = "https://api.weixin.qq.com/cgi-bin/template/api_add_template?access_token=ACCESS_TOKEN";

    /**
     * 获取模板列表
     * GET
     */
    private static String get_list_of_templates_url = "https://api.weixin.qq.com/cgi-bin/template/get_all_private_template?access_token=ACCESS_TOKEN";

    /**
     * 删除模板
     * POST
     */
    private static String delete_template_url = "https://api.weixin.qq.com/cgi-bin/template/del_private_template?access_token=ACCESS_TOKEN";
    /**
     * 发送模板消息
     * POST
     */
    private static String send_template_message_url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";

    /**
     * 发送模板消息
     * @return
     *
     * 参数说明
     *
     * 参数	是否必填	说明
     * touser	是	接收者openid
     * template_id	是	模板ID
     * url	否	模板跳转链接（海外帐号没有跳转能力）
     * miniprogram	否	跳小程序所需数据，不需跳小程序可不用传该数据
     * appid	是	所需跳转到的小程序appid（该小程序appid必须与发模板消息的公众号是绑定关联关系，暂不支持小游戏）
     * pagepath	否	所需跳转到小程序的具体页面路径，支持带参数,（示例index?foo=bar），要求该小程序已发布，暂不支持小游戏
     * data	是	模板数据
     * color	否	模板内容字体颜色，不填默认为黑色
     *
     * {
     *            "touser":"OPENID",
     *            "template_id":"ngqIpbwh8bUfcSsECmogfXcV14J0tQlEpBO27izEYtY",
     *            "url":"http://weixin.qq.com/download",
     *            "miniprogram":{
     *              "appid":"xiaochengxuappid12345",
     *              "pagepath":"index?foo=bar"
     *            },
     *            "data":{
     *                    "first": {
     *                        "value":"恭喜你购买成功！",
     *                        "color":"#173177"
     *                    },
     *                    "keyword1":{
     *                        "value":"巧克力",
     *                        "color":"#173177"
     *                    },
     *                    "keyword2": {
     *                        "value":"39.8元",
     *                        "color":"#173177"
     *                    },
     *                    "keyword3": {
     *                        "value":"2014年9月22日",
     *                        "color":"#173177"
     *                    },
     *                    "remark":{
     *                        "value":"欢迎再次购买！",
     *                        "color":"#173177"
     *                    }
     *            }
     *        }
     */
    public Result send_template_message(){
        Result result = Result.getInstance();
        String at = AccessTokenThread.accessToken.getToken();
        // 拼装创建菜单的url
        String url = send_template_message_url.replace("ACCESS_TOKEN", at);
        Map<String,Object> map = new HashMap<>();
//        您已提交领奖申请\n\n领奖金额：xxxx元\n领奖时间：2013-10-10 12:22:22\
//        n银行信息：xx银行(尾号xxxx)\n到账时间：预计xxxxxxx\n\n预计将于xxxx到达您的银行卡"
        map.put("touser" , "oUBht6dSdmRzBOtbZl93BgSPSeEk");
        map.put("template_id" , "OmE9A__I_FSyUoO6UTlxgZ1HwLxnBj6fqskfjfe03l0");
        Map<String,Object> data = new HashMap<>();
        Map<String,String> res = new HashMap<>();
        res.put("value","您已提交领奖申请");
        res.put("color","#173177");
        data.put("first",res);
        Map<String,String> withdrawMoney = new HashMap<>();
        withdrawMoney.put("value","2000元");
        withdrawMoney.put("color","#173177");
        data.put("withdrawMoney",withdrawMoney);
        Map<String,String> withdrawTime = new HashMap<>();
        withdrawTime.put("value","2013-10-10 12:22:22");
        withdrawTime.put("color","#173177");
        data.put("withdrawTime",withdrawTime);
        Map<String,String> cardInfo = new HashMap<>();
        cardInfo.put("value","xx银行(尾号xxxx)");
        cardInfo.put("color","#173177");
        data.put("cardInfo",cardInfo);
        Map<String,String> arrivedTime = new HashMap<>();
        arrivedTime.put("value","预计xxxxxxx");
        arrivedTime.put("color","#173177");
        data.put("arrivedTime",arrivedTime);
        Map<String,String> remark = new HashMap<>();
        remark.put("value","预计将于xxxx到达您的银行卡");
        remark.put("color","#173177");
        data.put("remark",remark);
        map.put("data",data);
        // 调用接口查询菜单
        JSONObject jsonObject = WeixinUtil.httpRequest(url, "POST", JSONObject.toJSONString(map));
        log.info("-------------获取模板列表 响应结果:"+jsonObject);
        result.setMessage("获取模板列表   成功");
        result.setData(jsonObject);
        return result;
    }

    /**
     * 删除模板
     * POST
     */
    public Result delete_template(){
        Result result = Result.getInstance();
        String at = AccessTokenThread.accessToken.getToken();
        // 拼装创建菜单的url
        String url = delete_template_url.replace("ACCESS_TOKEN", at);
        Map<String,String> map = new HashMap<>();
        //"template_id" : "Dyvp3-Ff0cnail_CDSzk1fIc6-9lOkxsQE7exTJbwUE"
        map.put("template_id" , "Dyvp3-Ff0cnail_CDSzk1fIc6-9lOkxsQE7exTJbwUE");
        // 调用接口查询菜单
        JSONObject jsonObject = WeixinUtil.httpRequest(url, "POST", JSONObject.toJSONString(map));
        log.info("-------------获取模板列表 响应结果:"+jsonObject);
        result.setMessage("获取模板列表   成功");
        result.setData(jsonObject);
        return result;
    }
    /**
     * 获取模板列表
     * GET
     * {
     *      "template_list": [{
     *       "template_id": "iPk5sOIt5X_flOVKn5GrTFpncEYTojx6ddbt8WYoV5s",
     *       "title": "领取奖金提醒",
     *       "primary_industry": "IT科技",
     *       "deputy_industry": "互联网|电子商务",
     *       "content": "{ {result.DATA} }\n\n领奖金额:{ {withdrawMoney.DATA} }\n领奖  时间:    { {withdrawTime.DATA} }\n银行信息:{ {cardInfo.DATA} }\n到账时间:  { {arrivedTime.DATA} }\n{ {remark.DATA} }",
     *       "example": "您已提交领奖申请\n\n领奖金额：xxxx元\n领奖时间：2013-10-10 12:22:22\n银行信息：xx银行(尾号xxxx)\n到账时间：预计xxxxxxx\n\n预计将于xxxx到达您的银行卡"
     *    }]
     * }
     */
    public Result get_list_of_templates(){
        Result result = Result.getInstance();
        String at = AccessTokenThread.accessToken.getToken();
        // 拼装创建菜单的url
        String url = get_list_of_templates_url.replace("ACCESS_TOKEN", at);
        // 调用接口查询菜单
        JSONObject jsonObject = WeixinUtil.httpRequest(url, "GET", null);
        log.info("-------------获取模板列表 响应结果:"+jsonObject);
        result.setMessage("获取模板列表   成功");
        result.setData(jsonObject);
        return result;
    }

    /**
     * 设置所属行业
     */
    public Result set_industry(String[] ids){
        Result result = Result.getInstance();
        String at = AccessTokenThread.accessToken.getToken();
        // 拼装创建菜单的url
        String url = set_industry_url.replace("ACCESS_TOKEN", at);
        Map<String,String> map = new HashMap<>();

//        for (int i = 0; i < ids.length; i++) {
//            map.put("industry_id"+(i+1),ids[i]);
//        }

        map.put("industry_id1","1");
        map.put("industry_id2","7");
        map.put("industry_id3", "10");
        map.put("industry_id4","17");
        map.put("industry_id5","18");
        map.put("industry_id6","22");
        String id = JSONObject.toJSONString(map);
        log.info("-------------设置所属行业:"+id);
        // 调用接口查询菜单
        JSONObject jsonObject = WeixinUtil.httpRequest(url, "POST", id);
        log.info("-------------设置所属行业 响应结果:"+jsonObject);
        result.setMessage("设置成功");
        result.setData(jsonObject);
        return result;
    }

    /**
     * 获取设置的行业信息
     * GET
     * 响应数据结构
     * {
     *     "primary_industry":{"first_class":"运输与仓储","second_class":"快递"},
     *     "secondary_industry":{"first_class":"IT科技","second_class":"互联网|电子商务"}
     * }
     *
     * 参数	是否必填	说明
     * access_token	是	接口调用凭证
     * primary_industry	是	帐号设置的主营行业
     * secondary_industry	是	帐号设置的副营行业
     */
    public Result get_set_industry_information(){
        Result result = Result.getInstance();
        String at = AccessTokenThread.accessToken.getToken();
        // 拼装创建菜单的url
        String url = get_set_industry_information_url.replace("ACCESS_TOKEN", at);
        // 调用接口查询菜单
        JSONObject jsonObject = WeixinUtil.httpRequest(url, "GET", null);
        log.info("-------------获取设置的行业信息 响应结果:"+jsonObject);
        result.setMessage("获取设置的行业信息成功");
        result.setData(jsonObject);
        return result;
    }

    /**
     * 获得模板ID
     */
    public Result get_template_id(){
        Result result = Result.getInstance();
        String at = AccessTokenThread.accessToken.getToken();
        // 拼装创建菜单的url
        String url = get_template_id_url.replace("ACCESS_TOKEN", at);
        //"template_id_short":"TM00015"
        Map<String,String> map = new HashMap<>();
        map.put("template_id_short","TM00015");
        // 调用接口查询菜单
        JSONObject jsonObject = WeixinUtil.httpRequest(url, "POST", JSONObject.toJSONString(map));
        log.info("-------------获得模板ID 响应结果:"+jsonObject);
        result.setMessage("获得模板ID   成功");
        result.setData(jsonObject);
        return result;
    }

}