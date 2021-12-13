package com.cloud.controller.weixin;

import com.cloud.service.CoreService;
import com.cloud.thread.AccessTokenThread;
import com.cloud.util.SignUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@RestController
@RequestMapping("/wx/attest")
@Slf4j
public class CoreController {

    @Autowired
    private CoreService coreService;

    //验证是否来自微信服务器的消息  认证
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String checkSignature(@RequestParam(name = "signature", required = false) String signature,
                                 @RequestParam(name = "nonce", required = false) String nonce,
                                 @RequestParam(name = "timestamp", required = false) String timestamp,
                                 @RequestParam(name = "echostr", required = false) String echostr) {
        // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
        if (SignUtil.checkSignature(signature, timestamp, nonce)) {
            log.info("接入成功");
            return echostr;
        }
        log.error("接入失败");
        return "";
    }

    // 调用核心业务类接收消息、处理消息跟推送消息
    @RequestMapping(value = "", method = RequestMethod.POST)
    public void post(HttpServletRequest req, HttpServletResponse resp) {
        String respMessage= "";
        try {
            req.setCharacterEncoding("UTF-8");
            resp.setCharacterEncoding("UTF-8");
            respMessage = coreService.processRequest(req);
            log.info("-----------respMessage"+respMessage);
            PrintWriter out = resp.getWriter();
            out.print(respMessage);
            //return respMessage;
        } catch (Exception e) {
            e.printStackTrace();
        }

        //return respMessage;

    }
    @Autowired
    private AccessTokenThread accessTokenThread;
    @RequestMapping(value = "/gettoken")
    public void gettoken(){
        accessTokenThread.gettoken();
    }

}
