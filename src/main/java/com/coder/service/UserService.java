package com.coder.service;

import com.coder.domain.User;

public interface UserService {

	User selectUserById(Integer id) throws Exception;
}
