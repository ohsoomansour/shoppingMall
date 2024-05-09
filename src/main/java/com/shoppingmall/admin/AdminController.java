package com.shoppingmall.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.servlet.ModelAndView;

import com.shoppingmall.service.AdminServiceImpl;


@RequestMapping("/admin")
@Controller
public class AdminController {
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private AdminServiceImpl adminServiceImpl;
	
	@GetMapping("/memberList.do")
	public ModelAndView getMembers() {
		log.info("admin/memberList");
		ModelAndView mav = new ModelAndView();
		mav.setViewName("member");
		System.out.println(this.adminServiceImpl.getMemberList());
		mav.addObject("members", this.adminServiceImpl.getMemberList());
		return mav;
	}
	
}
