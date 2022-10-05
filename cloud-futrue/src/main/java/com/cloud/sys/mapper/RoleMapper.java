package com.cloud.sys.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloud.sys.RoleEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleMapper extends BaseMapper<RoleEntity> {

    /**
     * 关联表user_role 查询role 根据uid
     * @param uid
     * @return
     */
    List<RoleEntity> selectRolesByUid(@Param("uid")Long uid);

}
