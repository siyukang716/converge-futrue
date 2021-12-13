package com.cloud.order.controller;

import com.cloud.order.service.OrderCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 *
 * @ClassName: OrderCartController
 * @Description: TODO(这里用一句话描述这个类的作用)购物车表
 * @author wjg
 * @date 2021-08-05
 */
@Controller
@RequestMapping(value ="/orderCartEntity")
public class OrderCartController  {
@Autowired
private OrderCartService orderCartService;

}