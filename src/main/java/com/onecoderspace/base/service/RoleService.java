package com.onecoderspace.base.service;

import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.onecoderspace.base.component.common.service.BaseService;
import com.onecoderspace.base.domain.Role;
import com.onecoderspace.base.util.Return;

/**
 *角色
 */
public interface RoleService extends BaseService<Role, Integer>{

	Page<Role> listByPage(String name, Pageable pageable);

	Return doSetPermissions(int roleId, String permissionIds);

	Role findByCode(String code);

	Set<Role> findByIds(Set<Integer> roleIds);
	
}
