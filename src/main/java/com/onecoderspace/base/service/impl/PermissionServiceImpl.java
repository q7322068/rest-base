package com.onecoderspace.base.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.onecoderspace.base.component.common.dao.BaseDao;
import com.onecoderspace.base.component.common.service.BaseServiceImpl;
import com.onecoderspace.base.dao.PermissionDao;
import com.onecoderspace.base.domain.Permission;
import com.onecoderspace.base.service.PermissionService;

@Transactional
@Service("permissionService")
public class PermissionServiceImpl extends BaseServiceImpl<Permission, Integer> implements PermissionService{

	@Autowired
	private PermissionDao permissionDao;

	@Override
	public BaseDao<Permission, Integer> getDAO() {
		return permissionDao;
	}

	@Override
	public Iterable<Permission> findAll(Sort sort) {
		return this.permissionDao.findAll(sort);
	}

}
