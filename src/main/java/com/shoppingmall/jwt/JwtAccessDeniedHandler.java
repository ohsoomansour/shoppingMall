package com.shoppingmall.jwt;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * @Explain: 접근 거부 처리 핸들러 
 * @When: *config: 설정에서 accessDeniedHandler에서 JwtAccessDeniedHandler등록 
 *  -> hasRole("ADMIN"):ROLE_ADMIN -> 로그인 사용자가 ROLE_USER -> JwtAccessDeniedHandler(accessDeniedHandler)호출
 *  ->  JwtAccessDeniedHandler가 실제 구현
 *  -> 클라이언트 사이드, status: 403, error: Forbidden, path: /admin/searchForUsers 
 * */

@Slf4j
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        try {
        	// 403:  .SC_FORBIDDEN 401: SC_UNAUTHORIZED 
        	log.info("###SECURITY DEBUGGING### - jwt Error Point");
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
		} catch (java.io.IOException e) {
			log.info("###SECURITY DEBUGGING### - jwt Error Point");
			e.printStackTrace();
		}
    }
}
