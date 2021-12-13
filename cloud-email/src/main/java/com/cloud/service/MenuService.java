package com.cloud.service;


import com.alibaba.fastjson.JSONObject;

public interface MenuService {
    public JSONObject getMenu(String accessToken);
    public int createMenu(String accessToken);
    public int deleteMenu(String accessToken);
}
