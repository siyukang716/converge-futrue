package com.cloud.sys.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloud.sys.SysDeptEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 *
 * @ClassName: SysDeptController
 * @Description: 部门
 * @author
 * @date 2021-09-20
 */
public interface SysDeptMapper extends BaseMapper<SysDeptEntity> {


    List<Map> treeList(@Param("ew") LambdaQueryWrapper<SysDeptEntity> lambda);
}