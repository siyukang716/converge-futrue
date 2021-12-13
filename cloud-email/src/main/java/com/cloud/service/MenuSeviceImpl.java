package com.cloud.service;


import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.entity.Menu;
import com.cloud.mapper.wx.MenuMapper;
import com.cloud.util.WeixinUtil;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 对订阅号的菜单的操作
 */
@SuppressWarnings("SpringJavaAutowiringInspection")
@Service("menuService")
@Slf4j
public class MenuSeviceImpl extends ServiceImpl<MenuMapper, Menu>  implements MenuService {



    // 菜单创建（POST） 限1000（次/天）
    public static String menu_create_url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";

    // 菜单查询（POST） 限10000（次/天）
    public static String menu_get_url = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=ACCESS_TOKEN";

    // 菜单删除（POST） 限1000（次/天）
    public static String menu_delete_url = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";
    /**
     * 查询菜单
     *
     * @param accessToken 有效的access_token
     * @return
     */
    public JSONObject getMenu(String accessToken) {

        // 拼装创建菜单的url
        String url = menu_get_url.replace("ACCESS_TOKEN", accessToken);
        // 调用接口查询菜单
        JSONObject jsonObject = WeixinUtil.httpRequest(url, "GET", null);

        return jsonObject;
    }
    /**
     * 创建菜单(替换旧菜单)
     *
     * @param accessToken 有效的access_token
     * @return 0表示成功，其他值表示失败
     */
    public int createMenu(String accessToken) {
        int result = 0;

        // 拼装创建菜单的url
        String url = menu_create_url.replace("ACCESS_TOKEN", accessToken);
        // 将菜单对象转换成json字符串
        String jsonMenu = JSONObject.toJSONString(getFirstMenu());
        // 调用接口创建菜单
        JSONObject jsonObject = WeixinUtil.httpRequest(url, "POST", jsonMenu);

        if (null != jsonObject) {
            if (0 != jsonObject.getInteger("errcode")) {
                result = jsonObject.getInteger("errcode");
                log.error("创建菜单失败 errcode:{} errmsg:{}", jsonObject.getInteger("errcode"), jsonObject.getString("errmsg"));
                log.error("****"+jsonMenu+"****");
            }
        }
        return result;
    }

    /**
     * 删除菜单
     *
     * @param accessToken 有效的access_token
     * @return 0表示成功，其他值表示失败
     */
    public int deleteMenu(String accessToken) {
        int result = 0;

        // 拼装创建菜单的url
        String url = menu_delete_url.replace("ACCESS_TOKEN", accessToken);

        // 调用接口创建菜单
        JSONObject jsonObject = WeixinUtil.httpRequest(url, "POST", null);

        if (null != jsonObject) {
            if (0 != jsonObject.getInteger("errcode")) {
                result = jsonObject.getInteger("errcode");
                log.error("删除菜单失败 errcode:{} errmsg:{}", jsonObject.getInteger("errcode"), jsonObject.getString("errmsg"));
            }
        }
        return result;
    }

    /**
     * 组装菜单
     * @return
     */
    private Map<String, Object> assemble_the_menu(Menu menu){
        Map<String, Object> menuMap = new HashMap<String, Object>();
        if (StrUtil.hasBlank(menu.getType())){
            menuMap.put("name",menu.getName());
            LambdaQueryWrapper<Menu> lambda = new LambdaQueryWrapper<>();
            lambda.eq(Menu::getParentId,menu.getId());
            List<Menu> list = this.list(lambda);
            if (list.size() > 0 ){
                List<Map<String, Object>> subMenuMapList = new ArrayList<Map<String, Object>>();
                list.forEach(obj->{
                    Map<String, Object> menu2Map = new HashMap<String, Object>();
                    menu2Map.put("name",obj.getName());
                    menu2Map.put("type",obj.getType());
                    menu2Map.put("key",obj.getKey());
                    menu2Map.put("url",obj.getUrl());
                    subMenuMapList.add(menu2Map);
//                    menu2Map.put("media_id","");
//                    menu2Map.put("appid","");
//                    menu2Map.put("pagepath","");
                });
                menuMap.put("sub_button",subMenuMapList);
            }
        }else {
            menuMap.put("name",menu.getName());
            menuMap.put("type",menu.getType());
            menuMap.put("key",menu.getKey());
            menuMap.put("url",menu.getUrl());
        }
        return menuMap;
    }


    /**
     * 组装菜单数据
     */
    public Map<String, Object> getFirstMenu(){
        //最外一层大括号
        Map<String, Object> wechatMenuMap = new HashMap<String, Object>();
        //包装button的List
        List<Map<String, Object>> wechatMenuMapList = new ArrayList<Map<String, Object>>();

        LambdaQueryWrapper<Menu> lambda = new LambdaQueryWrapper<>();
        lambda.eq(Menu::getParentId,0);
        List<Menu> list = this.list(lambda);
        list.forEach(obj->{
            Map<String, Object> menu = assemble_the_menu(obj);
            wechatMenuMapList.add(menu);
        });
        wechatMenuMap.put("button",wechatMenuMapList);
        return  wechatMenuMap;
    }
}
