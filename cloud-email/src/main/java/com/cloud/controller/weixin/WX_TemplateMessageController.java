package com.cloud.controller.weixin;

import com.cloud.service.weixin.WX_TemplateMessageService;
import com.cloud.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 微信模板消息接口
 */
@RestController
@RequestMapping(value = "/w/tp/message")
public class WX_TemplateMessageController {
    @Autowired
    private WX_TemplateMessageService service;

    /**
     * 发送模板消息
     * @return
     */
    @RequestMapping(value = "/send_template_message")
    public Result send_template_message(){
       return service.send_template_message();
    }
    /**
     * 删除模板
     * POST
     */
    @RequestMapping(value = "/delete_template")
    public Result delete_template(){
        return service.delete_template();
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
    @RequestMapping(value = "/get_list_of_templates")
    public Result get_list_of_templates(){
        return service.get_list_of_templates();
    }

    /**
     * 设置所属行业
     */
    @RequestMapping(value = "/set_industry")
    public Result set_industry(){
        return service.set_industry(null);
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
    @RequestMapping(value = "/get_set_industry_information")
    public Result get_set_industry_information(){
        return service.get_set_industry_information();
    }
    /**
     * 获得模板ID
     */
    @RequestMapping(value = "/get_template_id")
    public Result get_template_id(){
        return service.get_template_id();
    }
}
