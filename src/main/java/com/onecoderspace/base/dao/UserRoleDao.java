package com.onecoderspace.base.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.onecoderspace.base.component.common.dao.BaseDao;
import com.onecoderspace.base.domain.UserRole;

public interface UserRoleDao  extends BaseDao<UserRole, Integer>{

	List<UserRole> findByUserId(int uid);

	@Transactional
	@Query(value = "delete from user_role where user_id=?1 ", nativeQuery = true)  
	@Modifying
	void deleleByUserId(int uid);

}
