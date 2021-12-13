package com.cloud.product.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloud.product.entity.MmallAttributeValueEntity;
import com.cloud.product.service.MmallAttributeValueService;
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


/**
 * @author wjg
 * @ClassName: MmallAttributeValueController
 * @Description: TODO(这里用一句话描述这个类的作用)商品 sku   属性value表
 * @date 2021-08-24
 */
@Controller
@Api(value = "商品 sku   属性value表crud功能", tags = "")
@RequestMapping(value = "/prod/sku/attr/val")
public class MmallAttributeValueController {
    @Autowired
    private MmallAttributeValueService mmallAttributeValueService;

    @RequestMapping("/tolist/{makId}")
    public ModelAndView toList(@PathVariable("makId")Integer makId) {
        ModelAndView model = new ModelAndView();
        model.addObject("makId",makId);
        model.setViewName("/product/skuVal");
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
    public PageDataResult getPageList(Page<MmallAttributeValueEntity> p,Integer makId) {
        PageDataResult pdr = new PageDataResult();
        QueryWrapper<MmallAttributeValueEntity> wrapper = new QueryWrapper<MmallAttributeValueEntity>();
        wrapper.eq("mak_id",makId);
        IPage<MmallAttributeValueEntity> page = mmallAttributeValueService.page(p, wrapper);
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
    public Result aOrU(MmallAttributeValueEntity entity) {
        Result result = Result.getInstance();
        LambdaUpdateWrapper<MmallAttributeValueEntity> wrapper = Wrappers.<MmallAttributeValueEntity>lambdaUpdate();
        boolean b = mmallAttributeValueService.saveOrUpdate(entity);
        result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
        result.setMessage("完成信息完善!!!!!!");
        if (!b){
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
    public Result del(Integer mavId) {
        Result result = Result.getInstance();
        boolean b = mmallAttributeValueService.removeById(mavId);
        result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
        result.setMessage("删除成功!!!!!!");
        if (!b){
            result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
            result.setMessage("操作异常，请您稍后再试");
        }
        return result;
    }

    @RequestMapping("/getById")
    @ResponseBody
    public Result getById(Integer mavId) {
        Result result = Result.getInstance();
        MmallAttributeValueEntity obj = mmallAttributeValueService.getById(mavId);
        result.setData(obj);
        result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
        result.setMessage("获取成功!!!!!!");
        if (null == obj){
            result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
            result.setMessage("操作异常，请您稍后再试");
        }

        return result;
    }
}