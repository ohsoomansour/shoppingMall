package com.shoppingmall.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@RequestMapping("/login")
@Controller
public class LoginController {

	@Autowired
	private LoginService loginService;


	/*
	 *@Author:osm
	 *@Date: 2024.5.27
	 *@Param: 
	 *@Return:
	 *@Function: 
	 * */ 
	@RequestMapping("/login.do")
	public ModelAndView doFormLogin() {
		ModelAndView mav = new ModelAndView ("comm/login");
		
		return mav;
	}
}
