package com.cloud.sys.mapper;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloud.sys.UserEntity;
import org.apache.ibatis.annotations.Param;


public interface UserMapper extends BaseMapper<UserEntity> {

    Page<UserEntity> listUsers(Page<UserEntity> page, @Param(Constants.WRAPPER) Wrapper<UserEntity> wrapper);

    void recoverUser(@Param("id")Long id);
}
