package com.cloud.controller.weixin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloud.util.PageDataResult;
import com.cloud.service.weixin.WX_UserService;
import com.spring.commons.entity.weixin.entity.W_UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping(value = "/w/user")
public class WX_UserController {
    @Autowired
    private WX_UserService userService;

    /**
     * 获取用户列表
     */
    @RequestMapping(value = "/remote/users")
    public void getPageList(){
        userService.getPageList();
    }

    /**
     * 获取用户信息
     */
    @RequestMapping(value = "/get")
    public void getUser(String openid){
        userService.getUser(openid);
    }


    @PostMapping(value = "/users")
    //@ApiOperation(value = "微信菜单列表的分页查询",httpMethod = "POST",produces = MediaType.APPLICATION_JSON_VALUE)
    public PageDataResult getPageList(Page<W_UserEntity> p,
                                   W_UserEntity menu) {
        PageDataResult pdr = new PageDataResult();
        try {
            QueryWrapper<W_UserEntity> wrapper = new QueryWrapper<>();
            IPage<W_UserEntity> page = userService.page(p, wrapper);
            pdr.setTotals((int)page.getTotal());
            pdr.setList(page.getRecords());
        }catch (Exception e){
            e.printStackTrace();
            pdr.setCode(500);
        }
        return pdr;
    }


}
