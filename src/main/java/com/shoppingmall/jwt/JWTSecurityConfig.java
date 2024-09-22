package com.shoppingmall.jwt;


import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JWTSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
	private final TokenProvider tokenProvider;
	/**
	 *@Description : security로직에 JwtFilter 등록
	 *@UsernamePasswordAuthenticationFilter : 스프링 시큐리티에서 폼 기반 로그인 과정을 처리하는 필터. 로그인 정보를 제출하면이 필터가 해당 정보를 가로채
	 *  - 인증 과정을 진행합니다. 이 필터는 기본적으로 사용자의 username과 password를 검사하여 해당 사용자의 인증을 처리하는 역할
	 *  2. 사용자 인증 시도:  필터는 요청에서 사용자 이름과 비밀번호를 추출하고, 이 정보를 이용해 'UsernamePasswordAuthenticationToken을 생성' 이 토큰은 
	 *  1.인증 요청 감지: 기본적으로 이 필터는 ###/login POST 요청을 감지합니다.### 사용자가 로그인 폼에 입력한 사용자 이름과 비밀번호가 ###이 URL로 전송되면 필터가 가로챔###
	 *     'AuthenticationManager에 전달(인증 매니저)'에 전달되어실제 인증을 수행, 'DaoAuthenticationProvider를 호출'
	 *  3. 인증 성공 또는 실패 처리: 인증 과정이 성공적이면, 필터는 사용자를원래 요청했던 페이지로 redirection한다.
	 *     인증 실패하면, 사용자는 로그인 페이지나 지정된 오류 페이지로 redirection
	 *  4.세션 설정: 인증이 성공하면, 사용자의 세션을 생성하고 사용자의 인증정보를 세션에 저장한다. 이는 사용자가 추가 인증 없이 시스템의 다른 보안 영역에 접근할 수 있게 해준다
	 * */								
	@Override
	public void configure(HttpSecurity http) {
		// security 로직에 JwtFilter 등록
		http.addFilterBefore(
				(Filter) new JwtFilter(tokenProvider),
				UsernamePasswordAuthenticationFilter.class
		);
		
		
	}
	
	
}
