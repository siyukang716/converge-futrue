package com.cloud.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloud.sys.entity.CustomerAddrEntity;
import com.cloud.sys.service.CustomerAddrService;
import com.cloud.util.IStatusMessage;
import com.cloud.util.PageDataResult;
import com.cloud.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.annotations.ApiIgnore;


/**
 * @author wjg
 * @ClassName: CustomerAddrController
 * @Description: TODO(这里用一句话描述这个类的作用)用户地址表
 * @date 2021-08-05
 */
@Controller
@Api(value = "用户地址crud功能",tags = "CustomerAddr")
@ApiIgnore
@RequestMapping(value = "/customer/addr")
public class CustomerAddrController {
    @Autowired
    private CustomerAddrService customerAddrService;

    @RequestMapping("/tolist")
    public ModelAndView toList(Long customerId) {
        ModelAndView model = new ModelAndView();
        model.addObject("customerId",customerId);
        model.setViewName("auth/customerAddr");
        return model;
    }
    /**
     *
     * @param p
     * @param customerId
     * @return
     */
    @GetMapping("/getList")
    @ResponseBody
    @ApiOperation(value = "分页查询",httpMethod = "GET",produces = MediaType.APPLICATION_JSON_VALUE)
    public PageDataResult getPageList(Page<CustomerAddrEntity> p, Long customerId) {
        PageDataResult pdr = new PageDataResult();
        try {
            QueryWrapper<CustomerAddrEntity> wrapper = new QueryWrapper<>();
            wrapper.eq("customer_id", customerId);
            IPage<CustomerAddrEntity> page = customerAddrService.page(p,wrapper);
            pdr.setTotals((int)page.getTotal());
            pdr.setList(page.getRecords());
        }catch (Exception e){
            e.printStackTrace();
            pdr.setCode(500);
        }

        return pdr;
    }


    /**
     * 增加或修改地址信息
     * @param caentity
     * @return
     */
    @RequestMapping("/aOrU")
    @ResponseBody
    public Result setUser(CustomerAddrEntity caentity) {
        Result result = Result.getInstance();
        try {
            LambdaUpdateWrapper<CustomerAddrEntity> wrapper = Wrappers.<CustomerAddrEntity>lambdaUpdate();
//            wrapper.eq(CustomerInfEntity ::getCustomerId,info.getCustomerId());
//            wrapper.eq(CustomerInfEntity :: getCustomerInfId,info.getCustomerInfId());
            if (caentity.getIsDefault() == 1){
                wrapper.eq(CustomerAddrEntity :: getCustomerId,caentity.getCustomerId()).set(CustomerAddrEntity :: getIsDefault,0);
                customerAddrService.update(wrapper);
            }
            customerAddrService.saveOrUpdate(caentity);
            result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
            result.setMessage("完成信息完善!!!!!!");
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
            result.setMessage("操作异常，请您稍后再试");
        }
        return result;
    }

    /**
     * 删除地址信息
     * @param customerAddrId
     * @return
     */
    @RequestMapping("/del")
    @ResponseBody
    public Result del(Integer customerAddrId) {
        Result result = Result.getInstance();
        try {
            customerAddrService.removeById(customerAddrId);
            result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
            result.setMessage("地址信息删除成功!!!!!!");
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
            result.setMessage("操作异常，请您稍后再试");
        }
        return result;
    }

    @GetMapping("/getById")
    @ResponseBody
    public Result getById(Integer customerAddrId) {
        Result result = Result.success();
        try {
            CustomerAddrEntity obj = customerAddrService.getById(customerAddrId);
            result.setData(obj);
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
            result.setMessage("操作异常，请您稍后再试");
        }
        return result;
    }

}