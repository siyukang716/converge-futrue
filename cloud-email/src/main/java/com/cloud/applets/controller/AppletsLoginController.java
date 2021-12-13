package com.cloud.applets.controller;

import com.alibaba.fastjson.JSONObject;
import com.cloud.applets.service.AppletsLoginService;
import com.cloud.shiro.ShiroUtils;
import com.cloud.sys.UserEntity;
import com.cloud.util.IStatusMessage;
import com.cloud.util.Result;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @Description: AppletsLoginController 微信小程序入口类
 * @Author lenovo
 * @Date: 2021/11/12 15:20
 * @Version 1.0
 */

@Controller
@Api(value = "微信小程序入口类", tags = "微信小程序入口类")
@RequestMapping(value = "applets/entrance")
@Slf4j
public class AppletsLoginController {

    @Autowired
    private AppletsLoginService appletsLoginService;


    /**
     * auth.code2Session  微信登录接口
     *
     * @param code
     * @return
     */
    @GetMapping("/code2Session")
    @ResponseBody
    @ApiOperation(value = "微信登录接口", notes = "微信登录接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "登录时获取的 code", required = true, paramType = "query")
    })
    public Result code2Session(String code) {
        Result result = Result.getInstance();
        JSONObject jsonObject = appletsLoginService.code2Session(code);
        result.setData(jsonObject);
        String errcode = String.valueOf(jsonObject.get("errcode"));
        if (null == errcode ||"null".equals(errcode)) {
            result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
            result.setMessage("操作成功");
        } else {
            result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
            switch (errcode) {
                case "-1":
                    result.setMessage("系统繁忙，此时请开发者稍候再试");
                    break;
                case "40029":
                    result.setMessage("code 无效");
                    break;
                case "45011":
                    result.setMessage("频率限制，每个用户每分钟100次");
                    break;
                case "40226":
                    result.setMessage("高风险等级用户，小程序登录拦截 。");
                    break;
            }
        }
        return result;
    }

    @GetMapping("/getData")
    @ResponseBody
    @ApiOperation(value = "小程序判断是否已登录", notes = "小程序判断是否已登录")
    public Result getData() {
        Result result = Result.getInstance();
        UserEntity loginUser = ShiroUtils.getLoginUser();
        result.setData(loginUser);
        result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
        result.setMessage("操作成功");
        return result;
    }

    /**
     * 微信注册接口
     *
     * @param userName
     * @param password
     * @return
     */
    @PostMapping(value = "register")
    @ResponseBody
    @ApiOperation(value = "微信注册接口", notes = "微信注册接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "系统账号", required = true, paramType = "query"),
            @ApiImplicitParam(name = "password", value = "系统密码", required = true, paramType = "query"),
            @ApiImplicitParam(name = "openid", value = "openid", required = true, paramType = "query"),
            @ApiImplicitParam(name = "unionid", value = "unionid", required = true, paramType = "query")
    })
    public Result register(@RequestParam String userName, @RequestParam String password, @RequestParam String openid,@RequestParam String unionid) {//HttpServletRequest request,
        return appletsLoginService.register(userName, password, openid,unionid);
    }


    @PostMapping(value = "login")
    @ResponseBody
    @ApiResponses({
            @ApiResponse(code = 40029, message = "微信code授权失败"),
            @ApiResponse(code = 40028, message = "未关联系统用户")
    })
    @ApiOperation(value = "微信登录接口", notes = "微信登录接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "登录时获取的 code", required = true, paramType = "query"),
            @ApiImplicitParam(name = "appId", value = "小程序id", required = true, paramType = "query"),
            @ApiImplicitParam(name = "nickName", value = "微信用户名称 code", required = true, paramType = "query"),
            @ApiImplicitParam(name = "gender", value = "性别 ", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "language", value = "语言 ", required = true, paramType = "query")
    })
    public Result login(@RequestParam String openid,@RequestParam String unionid, @RequestParam String appId, @RequestParam String nickName, @RequestParam Integer gender
            , @RequestParam String language) {//HttpServletRequest request,
        Result result = Result.getInstance();
        try {
            result = appletsLoginService.appletsUservVerify(openid,unionid, appId, nickName, gender, language);
        } catch (Exception e) {
            //登录失败从request中获取shiro处理的异常信息   shiroLoginfailure 就是shiro异常类名全称
            //String shiroLoginfailure = (String) request.getAttribute("shiroLoginfailure");
            result.setMessage("登录错误");
            if (e instanceof UnknownAccountException)
                result.setMessage("用户名不存在");
            if (e instanceof IncorrectCredentialsException)
                result.setMessage("密码错误");
            if (e instanceof LockedAccountException)
                result.setMessage("账号已被锁定,请联系管理员");
            result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
            e.printStackTrace();
        }
        return result;

    }


}
