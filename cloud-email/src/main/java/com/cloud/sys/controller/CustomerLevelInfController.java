package com.cloud.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloud.sys.entity.CustomerLevelInfEntity;
import com.cloud.sys.service.CustomerLevelInfService;
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


/**
 * @author wjg
 * @ClassName: CustomerLevelInfController
 * @Description: TODO(这里用一句话描述这个类的作用)用户级别信息表
 * @date 2021-08-05
 */
@Controller
@Api(value = "用户级别信息表crud功能",tags = "CustomerLevelInf")
@RequestMapping(value = "/customer/level/inf")
public class CustomerLevelInfController {
    @Autowired
    private CustomerLevelInfService customerLevelInfService;



    @RequestMapping("/tolist")
    public ModelAndView toList() {
        ModelAndView model = new ModelAndView();
        model.setViewName("auth/customerlevelinf");
        return model;
    }
    /**
     *
     * @param p
     * @return
     */
    @GetMapping("/getList")
    @ResponseBody
    @ApiOperation(value = "用户列表的分页查询",httpMethod = "GET",produces = MediaType.APPLICATION_JSON_VALUE)
    public PageDataResult getPageList(Page<CustomerLevelInfEntity> p) {
        PageDataResult pdr = new PageDataResult();
        try {
            QueryWrapper<CustomerLevelInfEntity> wrapper = new QueryWrapper<>();
            IPage<CustomerLevelInfEntity> page = customerLevelInfService.page(p,wrapper);
            pdr.setTotals((int)page.getTotal());
            pdr.setList(page.getRecords());
        }catch (Exception e){
            e.printStackTrace();
            pdr.setCode(500);
        }

        return pdr;
    }


    /**
     * 增加或修改用户级别
     * @param clientity
     * @return
     */
    @RequestMapping("/aOrU")
    @ResponseBody
    public Result setUser(CustomerLevelInfEntity clientity) {
        Result result = Result.getInstance();
        try {
            //LambdaUpdateWrapper<CustomerLevelInfEntity> wrapper = Wrappers.<CustomerLevelInfEntity>lambdaUpdate();
//            wrapper.eq(CustomerInfEntity ::getCustomerId,info.getCustomerId());
//            wrapper.eq(CustomerInfEntity :: getCustomerInfId,info.getCustomerInfId());
            customerLevelInfService.saveOrUpdate(clientity);
            result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
            result.setMessage("添加成功!!!!!!");
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
            result.setMessage("操作异常，请您稍后再试");
        }
        return result;
    }

    /**
     * 删除用户级别
     * @param customerLevel
     * @return
     */
    @RequestMapping("/del")
    @ResponseBody
    public Result del(Integer customerLevel) {
        Result result = Result.getInstance();
        try {
            customerLevelInfService.removeById(customerLevel);
            result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
            result.setMessage("级别信息删除成功!!!!!!");
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
            result.setMessage("操作异常，请您稍后再试");
        }
        return result;
    }

    @RequestMapping("/getById")
    @ResponseBody
    public Result getById(Integer customerLevel) {
        Result result = Result.getInstance();
        try {
            CustomerLevelInfEntity obj = customerLevelInfService.getById(customerLevel);
            result.setData(obj);
            result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
            result.setMessage("获取成功!!!!!!");
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
            result.setMessage("操作异常，请您稍后再试");
        }
        return result;
    }
}