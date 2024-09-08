package com.shoppingmall.jwt;

import java.io.IOException;

import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
	
	private static final String AUTHORIZATION_HEADER = "Authorization";
	private final TokenProvider tokenProvider;
	
	/**
	 *@doFilterInternal메서드: OncePerRequestFilter 클래스에서 '한 요청당 한 번만 실행되는 필터를 구현'할 때 사용되는 메서드 
	 * - 역할: 실제로 HTTP 요청을 처리하, 요청에 대해 특정 로직을 수행한 후 다음 필터로요청을 전달하는 역할을 한다.
	 * */
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		
		
	}
}
