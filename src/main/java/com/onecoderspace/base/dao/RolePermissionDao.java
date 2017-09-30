package com.onecoderspace.base.dao;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.onecoderspace.base.component.common.dao.BaseDao;
import com.onecoderspace.base.domain.Permission;
import com.onecoderspace.base.domain.RolePermission;

public interface RolePermissionDao  extends BaseDao<RolePermission, Integer>{

	List<RolePermission> findByRoleId(int roleId);

	@Transactional
	@Query(value = "delete from role_permission where role_id=?1 ", nativeQuery = true)  
	@Modifying
	void deleleByRoleId(int roleId);

	List<RolePermission> findByRoleIdIn(Set<Integer> roles);

	@Query(value="select perm.* from role_permission rp left join permission perm on rp.permission_id=perm.id where rp.role_id in(?1);",nativeQuery=true)
	List<Permission> listByRoleIds(Set<Integer> roles);

}
