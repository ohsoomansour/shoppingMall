package com.rb.admin.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;




@Controller
public class AdminController {
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	
	@GetMapping("/admin/member")
	public ModelAndView getMembers() {
		log.info("admin/member");
		ModelAndView mav = new ModelAndView();
		mav.setViewName("member");
		return mav;
	}
	
	
}
