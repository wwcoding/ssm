package com.coder.dao;

import com.coder.domain.User;

public interface UserDao {
	public User selectUserById(Integer id) throws Exception;
	
	public User selectUserByUsername(String userName) throws Exception;

}
