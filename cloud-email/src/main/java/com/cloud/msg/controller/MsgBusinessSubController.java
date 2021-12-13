package com.cloud.msg.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloud.msg.entity.MsgBusinessSubEntity;
import com.cloud.msg.service.MsgBusinessSubService;
import com.cloud.util.IStatusMessage;
import com.cloud.util.PageDataResult;
import com.cloud.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
                        
/**
 *
 * @ClassName: MsgBusinessSubController
 * @Description: TODO(这里用一句话描述这个类的作用)消息订阅表
 * @author 小可爱
 * @date 2021-10-14
 */
@Controller
@Api(value = "消息订阅表crud功能",tags = "消息订阅表")
@RequestMapping(value ="system/sub")
public class MsgBusinessSubController  {
    @Autowired
    private MsgBusinessSubService msgBusinessSubService;

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
        QueryWrapper<MsgBusinessSubEntity> wrapper = new QueryWrapper<MsgBusinessSubEntity>();
        IPage<MsgBusinessSubEntity> page = msgBusinessSubService.page(p,wrapper);
        pdr.setTotals((int)page.getTotal());
        pdr.setList(page.getRecords());
        return pdr;
    }


    /**
     * 增加或修改地址信息
     * @param
     * @return
     */
    @PostMapping("/aOrU")
    @ResponseBody
    public Result aOrU(MsgBusinessSubEntity entity) {
        Result result = Result.getInstance();
        LambdaUpdateWrapper<MsgBusinessSubEntity> wrapper = Wrappers.<MsgBusinessSubEntity>lambdaUpdate();
        boolean b = msgBusinessSubService.saveOrUpdate(entity);
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
     * @param
     * @return
     */
    @GetMapping("/del")
    @ResponseBody
    public Result del(Long msgLinkId) {
        Result result = Result.getInstance();
        boolean b = msgBusinessSubService.removeById(msgLinkId);
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
    public Result getById(Long msgLinkId) {
        Result result = Result.getInstance();
        MsgBusinessSubEntity obj = msgBusinessSubService.getById(msgLinkId);
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