package com.cloud.sys;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 菜单角色关联表
 */
@Data
@TableName("role_permission")
public class RolePermissionEntity {
    private Long permitId;
    private Long roleId;

    /**
     * 公司主键
     */
    @TableField("company_id")
    @ApiModelProperty(value = "公司主键")
    private Long companyId;
}
