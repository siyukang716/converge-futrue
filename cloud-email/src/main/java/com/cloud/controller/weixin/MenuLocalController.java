package com.cloud.controller.weixin;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloud.service.MenuSeviceImpl;
import com.cloud.thread.AccessTokenThread;
import com.cloud.util.PageDataResult;
import com.cloud.util.Result;
import com.cloud.entity.Menu;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * 对订阅号的菜单的操作
 *
 */
@RestController
@RequestMapping("/menu")
@Slf4j
public class MenuLocalController {
    @Autowired
    private MenuSeviceImpl menuService;


    //private static Logger log = LoggerFactory.getLogger(MenuController.class);
    /**
     * 本地维护菜单
     * @param
     */
    @RequestMapping(value = "/addMenu")
    public Result addMenu(Menu menu){
        Result result = Result.getInstance();
        if (menuService.save(menu))
            result.setMessage("菜单维护成功");
        else
            result.setMessage("菜单维护失败");
        log.info("==================addMenu=======================");
        return result;
    }

    /**
     * 本地删除菜单
     * @param id
     * @return
     */
    @RequestMapping(value = "/removeMenu")
    public Result removeMenu(String id){
        Result result = Result.getInstance();
        if (menuService.removeById(id))
            result.setMessage("删除成功");
        else
            result.setMessage("删除失败");
        return result;
    }
    @RequestMapping(value = "/updateMenu")
    public Result updateMenu(Menu menu){
        Result result = Result.getInstance();
        if (menuService.updateById(menu))
            result.setMessage("菜单更新成功");
        else
            result.setMessage("菜单更新失败");
        return result;
    }





    @PostMapping(value = "/getMenus")
    //@ApiOperation(value = "微信菜单列表的分页查询",httpMethod = "POST",produces = MediaType.APPLICATION_JSON_VALUE)
    public PageDataResult getPageList(Page<Menu> p,
                                   Menu menu) {
        PageDataResult pdr = new PageDataResult();
        try {
            QueryWrapper<Menu> wrapper = new QueryWrapper<>();
//            wrapper.like(!StringUtils.isNullOrEmpty(user.getUsername()),"user.username",user.getUsername());
//            wrapper.like(!StringUtils.isNullOrEmpty(user.getMobile()),"user.mobile",user.getMobile());
//            wrapper.apply(user.getInsertTime() != null ,"date_format(user.insert_time,'%Y-%m-%d') = date_format({0},'%Y-%m-%d')",user.getInsertTime());
            IPage<Menu> page = menuService.page(p, wrapper);
            pdr.setTotals((int)page.getTotal());
            pdr.setList(page.getRecords());
        }catch (Exception e){
            e.printStackTrace();
            pdr.setCode(500);
        }
        return pdr;
    }
    //查询全部菜单
    @RequestMapping(value = "/get",method = RequestMethod.GET)
    public String getMenu() {
        // 调用接口获取access_token
        String at = AccessTokenThread.accessToken.getToken();
        JSONObject jsonObject =null;
        if (at != null) {
            // 调用接口查询菜单
            jsonObject = menuService.getMenu(at);
            // 判断菜单创建结果
            return String.valueOf(jsonObject);
        }
        log.info("token为"+at);
        return "无数据";
    }

    //创建菜单
    @RequestMapping(value = "/create")
    public Result createMenu() {
        // 调用接口获取access_token
        String at = AccessTokenThread.accessToken.getToken();
        Result result = Result.getInstance();
        if (at != null) {

            // 调用接口创建菜单
            int stat = menuService.createMenu(at);
            // 判断菜单创建结果
            if (0 == stat) {
                result.setMessage("菜单创建成功！");
                log.info("菜单创建成功！");
            } else {
                result.setMessage("菜单创建失败，错误码：" );
                log.info("菜单创建失败，错误码：" + stat);
            }
        }
        return result ;
    }

    //删除菜单
    @RequestMapping(value = "/delete")
    public Result deleteMenu() {
        // 调用接口获取access_token
        Result result = Result.getInstance();
        String at = AccessTokenThread.accessToken.getToken();
        if (at != null) {
            // 删除菜单
            int status = menuService.deleteMenu(at);
            // 判断菜单删除结果
            if (0 == status) {
                log.info("菜单删除成功！");
                result.setMessage("菜单删除成功!");
                result.setStatus(200);
            } else {
                log.info("菜单删除失败，错误码：" + result);
                result.setStatus(500);
                result.setMessage("删除菜单失败");
            }
        }
        return  result;
    }
}

