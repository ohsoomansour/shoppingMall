package com.shoppingmall.config;

import java.util.function.Function;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class SpringSecurityConfiguration {
	
	/**
	 * @InMemoryUserDetailsManager: 사용자 인증 정보를 메모리 내에서 관리하는 데 사용되는 클래스
	 * 
	 * */
	@Bean
	public InMemoryUserDetailsManager CreateDetailManager() {
		Function<String, String> passwordEncoder
			= input -> passwordEncoder().encode(input);
	}
	/**
	 * @PasswordEncoder: 
	 * @BCryptPasswordEncoder:
	 * */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
