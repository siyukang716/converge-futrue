package com.cloud.sys.controller;

import com.cloud.sys.service.CustomerPointLogService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 *
 * @ClassName: CustomerPointLogController
 * @Description: TODO(这里用一句话描述这个类的作用)用户积分日志表
 * @author wjg
 * @date 2021-08-05
 */
@Controller
@Api(value = "用户积分日志表crud功能",tags = "CustomerPointLog")
@RequestMapping(value ="/customerPointLogEntity")
public class CustomerPointLogController  {
@Autowired
private CustomerPointLogService customerPointLogService;

}