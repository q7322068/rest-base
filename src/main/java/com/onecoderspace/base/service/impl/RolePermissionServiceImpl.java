package com.onecoderspace.base.service.impl;

import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.onecoderspace.base.component.common.dao.BaseDao;
import com.onecoderspace.base.component.common.service.BaseServiceImpl;
import com.onecoderspace.base.dao.PermissionDao;
import com.onecoderspace.base.dao.RolePermissionDao;
import com.onecoderspace.base.domain.Permission;
import com.onecoderspace.base.domain.RolePermission;
import com.onecoderspace.base.service.RolePermissionService;

@Transactional
@Service("rolePermissionService")
public class RolePermissionServiceImpl extends BaseServiceImpl<RolePermission, Integer> implements RolePermissionService{

	@Autowired
	private RolePermissionDao rolePermissionDao;

	@Override
	public BaseDao<RolePermission, Integer> getDAO() {
		return rolePermissionDao;
	}

	@Override
	public List<RolePermission> findByRoleId(int roleId) {
		return this.rolePermissionDao.findByRoleId(roleId);
	}

	@Override
	public void deleleByRoleId(int roleId) {
		this.rolePermissionDao.deleleByRoleId(roleId);
	}
	
	@Override
	public List<Permission> getPermissions(Set<Integer> roles) {
		if(CollectionUtils.isEmpty(roles)){
			return Lists.newArrayList();
		}
		List<Permission> list = this.permissionDao.listByRoleIds(roles);
		return list;
	}

	@Autowired
	private PermissionDao permissionDao;
}
