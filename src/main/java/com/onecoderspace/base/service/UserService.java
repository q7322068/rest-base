package com.onecoderspace.base.service;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.onecoderspace.base.component.common.service.BaseService;
import com.onecoderspace.base.domain.User;
import com.onecoderspace.base.util.Return;

/**
 *用户
 */
public interface UserService extends BaseService<User, Integer>{

	User findByUsername(String username);

	/**
	 * 设置密码
	 * @author yangwk
	 * @time 2017年7月27日 上午10:15:12
	 * @param entity
	 * @param pwd
	 * @return
	 */
	Return changePwd(User entity, String pwd);
	
	/**
	 * 带条件分页查询
	 * @author yangwk
	 * @time 2017年7月27日 上午10:15:26
	 * @param params
	 * @param pageable
	 * @return
	 */
	Page<User> listByPage(Map<String, String> params,Pageable pageable);

	/**
	 * 保存审核结果
	 * @author yangwk
	 * @time 2017年7月27日 上午10:14:11
	 * @param entity 素材新
	 * @param value 审核结果 1通过 0未通过
	 * @param msg 备注新
	 * @return
	 */
	Return doAudit(User entity, int value, String msg);

	/**
	 * 设置用户的角色
	 * @author yangwk
	 * @time 2017年7月27日 上午10:15:01
	 * @param uid
	 * @param roleIds
	 * @return
	 */
	Return doSetRoles(int uid, String roleIds);

	/**
	 * 保存用户
	 * @author yangwk
	 * @time 2017年7月28日 下午1:37:24
	 * @param user 用户信息
	 * @param company 公司信息
	 * @return
	 */
	Return saveUser(User user);
	
}
