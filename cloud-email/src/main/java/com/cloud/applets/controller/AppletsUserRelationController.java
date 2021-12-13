package com.cloud.applets.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloud.util.IStatusMessage;
import com.cloud.util.PageDataResult;
import com.cloud.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.cloud.applets.entity.AppletsUserRelationEntity;
import com.cloud.applets.service.AppletsUserRelationService;
                    
/**
 *
 * @ClassName: AppletsUserRelationController
 * @Description: TODO(这里用一句话描述这个类的作用)  小程序系统用户关联
 * @author 小可爱
 * @date 2021-11-13
 */
@Controller
@Api(value = "  小程序系统用户关联crud功能",tags = "  小程序系统用户关联")
@RequestMapping(value ="sys_applets_user_relation")
public class AppletsUserRelationController  {
    @Autowired
    private AppletsUserRelationService appletsUserRelationService;

    @GetMapping("/tolist")
    public ModelAndView toList() {
        ModelAndView model = new ModelAndView();
        model.setViewName("");
        return model;
    }

    /**
     * 列表查询
     * @param p
     * @return
     */
    @GetMapping("/getList")
    @ResponseBody
    @ApiOperation(value = "列表查询",httpMethod = "GET")
    public PageDataResult getPageList(Page p) {
        PageDataResult pdr = new PageDataResult();
        QueryWrapper<AppletsUserRelationEntity> wrapper = new QueryWrapper<AppletsUserRelationEntity>();
        IPage<AppletsUserRelationEntity> page = appletsUserRelationService.page(p,wrapper);
        pdr.setTotals((int)page.getTotal());
        pdr.setList(page.getRecords());
        return pdr;
    }


    /**
     * 增加或修改
     * @param
     * @return
     */
    @PostMapping("/aOrU")
    @ResponseBody
    public Result aOrU(AppletsUserRelationEntity entity) {
        Result result = Result.getInstance();
        LambdaUpdateWrapper<AppletsUserRelationEntity> wrapper = Wrappers.<AppletsUserRelationEntity>lambdaUpdate();
        boolean b = appletsUserRelationService.saveOrUpdate(entity);
        result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
        result.setMessage("完成信息完善!!!!!!");
        if (!b) {
        result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
        result.setMessage("操作异常，请您稍后再试");
        }
        return result;
    }

    /**
     * 删除
     * @param
     * @return
     */
    @GetMapping("/del")
    @ResponseBody
    public Result del(Long linkUserId) {
        Result result = Result.getInstance();
        boolean b = appletsUserRelationService.removeById(linkUserId);
        result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
        result.setMessage("删除成功!!!!!!");
        if (!b) {
        result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
        result.setMessage("操作异常，请您稍后再试");
        }
        return result;
    }

    @GetMapping("/getById")
    @ResponseBody
    public Result getById(Long linkUserId) {
        Result result = Result.getInstance();
        AppletsUserRelationEntity obj = appletsUserRelationService.getById(linkUserId);
        result.setData(obj);
        result.setStatus(IStatusMessage.SystemStatus.SUCCESS.getCode());
        result.setMessage("获取成功!!!!!!");
        if (obj == null){
        result.setStatus(IStatusMessage.SystemStatus.ERROR.getCode());
        result.setMessage("操作异常，请您稍后再试");
        }
        return result;
    }
}