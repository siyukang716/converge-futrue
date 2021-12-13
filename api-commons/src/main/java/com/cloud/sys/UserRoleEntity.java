package com.cloud.sys;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.ToString;

/**
 * 用户角色关联表
 */
@TableName("user_role")
@Data
@ToString
public class UserRoleEntity extends Model<UserRoleEntity> {
    private Long userId;
    private Long roleId;
}
