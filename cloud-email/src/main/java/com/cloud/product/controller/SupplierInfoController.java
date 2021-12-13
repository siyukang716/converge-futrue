package com.cloud.product.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloud.product.entity.SupplierInfoEntity;
import com.cloud.product.service.SupplierInfoService;
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

import java.util.List;


/**
 * @author wjg
 * @ClassName: SupplierInfoController
 * @Description: TODO(这里用一句话描述这个类的作用)供应商信息表
 * @date 2021-08-05
 */
@Controller
@Api(value = "供应商信息crud功能",tags = "SupplierInfo")
@RequestMapping(value = "/supplier/info")
public class SupplierInfoController {
    @Autowired
    private SupplierInfoService supplierInfoService;
    @RequestMapping("/tolist")
    public ModelAndView toList() {
        ModelAndView model = new ModelAndView();
        model.setViewName("product/supplierinfo");
        return model;
    }

    /**
     * 获取供应商列表
     * @param p
     * @return
     */
    @GetMapping("/getList")
    @ResponseBody
    @ApiOperation(value = "用户列表的分页查询",httpMethod = "GET",produces = MediaType.APPLICATION_JSON_VALUE)
    public PageDataResult getPageList(Page<SupplierInfoEntity> p) {
        PageDataResult pdr = new PageDataResult();
        try {
            QueryWrapper<SupplierInfoEntity> wrapper = new QueryWrapper<>();
            IPage<SupplierInfoEntity> page = supplierInfoService.page(p,wrapper);
            pdr.setTotals((int)page.getTotal());
            pdr.setList(page.getRecords());
        }catch (Exception e){
            e.printStackTrace();
            pdr.setCode(500);
        }
        return pdr;
    }

    /**
     * 增加 修改  信息
     * @param clientity
     * @return
     */
    @RequestMapping("/aOrU")
    @ResponseBody
    public Result setUser(SupplierInfoEntity clientity) {
        Result result = Result.getInstance();
        try {
            supplierInfoService.saveOrUpdate(clientity);
            result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
            result.setMessage("操作成功!!!!!!");
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
            result.setMessage("操作异常，请您稍后再试");
        }
        return result;
    }

    /**
     * 删除供应商信息
     * @param supplierId
     * @return
     */
    @RequestMapping("/del")
    @ResponseBody
    public Result del(Integer supplierId) {
        Result result = Result.getInstance();
        try {
            supplierInfoService.removeById(supplierId);
            result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
            result.setMessage("删除成功!!!!!!");
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
            result.setMessage("操作异常，请您稍后再试");
        }
        return result;
    }

    @RequestMapping("/getById")
    @ResponseBody
    public Result getById(Integer supplierId) {
        Result result = Result.getInstance();
        try {
            SupplierInfoEntity obj = supplierInfoService.getById(supplierId);
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



    @RequestMapping("/linkage")
    @ResponseBody
    @ApiOperation(value = "供应商分类下拉框", notes = "供应商分类下拉框")
    public Result linkage(Integer categoryId) {
        Result result = Result.getInstance();
        try {
            LambdaQueryWrapper<SupplierInfoEntity> lambda = new QueryWrapper<SupplierInfoEntity>().lambda();
            lambda.select(SupplierInfoEntity :: getSupplierId,SupplierInfoEntity::getSupplierName);
            List<SupplierInfoEntity> list = supplierInfoService.list(lambda);
            result.setData(list);
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