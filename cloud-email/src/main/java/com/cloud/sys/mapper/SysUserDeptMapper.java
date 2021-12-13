package com.cloud.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloud.sys.entity.SysUserDeptEntity;

import java.util.List;

/**
 *
 * @ClassName: SysUserDeptController
 * @Description: 部门用户关联
 * @author 小可爱
 * @date 2021-10-11
 */
public interface SysUserDeptMapper extends BaseMapper<SysUserDeptEntity> {

    void batchUserDeptAorU(List<SysUserDeptEntity> list);
}