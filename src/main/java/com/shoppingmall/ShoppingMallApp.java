package com.shoppingmall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication  
public class ShoppingMallApp {

	public static void main(String[] args) {
		SpringApplication.run(ShoppingMallApp.class, args);
		System.out.println("main!");
		/*
		int age = 1;
		String content = "testyo";
		float floatValue = 3.14159f;
        double doubleValue = 3.141592653589793;
		System.out.printf("Age: %d, Content: %s", age, content);
		System.out.println();
		// 소수점 이하 2자리까지 표시
        System.out.printf("Formatted float value: %.2f%n", floatValue);
        System.out.printf("Formatted double value: %.2f%n", doubleValue);
        
        // 소수점 이하 5자리까지 표시
        System.out.printf("Formatted float value: %.5f%n", floatValue);
        System.out.printf("Formatted double value: %.5f%n", doubleValue);
		*/
	}
}
