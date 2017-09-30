package com.onecoderspace.base.dao;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;

import com.onecoderspace.base.component.common.dao.BaseDao;
import com.onecoderspace.base.domain.Permission;

public interface PermissionDao extends BaseDao<Permission, Integer>{

	@Query(value="select perm.* from role_permission rp left join permission perm on rp.permission_id=perm.id where rp.role_id in(?1);",nativeQuery=true)
	List<Permission> listByRoleIds(Set<Integer> roles);

}
