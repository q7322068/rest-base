package com.onecoderspace.base.service;

import java.util.List;
import java.util.Set;

import com.onecoderspace.base.component.common.service.BaseService;
import com.onecoderspace.base.domain.UserRole;

/**
 *用户角色
 */
public interface UserRoleService extends BaseService<UserRole, Integer>{

	List<UserRole> findByUserId(int uid);

	void deleleByUserId(int uid);

	/**
	 * 添加角色
	 * @author yangwk
	 * @time 2017年7月28日 下午2:12:35
	 * @param userId 用户ID
	 * @param code 角色编码
	 */
	void add(int id, String code);

	
	Set<Integer> findRoleIds(int userId);
	
}
