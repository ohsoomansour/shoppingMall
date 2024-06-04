package com.shoppingmall.login;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.shoppingmall.toaf.object.DataMap;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/login")
@Controller
public class LoginController {
	//private final Logger logger = LoggerFactory.getLogger(this.getClass()); // 부트는 기본 내장
	
	
	@Autowired
	private LoginService loginService;


	/*
	 *@Author:osm
	 *@Date: 2024.6.
	 *@Param: 
	 *@Return:
	 *@Function: 
	 * */ 
	@RequestMapping("/login.do")
	public ModelAndView doFormLogin(@ModelAttribute DataMap paramMap, HttpServletRequest request) {
		
		
		//24.6.4 세션 테스트 
		String sessionKey = request.getSession().getId();
		log.info("sessionKey:" + sessionKey);
		ModelAndView mav = new ModelAndView ("comm/login");
		
		return mav;
	}
}
