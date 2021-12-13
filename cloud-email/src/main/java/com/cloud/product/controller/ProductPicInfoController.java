package com.cloud.product.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloud.product.entity.ProductPicInfoEntity;
import com.cloud.product.service.ProductPicInfoService;
import com.cloud.properties.ProductFileProperties;
import com.cloud.util.IStatusMessage;
import com.cloud.util.PageDataResult;
import com.cloud.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;


/**
 * @author wjg
 * @ClassName: ProductPicInfoController
 * @Description: TODO(这里用一句话描述这个类的作用)商品图片信息表
 * @date 2021-08-05
 */
@Controller
@Slf4j
@Api(value = "商品图片信息表crud功能", tags = "ProductPicInfoController")
@RequestMapping(value = "/product/pic/info")
public class ProductPicInfoController {
    @Autowired
    private ProductPicInfoService productPicInfoService;
    @Autowired
    private ProductFileProperties properties;





    @RequestMapping("/tolist")
    public ModelAndView toList(Integer productId) {
        ModelAndView model = new ModelAndView();
        model.addObject("productId",productId);
        model.setViewName("product/productpicinfo");
        return model;
    }

    /**
     * 列表查询
     *
     * @param p
     * @return
     */
    @GetMapping("/getList")
    @ResponseBody
    @ApiOperation(value = "列表查询", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    public PageDataResult getPageList(Page<ProductPicInfoEntity> p,Integer productId) {
        PageDataResult pdr = new PageDataResult();
        try {
            LambdaQueryWrapper<ProductPicInfoEntity> wrapper = new QueryWrapper<ProductPicInfoEntity>().lambda();
            wrapper.eq(ProductPicInfoEntity::getProductId,productId);
            IPage<ProductPicInfoEntity> page = productPicInfoService.page(p, wrapper);
            pdr.setTotals((int) page.getTotal());
            pdr.setList(page.getRecords());
        } catch (Exception e) {
            e.printStackTrace();
            pdr.setCode(500);
        }
        return pdr;
    }


    /**
     * 增加或修改
     *
     * @param caentity
     * @return
     */
    @RequestMapping("/aOrU")
    @ResponseBody
    public Result aOrU(ProductPicInfoEntity caentity) {
        Result result = Result.getInstance();
        try {
            //LambdaUpdateWrapper<ProductPicInfoEntity> wrapper = Wrappers.<ProductPicInfoEntity>lambdaUpdate();
            productPicInfoService.saveOrUpdate(caentity);
            result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
            result.setMessage("保存成功!!!!!!");
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
            result.setMessage("操作异常，请您稍后再试");
        }
        return result;
    }

    /**
     * 删除
     *
     * @param productPicId
     * @return
     */
    @RequestMapping("/del")
    @ResponseBody
    public Result del(Integer productPicId) {
        Result result = Result.getInstance();
        try {
            //LambdaUpdateWrapper<ProductPicInfoEntity> wrapper = Wrappers.<ProductPicInfoEntity>lambdaUpdate();
            //wrapper.eq(ProductPicInfoEntity::getBrandId,brandId).set(ProductPicInfoEntity::getBrandStatus,0);
            productPicInfoService.removeById(productPicId);
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
    public Result getById(Integer productPicId) {
        Result result = Result.getInstance();
        try {
            ProductPicInfoEntity obj = productPicInfoService.getById(productPicId);
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
    @PostMapping("/upload")
    @ResponseBody
    public Result upload(@RequestParam("file") MultipartFile file, Integer businessId, HttpServletRequest request) {
        return properties.uploadProduct(file,businessId);
    }


}