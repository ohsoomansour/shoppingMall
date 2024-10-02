package com.shoppingmall.oauth;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shoppingmall.secuser.SecUserService;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService   {
	
	@Autowired
	private SecUserService secUserService;
	
	/**
	 *@Explain: 
	 *@:소셜 로그인 -> 등록된 URL: http://{domain}/login/oauth2/code/{registrationId}?code=AUTHORIZATION_CODE 형식으로
	 *         *registrationId: OAuth2 공급자(Identity Provider)를 식별하는 ID  
	 *  -> *인증이 완료(호출시점) -> OAuth 2.0 공급자(google Authorization server=인증 서버)에서 토큰을 발급 
	 *        -> SpringSecurity 필터가 loadUser 메서드를 호출하여 해당 엑세스 토큰, OAuth2AccessToken token = userRequest.getAccessToken();
	 *         token 사용 -> 사용자 정보를 가져옴 
	 *@return: OAuth2User 객체는 Google의 사용자 정보 API를 통해 가져온 정보, new DefaultOAuth2User(authorities, attributes, userNameAttributeName) 
	 * */
	@Transactional
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{
		//1.유저 정보 가져오기
		Map<String, Object> oAuth2UserAttributes =  super.loadUser(userRequest).getAttributes();
		//2. registragtionId 가져오기
		String registrationId = userRequest.getClientRegistration().getRegistrationId();
		//3.userNameAttributeName 가져오기
		String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
				.getUserInfoEndpoint().getUserNameAttributeName();
		//4.유저 정보 dto 생성
		OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfo.of(registrationId, oAuth2UserAttributes);
		//5.회원 가입 및 로그인 
		
		
		return new PrincipalDetails();
	}
	/*email 기준 -> DB에서 SocialMember에서 '검색' 또는 'new 회원' 생성하는 기능
	  1. 회원 가입할 때, 소셜 가입 ->  getOrSave 로직을 사용해서 가입하는거라 
	*/
	private NonSocialUser getOrSave(OAuth2UserInfo oAuth2UserInfo) {
		oAuth2UserInfo.getAttributes().get("email");
	}
	
}
