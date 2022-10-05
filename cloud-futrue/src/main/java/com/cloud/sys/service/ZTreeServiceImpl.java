package com.cloud.sys.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cloud.shiro.ShiroUtils;
import com.cloud.sys.mapper.ZTreeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ZTreeServiceImpl {
    @Autowired
    ZTreeMapper zTreeMapper;

    /**
     * 查询角色树
     * @param pId
     * @return
     */
    public List<Map<String,Object>> getRole(Long pId) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("a.is_del",1);
        wrapper.eq(null == pId || 0 == pId,"a.pId",0);
        wrapper.eq(null != pId && 0 != pId,"a.pId",pId);
        wrapper.eq("a.company_id", ShiroUtils.getLoginUser().getCompanyId());
        return zTreeMapper.getRole(wrapper);
    }

    /**
     * 查询菜单树
     * @param pId
     * @return
     */
    public List<Map<String,Object>> getPermTree(Long pId) {
        return  zTreeMapper.getPermTree(pId);
    }

    /**
     * 角色菜单授权管理  树
     * @param roleId
     * @param id
     * @return
     */
    public List<Map<String,Object>> getRoleTermTree(Long roleId, Long id) {
        return zTreeMapper.getRoleTermTree(roleId,id);
    }

    public List<Map<String,Object>> getAllPermTree(Long id) {
        return zTreeMapper.getAllPermTree(id);
    }
}
