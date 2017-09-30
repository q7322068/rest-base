package com.onecoderspace.base.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Sets;
import com.onecoderspace.base.component.common.dao.BaseDao;
import com.onecoderspace.base.component.common.service.BaseServiceImpl;
import com.onecoderspace.base.dao.RoleDao;
import com.onecoderspace.base.domain.Role;
import com.onecoderspace.base.domain.RolePermission;
import com.onecoderspace.base.service.RolePermissionService;
import com.onecoderspace.base.service.RoleService;
import com.onecoderspace.base.util.Return;

@Transactional
@Service("roleService")
public class RoleServiceImpl extends BaseServiceImpl<Role, Integer> implements RoleService{

	@Autowired
	private RoleDao roleDao;

	@Override
	public BaseDao<Role, Integer> getDAO() {
		return roleDao;
	}

	@Override
	public Page<Role> listByPage(final String name, Pageable pageable) {
		Specification<Role> spec = new Specification<Role>() {  
			@Override
			public Predicate toPredicate(Root<Role> root,CriteriaQuery<?> query,CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();  
			    if(StringUtils.isNotBlank(name)){  
			    	list.add(cb.like(root.get("name").as(String.class), String.format("%%%s%%", name)));   
			    }
			    Predicate[] p = new Predicate[list.size()];  
			    return cb.and(list.toArray(p));  
			}  
		};  
		Page<Role> page = roleDao.findAll(spec, pageable);
		return page;
	}

	@Override
	public Return doSetPermissions(int roleId, String permissionIds) {
		rolePermissionService.deleleByRoleId(roleId);
		String[] arr = permissionIds.split(",");
		for (String permissionId : arr) {
			RolePermission entity = new RolePermission();
			entity.setRoleId(roleId);
			entity.setPermissionId(NumberUtils.toInt(permissionId));
			rolePermissionService.save(entity);
		}
		return Return.success();
	}
	
	@Override
	public Role findByCode(String code) {
		return this.roleDao.findByCode(code);
	}
	
	@Override
	public Set<Role> findByIds(Set<Integer> roleIds) {
		if(CollectionUtils.isEmpty(roleIds)){
			return Sets.newHashSet();
		}
		return this.roleDao.findByIdIn(roleIds);
	}
	
	@Autowired
	private RolePermissionService rolePermissionService;
}
