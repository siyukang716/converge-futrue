package com.cloud.product.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloud.product.entity.MmallAttributeKeyEntity;
import com.cloud.product.service.MmallAttributeKeyService;
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
 * @ClassName: MmallAttributeKeyController
 * @Description: TODO(这里用一句话描述这个类的作用)商品spu 属性key表
 * @date 2021-08-24
 */
@Controller
@Api(value = "商品spu 属性key表crud功能", tags = "")
@RequestMapping(value = "/prod/sku/attr/key")
public class MmallAttributeKeyController {
    @Autowired
    private MmallAttributeKeyService mmallAttributeKeyService;

    @RequestMapping("/tolist")
    public ModelAndView toList() {
        ModelAndView model = new ModelAndView();
        model.setViewName("/product/skuManager");
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
    public PageDataResult getPageList(Page<MmallAttributeKeyEntity> p) {
        PageDataResult pdr = new PageDataResult();
        QueryWrapper<MmallAttributeKeyEntity> wrapper = new QueryWrapper<MmallAttributeKeyEntity>();
        IPage<MmallAttributeKeyEntity> page = mmallAttributeKeyService.page(p, wrapper);
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
    public Result aOrU(MmallAttributeKeyEntity entity) {
        Result result = Result.getInstance();
        LambdaUpdateWrapper<MmallAttributeKeyEntity> wrapper = Wrappers.<MmallAttributeKeyEntity>lambdaUpdate();
        boolean b = mmallAttributeKeyService.saveOrUpdate(entity);
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
    public Result del(Integer makId) {
        Result result = Result.getInstance();
        boolean b = mmallAttributeKeyService.removeById(makId);
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
    public Result getById(Integer makId) {
        Result result = Result.getInstance();
        MmallAttributeKeyEntity obj = mmallAttributeKeyService.getById(makId);
        result.setData(obj);
        result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
        result.setMessage("获取成功!!!!!!");
        if (obj == null){
            result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
            result.setMessage("操作异常，请您稍后再试");
        }

        return result;
    }



    @RequestMapping("/getTemplate")
    @ResponseBody
    public Result getTemplate() {
        Result result = Result.getInstance();
        List<MmallAttributeKeyEntity> list = mmallAttributeKeyService.list();
        result.setData(list);
        result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
        result.setMessage("获取成功!!!!!!");
        if (list == null || list.size() == 0){
            result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
            result.setMessage("操作异常，请您稍后再试");
        }

        return result;
    }


    /**
     * 获取商品sku
     * @param productId
     * @return
     */
    @RequestMapping("/getProByPid")
    @ResponseBody
    public Result getProByPid(Integer productId) {
        Result result = Result.getInstance();
        List<MmallAttributeKeyEntity> list = mmallAttributeKeyService.getProByPid(productId);
        result.setData(list);
        result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
        result.setMessage("获取成功!!!!!!");
        if (list == null || list.size() == 0){
            result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
            result.setMessage("操作异常，请您稍后再试");
        }

        return result;
    }


    /**
     * 获取商品sku key value
     * @param productId
     * @return
     */
    @RequestMapping("/getKeyandVal")
    @ResponseBody
    public Result getKeyandVal(Integer productId) {
        Result result = Result.getInstance();
        Map map = mmallAttributeKeyService.getKeyandVal(productId);
        result.setData(map);
        result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
        result.setMessage("获取成功!!!!!!");
        if (map == null || map.size() == 0){
            result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
            result.setMessage("操作异常，请您稍后再试");
        }

        return result;
    }
}