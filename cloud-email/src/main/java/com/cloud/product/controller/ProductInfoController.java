package com.cloud.product.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloud.product.entity.ProductInfoEntity;
import com.cloud.product.service.ProductInfoService;
import com.cloud.product.vo.ProductInfoVo;
import com.cloud.util.IStatusMessage;
import com.cloud.util.PageDataResult;
import com.cloud.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;


/**
 * @author wjg
 * @ClassName: ProductInfoController
 * @Description: TODO(这里用一句话描述这个类的作用)商品信息表
 * @date 2021-08-05
 */
@Controller
@Api(value = "商品信息crud功能", tags = "ProductInfoController")
@RequestMapping(value = "/product/info")
public class ProductInfoController {
    @Autowired
    private ProductInfoService productInfoService;

    @RequestMapping("/tolist")
    public ModelAndView toList() {
        ModelAndView model = new ModelAndView();
        model.setViewName("product/productinfo");
        return model;
    }
    @RequestMapping("/tosales")
    public ModelAndView tosales() {
        ModelAndView model = new ModelAndView();
        model.setViewName("product/sales_statistics");
        return model;
    }

    /**
     * 商品列表页面 给消费者
     * @return
     */
    @RequestMapping("/list")
    public ModelAndView list() {
        ModelAndView model = new ModelAndView();
        model.setViewName("product/productshow");
        return model;
    }

    /**
     * 消费者 页面
     * @return
     */
    @RequestMapping("/{productId}")
    public ModelAndView productDetails(@PathVariable("productId")Integer productId) {
        ModelAndView model = new ModelAndView();
        Map map = productInfoService.getProductDetails(productId);
        model.addObject("map",map);
        model.setViewName("product/productDetails");
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
    public PageDataResult getPageList(Page<ProductInfoEntity> p) {
        PageDataResult pdr = new PageDataResult();
        try {

            QueryWrapper<ProductInfoEntity> wrapper = new QueryWrapper<>();
            IPage<ProductInfoVo> page = productInfoService.selectPagePartner(p, wrapper);

            pdr.setTotals((int) page.getTotal());
            pdr.setList(page.getRecords());
        } catch (Exception e) {
            e.printStackTrace();
            pdr.setCode(500);
        }
        return pdr;
    }

    /**
     * 销售信息
     * @param p
     * @return
     */
    @RequestMapping("/getsales")
    @ResponseBody
    @ApiOperation(value = "销售信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    public PageDataResult getsales(Page<ProductInfoEntity> p) {
        PageDataResult pdr = new PageDataResult();
        IPage<ProductInfoVo> page = productInfoService.getsales(p);
        pdr.setTotals((int) page.getTotal());
        pdr.setList(page.getRecords());
        return pdr;
    }

    @RequestMapping("/getProductPage")
    @ResponseBody
    public PageDataResult getProductPage(Integer page, Integer limit) {
        PageDataResult pdr = new PageDataResult();
        Page<ProductInfoEntity> p = new Page<ProductInfoEntity>();
        try {
            p.setCurrent(page);
            p.setSize(limit);

            IPage<ProductInfoEntity> pa = productInfoService.getProductPage(p);

            pdr.setTotals((int) pa.getTotal());
            pdr.setList(pa.getRecords());
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
    public Result aOrU(ProductInfoEntity caentity) {
        Result result = Result.getInstance();
        try {
            LambdaUpdateWrapper<ProductInfoEntity> wrapper = Wrappers.<ProductInfoEntity>lambdaUpdate();
            productInfoService.saveOrUpdate(caentity);
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
     * @param productId
     * @return
     */
    @RequestMapping("/del")
    @ResponseBody
    public Result del(Integer productId) {
        Result result = Result.getInstance();
        try {
            productInfoService.removeById(productId);
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
    public Result getById(Integer productId) {
        Result result = Result.getInstance();
        try {
            ProductInfoEntity obj = productInfoService.getById(productId);
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