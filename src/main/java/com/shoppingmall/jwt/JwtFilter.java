package com.shoppingmall.jwt;

import java.io.IOException;

import org.springframework.util.StringUtils;
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
	
	public static final String AUTHORIZATION_HEADER = "Authorization";
	private final TokenProvider tokenProvider;
	
	/**
	 *@doFilterInternal메서드: OncePerRequestFilter 클래스에서 '한 요청당 한 번만 실행되는 필터를 구현'할 때 사용되는 메서드 
	 * - 역할: 실제로 HTTP 요청을 처리하, 요청에 대해 특정 로직을 수행한 후 다음 필터로요청을 전달하는 역할을 한다.
	 * */
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		String jwt = resolveToken(httpServletRequest);
		String requestURI = httpServletRequest.getRequestURI();
	}
	/**
	 *@Date: 24.9.9 
	 *@Function: Request Header에서 토큰 정보를 꺼내오기 위한 메소드 
	 *@Front_Header: { Authorization:`Bearer ${token}` }
	 * - Bearer은 인증 방식 중 하나 뒤에 토큰을 붙임
	 */
	private String resolveToken(HttpServletRequest request) {
		 String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
		 if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return  bearerToken.substring(7);
		 }
		 return null;
	}
}
