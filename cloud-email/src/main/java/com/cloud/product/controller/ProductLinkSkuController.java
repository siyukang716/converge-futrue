package com.cloud.product.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloud.product.entity.ProductLinkSkuEntity;
import com.cloud.product.service.ProductLinkSkuService;
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
 * @ClassName: ProductLinkSkuController
 * @Description: TODO(这里用一句话描述这个类的作用)商品 属性关联表
 * @date 2021-08-25
 */
@Controller
@Api(value = "商品 属性关联表crud功能", tags = "")
@RequestMapping(value = "/product/Link/Sku")
public class ProductLinkSkuController {
    @Autowired
    private ProductLinkSkuService productLinkSkuService;

    @RequestMapping("/tolist")
    public ModelAndView toList() {
        ModelAndView model = new ModelAndView();
        model.setViewName("");
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
    public PageDataResult getPageList(Page<ProductLinkSkuEntity> p) {
        PageDataResult pdr = new PageDataResult();
        QueryWrapper<ProductLinkSkuEntity> wrapper = new QueryWrapper<ProductLinkSkuEntity>();
        IPage<ProductLinkSkuEntity> page = productLinkSkuService.page(p, wrapper);
        pdr.setTotals((int) page.getTotal());
        pdr.setList(page.getRecords());
        return pdr;
    }


    /**
     * 增加或修改地址信息
     *
     * @param
     * @return
     */
    @RequestMapping("/aOrU")
    @ResponseBody
    public Result aOrU(Integer productId ,String keys) {
        Result result = Result.getInstance();
        LambdaUpdateWrapper<ProductLinkSkuEntity> wrapper = Wrappers.<ProductLinkSkuEntity>lambdaUpdate();
        boolean b = productLinkSkuService.saveOrUpdateL(productId,keys);
        result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
        result.setMessage("完成信息完善!!!!!!");
        if (!b) {
            result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
            result.setMessage("操作异常，请您稍后再试");
        }
        return result;
    }






    /**
     * 删除地址信息
     *
     * @param
     * @return
     */
    @RequestMapping("/del")
    @ResponseBody
    public Result del(Integer id) {
        Result result = Result.getInstance();
        boolean b = productLinkSkuService.removeById(id);
        result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
        result.setMessage("删除成功!!!!!!");
        if (!b) {
            result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
            result.setMessage("操作异常，请您稍后再试");
        }
        return result;
    }

    @RequestMapping("/getById")
    @ResponseBody
    public Result getById(Integer id) {
        Result result = Result.getInstance();
        ProductLinkSkuEntity obj = productLinkSkuService.getById(id);
        result.setData(obj);
        result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
        result.setMessage("获取成功!!!!!!");
        if (obj == null) {
            result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
            result.setMessage("操作异常，请您稍后再试");
        }
        return result;
    }
}