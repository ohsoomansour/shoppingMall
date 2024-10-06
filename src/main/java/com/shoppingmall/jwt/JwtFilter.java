package com.shoppingmall.jwt;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
	 *@doFilterInternal메서드: OncePerRequestFilter 클래스에서 '한 요청당 한 번만 실행되는 필터를 구현'할 때 호출되어 실행
	 * - 역할: 실제로 HTTP 요청을 처리, 요청에 대해 특정 로직을 수행한 후 다음 필터로요청을 전달하는 역할을 한다.
	 *@SecurityContextHolder: 안에 저장된 Authentication은 다음 필터에서 사용 가능하다. 이유는 현재 인증된 사용자 정보를 전역적으로 관리
	 *@ 스프링 시큐리티에서 로그인 후 authentication 을 성공적으로 받아서 SecurityContextHolder에 넣었어 
	 *   그럼 다른 경로에서 SecurityContextHolder.getContext()하면 그 authentication이 그대로 있어?  
	 * */
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		String jwt = resolveToken(httpServletRequest);
		log.info("JwtFilter's doFilterInternal jwt =======> " + jwt);
		
		String requestURI = httpServletRequest.getRequestURI();
		if(StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
			Authentication authentication = tokenProvider.getAuthentication(jwt);
			log.info("JwtFilter -> getAuthentication(jwt) ====>" + authentication);
			//필터 -> Authentication에 권한 넣고 -> SecurityContextHolder 
			SecurityContextHolder.getContext().setAuthentication(authentication);
			log.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
			log.debug("Security Context에 '{} Pricipal", authentication.getPrincipal().toString());
			//인증이 되었기 때문에 다른 라우터로 이동하게 만들어야지 : response.sendRedirect("")  return;
			//response.sendRedirect("localhost:8088/#/products");
			// JSON 응답으로 인증 성공 메시지를 반환
		    response.setContentType("application/json");
		    response.setCharacterEncoding("UTF-8");
		    response.setStatus(HttpServletResponse.SC_OK);
		    response.getWriter().write("{\"message\": \"Authentication successful\"}");
		    response.getWriter().flush();
			return;
		} 
		else {
			log.info("유효한 JWT 토큰이 없습니다. uri:" + requestURI);
			//토큰이 유효하지 않을 경우 로그인 경로? 로직을 추가적으로 작성 
		}
		log.info("다음 필터로 이동!!");
		filterChain.doFilter(httpServletRequest, response);
	}
	/**
	 *@Date: 24.9.9 
	 *@Function: Request Header에서 토큰 정보를 꺼내오기 위한 메소드 
	 *@Front_Header: { Authorization:`Bearer ${token}` }
	 * - Bearer은 인증 방식 중 하나 뒤에 토큰을 붙임
	 */
	private String resolveToken(HttpServletRequest request) {
		 String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
		 log.debug("Authorization ====> " + bearerToken);
		 if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return  bearerToken.substring(7);
		 }
		 return null;
	}
}
