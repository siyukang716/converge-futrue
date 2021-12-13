package com.cloud.product.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloud.product.entity.BrandInfoEntity;
import com.cloud.product.service.BrandInfoService;
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
 * @ClassName: BrandInfoController
 * @Description: TODO(这里用一句话描述这个类的作用)品牌信息表
 * @date 2021-08-05
 */
@Controller
@Api(value = "品牌信息表crud功能", tags = "BrandInfoController")
@RequestMapping(value = "/brand/info")
public class BrandInfoController {
    @Autowired
    private BrandInfoService brandInfoService;
    @RequestMapping("/tolist")
    public ModelAndView toList() {
        ModelAndView model = new ModelAndView();
        model.setViewName("product/brandinfo");
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
    public PageDataResult getPageList(Page<BrandInfoEntity> p) {
        PageDataResult pdr = new PageDataResult();
        try {
            QueryWrapper<BrandInfoEntity> wrapper = new QueryWrapper<>();
            IPage<BrandInfoEntity> page = brandInfoService.page(p, wrapper);
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
    public Result aOrU(BrandInfoEntity caentity) {
        Result result = Result.getInstance();
        try {
            //LambdaUpdateWrapper<BrandInfoEntity> wrapper = Wrappers.<BrandInfoEntity>lambdaUpdate();
            brandInfoService.saveOrUpdate(caentity);
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
     * @param brandId
     * @return
     */
    @RequestMapping("/del")
    @ResponseBody
    public Result del(Integer brandId) {
        Result result = Result.getInstance();
        try {
            LambdaUpdateWrapper<BrandInfoEntity> wrapper = Wrappers.<BrandInfoEntity>lambdaUpdate();
            wrapper.eq(BrandInfoEntity::getBrandId,brandId).set(BrandInfoEntity::getBrandStatus,0);
            brandInfoService.update(wrapper);
            result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
            result.setMessage("删除成功!!!!!!");
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
            result.setMessage("操作异常，请您稍后再试");
        }
        return result;
    }
    @RequestMapping("/statusSwitch")
    @ResponseBody
    public Result statusSwitch(Integer brandId,Integer brandStatus) {
        Result result = Result.getInstance();
        try {
            LambdaUpdateWrapper<BrandInfoEntity> wrapper = Wrappers.<BrandInfoEntity>lambdaUpdate();
            wrapper.eq(BrandInfoEntity::getBrandId,brandId).set(BrandInfoEntity::getBrandStatus,brandStatus);
            brandInfoService.update(wrapper);
            result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
            result.setMessage("修改成功!!!!!!");
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
            result.setMessage("操作异常，请您稍后再试");
        }
        return result;
    }
    @RequestMapping("/getById")
    @ResponseBody
    public Result getById(Integer brandId) {
        Result result = Result.getInstance();
        try {
            BrandInfoEntity obj = brandInfoService.getById(brandId);
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

    /**
     * 下拉框
     * @return
     */
    @RequestMapping("/linkage")
    @ResponseBody
    public Result linkage() {
        Result result = Result.getInstance();
        try {
            LambdaQueryWrapper<BrandInfoEntity> lambda = new QueryWrapper<BrandInfoEntity>().lambda();
            lambda.select(BrandInfoEntity :: getBrandId,BrandInfoEntity::getBrandName)
                    .orderBy(true,true,BrandInfoEntity::getBrandOrder);
            List<Map<String, Object>> maps = brandInfoService.listMaps(lambda);
            result.setData(maps);
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