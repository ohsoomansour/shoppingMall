package com.shoppingmall.jwt;


import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
/*** SecurityConfigurerAdapter 5.7 이후 deprecated ***
@RequiredArgsConstructor
public class JWTSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
	private final TokenProvider tokenProvider;
							
	@Override
	public void configure(HttpSecurity http) {
		// security 로직에 JwtFilter 등록
		http.addFilterBefore(
				(Filter) new JwtFilter(tokenProvider),
				UsernamePasswordAuthenticationFilter.class
		);
		
		
	}
	
	
}*/
