package com.cloud.sys.service;


import com.cloud.sys.PermissionEntity;

import java.util.List;

public interface PermissionService {
    List<PermissionEntity> getUserPerms(Long id);
}
