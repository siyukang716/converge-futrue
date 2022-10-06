package com.cloud.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloud.sys.entity.SysCommonDictEntity;
import com.cloud.sys.service.SysCommonDictService;
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
                                
/**
 *
 * @ClassName: SysCommonDictController
 * @Description: TODO(这里用一句话描述这个类的作用)公共字典
 * @author 豆芽菜
 * @date 2021-10-12
 */
@Controller
@Api(value = "公共字典crud功能",tags = "公共字典")
@RequestMapping(value ="system/dict")
public class SysCommonDictController  {
    @Autowired
    private SysCommonDictService sysCommonDictService;

    @GetMapping("/tolist")
    public ModelAndView toList() {
        ModelAndView model = new ModelAndView();
        model.setViewName("auth/syscommondict");
        return model;
    }

    /**
     * 列表查询
     * @param p
     * @return
     */
    @GetMapping("/getList")
    @ResponseBody
    @ApiOperation(value = "列表查询",httpMethod = "GET",produces = MediaType.APPLICATION_JSON_VALUE)
    public PageDataResult getPageList(Page p) {
        PageDataResult pdr = new PageDataResult();
        QueryWrapper<SysCommonDictEntity> wrapper = new QueryWrapper<SysCommonDictEntity>();
        IPage<SysCommonDictEntity> page = sysCommonDictService.page(p,wrapper);
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
    public Result aOrU(SysCommonDictEntity entity) {
        return sysCommonDictService.saveOrUpdateLocal(entity);
    }

    /**
     * 删除地址信息
     * @param
     * @return
     */
    @GetMapping("/del")
    @ResponseBody
    public Result del(Long dictId) {
        Result result = Result.getInstance();
        boolean b = sysCommonDictService.removeById(dictId);
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
    public Result getById(Long dictId) {
        Result result = Result.getInstance();
        SysCommonDictEntity obj = sysCommonDictService.getById(dictId);
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