package com.shoppingmall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.shoppingmall.basemvc.BaseDao;



//@ComponentScan(basePackages = {"com.shoppingmall"} )
//@EnableAutoConfiguration
@SpringBootApplication(scanBasePackages = {"com.shoppingmall.baseDao"})
public class ShoppingMallApp {

	public static void main(String[] args) {
		SpringApplication.run(ShoppingMallApp.class, args);
		System.out.println("test...");
	}

	
}
