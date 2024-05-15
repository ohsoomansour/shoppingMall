package com.shoppingmall;

<<<<<<< HEAD

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


//@EnableAutoConfiguration

=======
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


>>>>>>> 5bb0adc72111f24c9fcda0aa112156e834d4053b
@SpringBootApplication  //(scanBasePackages = {"com.shoppingmall"})
public class ShoppingMallApp {

	public static void main(String[] args) {
		SpringApplication.run(ShoppingMallApp.class, args);
<<<<<<< HEAD
		System.out.println("test...");
=======
		System.out.println("main!");
>>>>>>> 5bb0adc72111f24c9fcda0aa112156e834d4053b
	}
}
