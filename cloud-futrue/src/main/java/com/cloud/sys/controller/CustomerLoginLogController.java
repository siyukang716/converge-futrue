package com.cloud.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloud.sys.entity.CustomerLoginLogEntity;
import com.cloud.sys.service.CustomerLoginLogService;
import com.cloud.util.PageDataResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


/**
 * @author wjg
 * @ClassName: CustomerLoginLogController
 * @Description: TODO(这里用一句话描述这个类的作用)用户登陆日志表
 * @date 2021-08-05
 */
@Controller
@Api(value = "用户登陆日志表crud功能",tags = "CustomerLoginLog")
@RequestMapping(value = "/customer/login/log")
public class CustomerLoginLogController {
    @Autowired
    private CustomerLoginLogService customerLoginLogService;
    @RequestMapping("/tolist")
    public ModelAndView toList(Long customerId) {
        ModelAndView model = new ModelAndView();
        model.addObject("customerId",customerId);
        model.setViewName("auth/loginLog");
        return model;
    }
    /**
     *
     * @param p
     * @return
     */
    @GetMapping("/getList")
    @ResponseBody
    @ApiOperation(value = "用户登录日志列表",httpMethod = "GET",produces = MediaType.APPLICATION_JSON_VALUE)
    public PageDataResult getPageList(Page<CustomerLoginLogEntity> p,Long customerId) {
        PageDataResult pdr = new PageDataResult();
        try {
            QueryWrapper<CustomerLoginLogEntity> wrapper = new QueryWrapper<>();
            wrapper.eq("customer_id",customerId);
            IPage<CustomerLoginLogEntity> page = customerLoginLogService.page(p,wrapper);
            pdr.setTotals((int)page.getTotal());
            pdr.setList(page.getRecords());
        }catch (Exception e){
            e.printStackTrace();
            pdr.setCode(500);
        }

        return pdr;
    }
}