package com.cloud.sys.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloud.sys.entity.RoleDeptEntity;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 *
 * @ClassName: RoleDeptController
 * @Description: 角色部门数据权限
 * @author 小可爱
 * @date 2021-11-18
 */
public interface RoleDeptMapper extends BaseMapper<RoleDeptEntity> {

    IPage<Map> pageCustomize(Page p, @Param("ew") QueryWrapper<RoleDeptEntity> wrapper);
}