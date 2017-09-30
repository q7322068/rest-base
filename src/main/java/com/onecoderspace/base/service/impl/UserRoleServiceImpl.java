package com.onecoderspace.base.service.impl;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Sets;
import com.onecoderspace.base.component.common.dao.BaseDao;
import com.onecoderspace.base.component.common.service.BaseServiceImpl;
import com.onecoderspace.base.dao.UserRoleDao;
import com.onecoderspace.base.domain.Role;
import com.onecoderspace.base.domain.UserRole;
import com.onecoderspace.base.service.RoleService;
import com.onecoderspace.base.service.UserRoleService;

@Transactional
@Service("userRoleService")
public class UserRoleServiceImpl extends BaseServiceImpl<UserRole, Integer> implements UserRoleService{

	@Autowired
	private UserRoleDao userRoleDao;

	@Override
	public BaseDao<UserRole, Integer> getDAO() {
		return userRoleDao;
	}

	@Override
	public List<UserRole> findByUserId(int uid) {
		return userRoleDao.findByUserId(uid);
	}

	@Override
	public void deleleByUserId(int uid) {
		userRoleDao.deleleByUserId(uid);
	}
	
	@Override
	public void add(int userId, String code) {
		Role role = this.roleService.findByCode(code);
		if(role == null){
			return ;
		}
		UserRole userRole = new UserRole();
		userRole.setUserId(userId);
		userRole.setRoleId(role.getId());
		this.userRoleDao.save(userRole);
	}
	
	@Override
	public Set<Integer> findRoleIds(int userId) {
		List<UserRole> list = this.userRoleDao.findByUserId(userId);
		Set<Integer> ids = Sets.newHashSet();
		for(UserRole entity : list){
			ids.add(entity.getRoleId());
		}
		return ids;
	}

	@Autowired
	private RoleService roleService;
}
