package com.cloud.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName(value = "wx_menu")
@Data
public class Menu {
    @TableId(type = IdType.ID_WORKER_STR)
    private String id;
    /**  账户id*/
    private String accountId;
    /** 父级id*/
    private String parentId;
    /**父级*/
    private String parentName;
    /** 菜单名称*/
    private String name;
    /** 菜单类型
     * 菜单的响应动作类型，view表示网页类型，click表示点击类型，miniprogram表示小程序类型
     * */
    private String type;
    /**
     *
     * 菜单KEY值，用于消息接口推送，不超过128字节
     *click等点击类型必须
     */
    @TableField(value = "`key`")
    private String key;
    /**
     *网页 链接，用户点击菜单可打开链接，不超过1024字节。
     *  type为miniprogram时，不支持小程序的老版本客户端将打开本url。
     */
    private String url;
    /**
     *  排序
     */
    private Integer sort;
}
