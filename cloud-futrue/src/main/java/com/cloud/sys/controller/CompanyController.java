package com.cloud.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloud.sys.entity.CompanyEntity;
import com.cloud.sys.service.CompanyService;
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
 * @author 豆芽菜
 * @ClassName: CompanyController
 * @Description: TODO(这里用一句话描述这个类的作用)公司维护
 * @date 2021-11-15
 */
@Controller
@Api(value = "公司维护crud功能", tags = "公司维护")
@RequestMapping(value = "sys/company")
public class CompanyController {
    @Autowired
    private CompanyService companyService;

    @GetMapping("/tolist")
    public ModelAndView toList() {
        ModelAndView model = new ModelAndView();
        model.setViewName("auth/company");
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
    @ApiOperation(value = "列表查询", httpMethod = "GET")
    public PageDataResult getPageList(Page p) {
        PageDataResult pdr = new PageDataResult();
        QueryWrapper<CompanyEntity> wrapper = new QueryWrapper<CompanyEntity>();
        IPage<CompanyEntity> page = companyService.page(p, wrapper);
        pdr.setTotals((int) page.getTotal());
        pdr.setList(page.getRecords());
        return pdr;
    }


    /**
     * 增加
     *
     * @param
     * @return
     */
    @PostMapping("/aOrU")
    @ResponseBody
    public Result save(CompanyEntity entity,String userName,String password) {
       return companyService.saveCustomize(entity,userName,password);
    }
    /**
     * 删除
     *
     * @param
     * @return
     */
    @GetMapping("/del")
    @ResponseBody
    public Result del(Long companyId) {
        Result result = Result.getInstance();
        boolean b = companyService.removeById(companyId);
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
    public Result getById(Long companyId) {
        Result result = Result.getInstance();
        CompanyEntity obj = companyService.getById(companyId);
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