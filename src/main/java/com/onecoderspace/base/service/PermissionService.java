package com.onecoderspace.base.service;

import org.springframework.data.domain.Sort;

import com.onecoderspace.base.component.common.service.BaseService;
import com.onecoderspace.base.domain.Permission;

/**
 *菜单及操作权限,树形结构
 */
public interface PermissionService extends BaseService<Permission, Integer>{

	Iterable<Permission> findAll(Sort sort);

	
}
