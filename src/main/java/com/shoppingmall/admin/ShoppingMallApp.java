package com.shoppingmall.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@SpringBootApplication
public class ShoppingMallApp {

	public static void main(String[] args) {
		SpringApplication.run(ShoppingMallApp.class, args);
	}

	
}
