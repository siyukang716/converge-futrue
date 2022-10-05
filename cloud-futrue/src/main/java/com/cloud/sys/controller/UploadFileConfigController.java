package com.cloud.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloud.sys.entity.UploadFileConfigEntity;
import com.cloud.sys.service.UploadFileConfigService;
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
 * @ClassName: UploadFileConfigController
 * @Description: TODO(这里用一句话描述这个类的作用)文件上传路径配置
 * @author 小可爱
 * @date 2021-09-23
 */
@Controller
@Api(value = "文件上传路径配置crud功能",tags = "文件上传路径配置crud功能")
@RequestMapping(value ="system/config")
public class UploadFileConfigController  {
    @Autowired
    private UploadFileConfigService uploadFileConfigService;

    @GetMapping("/tolist")
    public ModelAndView toList() {
        ModelAndView model = new ModelAndView();
        model.setViewName("auth/uploadfileconfig");
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
    public PageDataResult getPageList(Page<UploadFileConfigEntity> p) {
        PageDataResult pdr = new PageDataResult();
        QueryWrapper<UploadFileConfigEntity> wrapper = new QueryWrapper<UploadFileConfigEntity>();
        IPage<UploadFileConfigEntity> page = uploadFileConfigService.page(p,wrapper);
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
    public Result aOrU(UploadFileConfigEntity entity) {
        Result result = Result.getInstance();
        LambdaUpdateWrapper<UploadFileConfigEntity> wrapper = Wrappers.<UploadFileConfigEntity>lambdaUpdate();
        boolean b = uploadFileConfigService.saveOrUpdate(entity);
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
    public Result del(Long configId) {
        Result result = Result.getInstance();
        boolean b = uploadFileConfigService.removeById(configId);
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
    public Result getById(Long configId) {
        Result result = Result.getInstance();
        UploadFileConfigEntity obj = uploadFileConfigService.getById(configId);
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