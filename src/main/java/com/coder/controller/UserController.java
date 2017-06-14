package com.coder.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.coder.domain.User;
import com.coder.service.UserService;

@Controller
public class UserController {

	@Resource
	private UserService userService;
	
	@RequestMapping("/hello")
	public ModelAndView getIndex() throws Exception{
		ModelAndView mav = new ModelAndView("index");
		User user = userService.selectUserById(2);
		mav.addObject("user",user);
		return mav;
	}
}
