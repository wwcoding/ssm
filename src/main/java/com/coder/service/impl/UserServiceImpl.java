package com.coder.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coder.dao.UserDao;
import com.coder.domain.User;
import com.coder.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;
	@Override
	public User selectUserById(Integer id) throws Exception {
		return userDao.selectUserById(id);
	}

}
