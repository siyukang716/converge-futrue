package com.cloud.sys.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ZTreeMapper {
    /**
     * 查询角色树
     * @return
     */
    List<Map<String,Object>> getRole(@Param("ew") QueryWrapper wrapper);

    /**
     * 查询菜单树
     * @param pId
     * @return
     */
    List<Map<String,Object>> getPermTree(@Param("pId") Long pId);
    List<Map<String,Object>> getAllPermTree(@Param("pId") Long pId);

    /**
     *
     * @param roleId
     * @param pId
     * @return
     */
    List<Map<String,Object>> getRoleTermTree(@Param("roleId") Long roleId, @Param("pId") Long pId);


}
