package com.onecoderspace.base.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.onecoderspace.base.component.common.dao.BaseDao;
import com.onecoderspace.base.component.common.service.BaseServiceImpl;
import com.onecoderspace.base.dao.UserDao;
import com.onecoderspace.base.domain.User;
import com.onecoderspace.base.domain.UserRole;
import com.onecoderspace.base.service.UserRoleService;
import com.onecoderspace.base.service.UserService;
import com.onecoderspace.base.util.Constants;
import com.onecoderspace.base.util.LoginSessionHelper;
import com.onecoderspace.base.util.Return;
import com.onecoderspace.base.util.SaltMD5Util;

@Transactional
@Service("userService")
public class UserServiceImpl extends BaseServiceImpl<User, Integer> implements UserService{

	@Autowired
	private UserDao userDao;

	@Override
	public BaseDao<User, Integer> getDAO() {
		return userDao;
	}

	@Override
	public User findByUsername(String username) {
		return userDao.findByUsernameAndDel(username,Constants.DEL_NO);
	}
	
	@Override
	public Return changePwd(User entity, String pwd) {
		String salt = SaltMD5Util.getSalt();
		entity.setPwd(SaltMD5Util.encode(pwd, salt));
		entity.setSalt(salt);
		userDao.save(entity);
		return Return.success();
	}
	
	@Override
	public Page<User> listByPage(final Map<String, String> params,Pageable pageable){
		Specification<User> spec = new Specification<User>() {  
			@Override
			public Predicate toPredicate(Root<User> root,CriteriaQuery<?> query,CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();  
				String type = params.get("type");
				String status= params.get("status");
				String username = params.get("username");
				String name = params.get("name");
				
			    if(StringUtils.isNotBlank(type)){  
			        list.add(cb.equal(root.get("type").as(Integer.class), NumberUtils.toInt(type)));  
			    }  
			    if(StringUtils.isNotBlank(status)){  
			        list.add(cb.equal(root.get("status").as(Integer.class), NumberUtils.toInt(status)));  
			    }
			    if(StringUtils.isNotBlank(username)){  
			        list.add(cb.like(root.get("username").as(String.class), String.format("%%%s%%", username)));  
			    }  
			    if(StringUtils.isNotBlank(name)){  
			    	list.add(cb.like(root.get("name").as(String.class), String.format("%%%s%%", name)));   
			    }
			    list.add(cb.equal(root.get("del"), Constants.DEL_NO));
			    Predicate[] p = new Predicate[list.size()];  
			    return cb.and(list.toArray(p));  
				
				//in条件查询
				/*List<Integer> ids = Lists.newArrayList();
				ids.add(1);
				ids.add(2);
				In<Integer> in = cb.in(root.get("id").as(Integer.class));
				in.value(1);
				in.value(2);
			    return cb.or(in);*/
			}  
		};  
		Page<User> page = userDao.findAll(spec, pageable);
		return page;
	}
	
	@Override
	public Return doAudit(User entity, int value, String msg) {
		if(value == 1){
			entity.setStatus(User.STATUS_AUDIT_PASS);
		} else {
			entity.setStatus(User.STATUS_AUDIT_UNPASS);
		}
		int currentUid = LoginSessionHelper.getCurrentUserId();
		entity.setUpdator(currentUid);
		entity.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		userDao.save(entity);
		Pageable pageable = new PageRequest(0, 10, new Sort(Direction.DESC, "updateTime"));
		Page<User> page = userDao.findAll(pageable);
		
		return Return.success();
	}
	
	@Override
	public Return doSetRoles(int uid, String roleIds) {
		userRoleService.deleleByUserId(uid);
		String[] arr = roleIds.split(",");
		for (String roleId : arr) {
			UserRole entity = new UserRole();
			entity.setUserId(uid);
			entity.setRoleId(NumberUtils.toInt(roleId));
			userRoleService.save(entity);
		}
		return Return.success();
	}
	
	@Override
	public Return saveUser(User user) {
		this.userDao.save(user);
		
		String code = user.getType() == User.TYPE_OPERATOR ? Constants.ROLE_CODE_OPERATOR : Constants.ROLE_CODE_USER;
		this.userRoleService.add(user.getId(),code);
		
		return Return.success();
	}

	@Autowired
	private UserRoleService userRoleService;
	
}
