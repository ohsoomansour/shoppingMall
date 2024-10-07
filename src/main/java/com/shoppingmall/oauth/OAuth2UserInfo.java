package com.shoppingmall.oauth;

import java.util.Map;

import com.shoppingmall.secmember.Member;
import com.shoppingmall.toaf.util.KeyGenerator;

import jakarta.security.auth.message.AuthException;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
/**
 *@Explain: 어떤 소셜 로그인인지 구별하여 유저 정보 dto(OAuth2UserInfo)를 생성 
 * */
@Slf4j
@Builder
public record OAuth2UserInfo(
		String name,
		String email,
		String profile
		
   ) {


	private static final String ILLEGAL_REGISTRATION_ID = "error regId!!";

	/**/
	public static OAuth2UserInfo of(String regId, Map<String, Object> attributes) throws AuthException {
		log.info("OAuth2UserInfo of 'regId' ===> " + regId);
		return switch(regId) {
		  case "google" -> ofGoogle(attributes); 
		  case "kakao" -> ofKakao(attributes);
		  default -> throw new AuthException(ILLEGAL_REGISTRATION_ID);
		};
	}
	
	
	private static OAuth2UserInfo ofGoogle(Map<String, Object> attributes) {
		return OAuth2UserInfo.builder()
				.name((String) attributes.get("name"))
				.email((String) attributes.get("email"))
				.profile((String) attributes.get("profile"))
				.build();
	}
	
	private static OAuth2UserInfo ofKakao(Map<String, Object> attributes) {
		Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
		Map<String, Object> profile = (Map<String, Object>) account.get("profile");
		return OAuth2UserInfo.builder()
				.name((String) profile.get("nickname"))
				.email((String) account.get("email"))
				.profile((String) profile.get("profile"))
				.build();
				
	}
	/* 			    .memberKey(KeyGenerator.generateKey()) */
	public Member toEntity() {
		return Member.builder()
				.user_name(name)
			    .email(email)
			    .profile(profile)
			    .authority("USER")
				.build();
	}
	
}
