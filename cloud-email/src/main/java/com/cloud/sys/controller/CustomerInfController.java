package com.cloud.sys.controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cloud.sys.entity.CustomerInfEntity;
import com.cloud.sys.service.CustomerInfService;
import com.cloud.util.IStatusMessage;
import com.cloud.util.Result;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * @author wjg
 * @ClassName: CustomerInfController
 * @Description: TODO(这里用一句话描述这个类的作用)用户信息表
 * @date 2021-08-05
 */
@Controller
@Api(value = "用户信息表crud功能", tags = "用户信息表crud功能")
@RequestMapping(value = "/customer/inf")
public class CustomerInfController {
    @Autowired
    private CustomerInfService customerInfService;


    /**
     * 设置用户[新增或更新]
     *
     * @return ok/fail
     */
    @RequestMapping("/aOrU")
    @ResponseBody
    public Result setUser(CustomerInfEntity info) {
        Result result = Result.getInstance();
        try {
//            LambdaUpdateWrapper<CustomerInfEntity> wrapper = Wrappers.<CustomerInfEntity>lambdaUpdate();
//            wrapper.eq(CustomerInfEntity ::getCustomerId,info.getCustomerId());
//            wrapper.eq(CustomerInfEntity :: getCustomerInfId,info.getCustomerInfId());
            customerInfService.saveOrUpdate(info);

            result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
            result.setMessage("完成信息完善!!!!!!");
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
            result.setMessage("操作异常，请您稍后再试");
        }
        return result;
    }

    @RequestMapping("/getById")
    @ResponseBody
    public Result getById(String customerId) {
        Result result = Result.getInstance( );
        try {
            LambdaUpdateWrapper<CustomerInfEntity> wrapper = Wrappers.<CustomerInfEntity>lambdaUpdate();
            wrapper.eq(CustomerInfEntity ::getCustomerId,customerId);
            CustomerInfEntity ciEntity = customerInfService.getOne(wrapper);
            result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
            result.setData(ciEntity);
            result.setMessage("完成信息完善!!!!!!");
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
            result.setMessage("操作异常，请您稍后再试");
        }
        return result;
    }
}