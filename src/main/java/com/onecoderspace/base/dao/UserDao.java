package com.onecoderspace.base.dao;

import com.onecoderspace.base.component.common.dao.BaseDao;
import com.onecoderspace.base.domain.User;

public interface UserDao  extends BaseDao<User, Integer>{
	User findByUsernameAndDel(String username, int del);
}
