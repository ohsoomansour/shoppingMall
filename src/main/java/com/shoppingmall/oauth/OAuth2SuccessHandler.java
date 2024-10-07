package com.shoppingmall.oauth;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.shoppingmall.jwt.TokenProvider;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 *@Date: 24.10.4 
 *@선행조건: code -> 구글 서버, accessToken발급
 *@Explan: 호출 시점, 'accessToken'을 사용하여 사용자를 인증 -> 인증 성공 후 OAuth2SuccessHandler 호출!
 * */

@Slf4j
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler  {
	@Autowired  //#/auth/success"
	private TokenProvider tokenprovider;
	private static final String URI = "http://localhost:8088/";
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
	  
	  // ***로그인 인증 -> authentication -> 토큰 발급 
	  String accessToken = tokenprovider.createToken(authentication);
	  log.info("OAuth2SuccessHandler' accessToken ===> " + accessToken);	  
	  /*
	  String redirectUrl = UriComponentsBuilder.fromUriString(URI)
			  .queryParam("accessToken", accessToken)
			  .build().toUriString();
			  //String redirectUrl = URI + "#/auth/success?accessToken=" + accessToken;
	  */
	  String redirectUrl = URI + "#/auth/success";
	  /*###문제점:'토큰 탈취 위험' - ###
	  * - 방법1. 'HTTP header'에 담아서 바로 프론트로 리다이렉트
	  * - 방법2. '쿠키'에 담아서 반환
	  */
	  Cookie cookie = new Cookie("accessToken", accessToken);
	  cookie.setHttpOnly(false); //xss 공격 방지, true의 경우, 크롬 브라우저에서 쿠키 값을 못 가져옴
	  cookie.setSecure(false); // https에서만 전송 true or false
	  cookie.setPath("/");  // accessToken  a path for the cookie to which the client should return the cookie. 
	  cookie.setMaxAge(60 * 60); //1h
	  response.addCookie(cookie);
	  response.sendRedirect(redirectUrl);
	}

}
