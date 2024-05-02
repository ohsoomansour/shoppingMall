package com.rb.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
	/*
	@GetMapping("index")
	public String index() {
		return "index Test";
	}
	
	@GetMapping(path = "/member")
	public ModelAndView getMembers() {
		ModelAndView mav = new ModelAndView("member");	
		
		return mav;
	}*/
	
}
