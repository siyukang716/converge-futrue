package com.cloud.sys.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloud.sys.PermissionEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


public interface PermissionMapper extends BaseMapper<PermissionEntity> {
    /**
     * 关联表role_permission 查询permission 根据 roleid
     * @param roleid
     * @return
     */
    List<PermissionEntity> selectPermissionByUid(@Param("roleid")Long roleid);

    List<PermissionEntity> getUserPerms(@Param("userId") Long userId);

    List<Map<String,Object>> getMapUserPerms(@Param("userId")Long loginUserId);

    List<Map<String, Object>> getTermTreeBycompanyId(@Param("ew")QueryWrapper wrapper);
}
