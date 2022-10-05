package com.cloud.sys.controller;

import com.cloud.sys.service.CustomerBalanceLogService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;


/**
 * @author wjg
 * @ClassName: CustomerBalanceLogController
 * @Description: TODO(这里用一句话描述这个类的作用)用户余额变动表
 * @date 2021-08-05
 */
@Controller
@Api(value = "用户余额变动crud功能", tags = "CustomerBalanceLog")
@ApiIgnore
@RequestMapping(value = "/customerBalanceLogEntity")
public class CustomerBalanceLogController {
    @Autowired
    private CustomerBalanceLogService customerBalanceLogService;

}