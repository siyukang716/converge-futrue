package com.cloud.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.cloud.shiro.ShiroUtils;
import com.cloud.sys.UserEntity;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * mybatis-plus 自动填充组件
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    //private static final Logger LOGGER = LoggerFactory.getLogger(MyMetaObjectHandler.class);
    @Override
    public void insertFill(MetaObject metaObject) {
        boolean createTime = metaObject.hasSetter("createTime");//获取是否有该属性
        if (createTime)
            this.setInsertFieldValByName("createTime", new Date(),metaObject);
        boolean insertTime = metaObject.hasSetter("insertTime");//获取是否有该属性
        if (insertTime)
            this.setInsertFieldValByName("insertTime", new Date(),metaObject);
        boolean addTime = metaObject.hasSetter("addTime");//获取是否有该属性
        if (addTime)
            this.setInsertFieldValByName("addTime", new Date(),metaObject);
        boolean insertUid = metaObject.hasSetter("insertUid");
        if (insertUid){
            UserEntity principal = (UserEntity) SecurityUtils.getSubject().getPrincipal();
            this.setInsertFieldValByName("insertUid",principal.getId(),metaObject);
        }

        boolean isDel = metaObject.hasSetter("isDel");
        if (isDel){
            setFieldValByName("isDel", 1,metaObject);
        }

        boolean companyId = metaObject.hasSetter("companyId");
        if (companyId){
            setInsertFieldValByName("companyId", ShiroUtils.getLoginUser().getCompanyId(),metaObject);
        }

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        boolean updateTime = metaObject.hasSetter("updateTime");//获取是否有该属性
        if (updateTime)
            this.setUpdateFieldValByName("updateTime",new Date(),metaObject);
        boolean modifiedTime = metaObject.hasSetter("modifiedTime");//获取是否有该属性
        if (modifiedTime)
            this.setUpdateFieldValByName("modifiedTime", new Date(), metaObject);
        boolean updateUid = metaObject.hasSetter("updateUid");//获取是否有该属性
        if (updateUid)
            this.setUpdateFieldValByName("updateUid", ShiroUtils.getLoginUserId(), metaObject);
    }
}