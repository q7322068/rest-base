package com.onecoderspace.base.dao;

import java.util.Set;

import com.onecoderspace.base.component.common.dao.BaseDao;
import com.onecoderspace.base.domain.Role;

public interface RoleDao  extends BaseDao<Role, Integer>{

	Role findByCode(String code);

	Set<Role> findByIdIn(Set<Integer> roleIds);

}
