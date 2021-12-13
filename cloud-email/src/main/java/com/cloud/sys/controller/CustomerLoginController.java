package com.cloud.sys.controller;

import com.cloud.sys.service.CustomerLoginService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 *
 * @ClassName: CustomerLoginController
 * @Description: TODO(这里用一句话描述这个类的作用)用户登录表
 * @author wjg
 * @date 2021-08-05
 */
@Controller
@Api(value = "用户登录表crud功能",tags = "CustomerLogin")
@RequestMapping(value ="/customerLoginEntity")
public class CustomerLoginController  {
@Autowired
private CustomerLoginService customerLoginService;

}