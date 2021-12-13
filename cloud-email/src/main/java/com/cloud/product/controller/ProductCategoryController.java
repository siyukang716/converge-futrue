package com.cloud.product.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloud.product.entity.ProductCategoryEntity;
import com.cloud.product.service.ProductCategoryService;
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
import java.util.Map;


/**
 * @author wjg
 * @ClassName: ProductCategoryController
 * @Description: TODO(这里用一句话描述这个类的作用)商品分类表
 * @date 2021-08-05
 */
@Controller
@Api(value = "商品分类crud功能",tags = "ProductCategory")
@RequestMapping(value = "/product/category")
public class ProductCategoryController {
    @Autowired
    private ProductCategoryService productCategoryService;



    @RequestMapping("/tolist")
    public ModelAndView toList() {
        ModelAndView model = new ModelAndView();
        model.setViewName("product/productcategory");
        return model;
    }

    /**
     * 获取列表
     * @param p
     * @return
     */
    @GetMapping("/getList")
    @ResponseBody
    @ApiOperation(value = "分页查询",httpMethod = "GET",produces = MediaType.APPLICATION_JSON_VALUE)
    public PageDataResult getPageList(Page<ProductCategoryEntity> p) {
        PageDataResult pdr = new PageDataResult();
        try {
            QueryWrapper<ProductCategoryEntity> wrapper = new QueryWrapper<>();
            IPage<ProductCategoryEntity> page = productCategoryService.page(p,wrapper);
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
    public Result aOrU(ProductCategoryEntity clientity) {
        Result result = Result.getInstance();
        try {
            productCategoryService.saveOrUpdate(clientity);
            result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
            result.setData(clientity);
            result.setMessage("操作成功!!!!!!");
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
            result.setMessage("操作异常，请您稍后再试");
        }
        return result;
    }

    /**
     * 删除
     * @param categoryId
     * @return
     */
    @RequestMapping("/del")
    @ResponseBody
    public Result del(Integer categoryId) {
        Result result = Result.getInstance();
        try {
            LambdaUpdateWrapper<ProductCategoryEntity> wrapper = Wrappers.<ProductCategoryEntity>lambdaUpdate();
            wrapper.eq(ProductCategoryEntity :: getCategoryId,categoryId).set(ProductCategoryEntity:: getCategoryStatus,0);
            productCategoryService.update(wrapper);
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
    public Result getById(Integer categoryId) {
        Result result = Result.getInstance();
        try {
            ProductCategoryEntity obj = productCategoryService.getById(categoryId);
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


    @RequestMapping("/ztree")
    @ResponseBody
    public List<Map<String,Object>> getAllPermTree(Integer categoryId){
        return productCategoryService.ztree(categoryId);
    }
    @RequestMapping("/linkage")
    @ResponseBody
    @ApiOperation(value = "商品分类下拉框", notes = "根据层级查询商品分类下拉框")
    public Result linkage(Integer categoryId) {
        Result result = Result.getInstance();
        try {
            LambdaQueryWrapper<ProductCategoryEntity> lambda = new QueryWrapper<ProductCategoryEntity>().lambda();
            lambda.select(ProductCategoryEntity :: getCategoryId,ProductCategoryEntity::getCategoryName)
                    .eq(ProductCategoryEntity :: getParentId,categoryId);
            List<ProductCategoryEntity> list = productCategoryService.list(lambda);
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