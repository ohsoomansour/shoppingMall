package com.shoppingmall.oauth;

import java.util.Collections;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shoppingmall.secmember.Member;
import com.shoppingmall.secmember.SecMemberService;
import com.shoppingmall.toaf.object.DataMap;

import jakarta.security.auth.message.AuthException;
import lombok.extern.slf4j.Slf4j;

/**
 *@Date: 24.10.4
 *@호출시점:'OAuth2LoginAuthenticationFilter'는 OAuth2UserService 인터페이스의 구현체를 호출하여, 
 *        Access Token을 사용해 '사용자 정보'를 가져옵니다. 이때 DefaultOAuth2UserService가 호출  
 * */

@Slf4j
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService   {
	
	@Autowired
	private SecMemberService secUserService;
	
	/**
	 *@Explain: 
	 *@:소셜 로그인 -> 등록된 URL: http://{domain}/login/oauth2/code/{registrationId}?code=AUTHORIZATION_CODE 형식으로
	 *         *registrationId: OAuth2 공급자(Identity Provider)를 식별하는 ID  
	 *  -> *인증이 완료(호출시점): 토큰을 발급 -> OAuth2UserService 인터페이스의 구현체를 호출
	 *@return: OAuth2User 객체는 Google의 사용자 정보 API를 통해 가져온 정보, new DefaultOAuth2User(authorities, attributes, userNameAttributeName) 
	  - 추가 설명: SecurityContextHolder의 principal에 더 디테일하게 저장하기 위해 이렇게 변환해서 사용
	  - 10.7 git 데스크 탑
	 * */
	@Transactional
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{
	  try {
		//1.소셜 유저 정보 가져오기
		Map<String, Object> oAuth2UserAttributes =  super.loadUser(userRequest).getAttributes();
		log.info("oAuth2UserAttributes ===>" + oAuth2UserAttributes);
		//2. registragtionId 가져오기: yml에서 client.registration 값
		String registrationId = userRequest.getClientRegistration().getRegistrationId();
		log.info("registrationId ===>"+ registrationId);
		//3.userNameAttributeName 가져오기
		String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
				.getUserInfoEndpoint().getUserNameAttributeName();
		log.info("userNameAttributeName(attributeKey) ===>" + userNameAttributeName);
		//4.유저 정보 dto 생성
		OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfo.of(registrationId, oAuth2UserAttributes);
	    log.info("oAuth2UserInfo =====> " + oAuth2UserInfo);
		//5.회원 가입 및 로그인 : Member -> DataMap 
	    Member member = getOrSave(oAuth2UserInfo);	// PrincipalDetails에 member기준 user_name, email이 반드시 포함되어야 한다.
	    log.debug("getOrSave return ===>" + member);
		return new PrincipalDetails(member, oAuth2UserAttributes, userNameAttributeName);
	   } catch (AuthException e) {
		    log.debug("loadUser error! ");
			e.printStackTrace();
	   }
	   return new DefaultOAuth2User(
	       Collections.emptyList(),
	       Collections.emptyMap(),
	       "name"
	   );
	   
    }
	/*email 기준 -> DB에서 SocialMember에서 '검색' 또는 'new 회원' 생성하는 기능
	  1. 회원 가입할 때, 소셜 가입 ->  없어? getOrSave 로직을 사용해서 가입
	  2. JPA사용 경우(로직은 같음, 해석: DB member 테이블에서 못 찾으면 oAuth2UserInfo객체 -> Member 엔터티로 변환하는 메서드
	    why ? 5번에서 Member가 있으면 그대로 로그인 -> PrincipalDetails를 발급! 
	                Member가 없으면 '회원가입'
	*
	*/
	private Member getOrSave(OAuth2UserInfo oAuth2UserInfo) {
	  log.debug("getOrSave's param(=oAuth2UserInfo) ===> " + oAuth2UserInfo);
	  //회원 검색
	  DataMap user = secUserService.getMemberByEmail(oAuth2UserInfo.email());
	  //회원이 없으면 가입
	  if(user == null || user.isEmpty()) {
		  Member authMember = oAuth2UserInfo.toEntity();
		  log.debug("authMember ===> " + authMember);
		  DataMap authMemberMap =  new DataMap();
		  authMemberMap.put("login_id", authMember.email());
		  authMemberMap.put("email_verified", false);
		  authMemberMap.put("locked", false);
		  authMemberMap.put("authority", "USER");
		  authMemberMap.put("address", "");
		  authMemberMap.put("u_ph", "");
		  authMemberMap.put("login_type", 2); //  0:비회원, 1: 기본 로그인, 2: social
		  authMemberMap.put("user_name", authMember.user_name()); // String name,String email,String profile
		  authMemberMap.put("email", authMember.email());
		  authMemberMap.put("profile", authMember.profile());
		  secUserService.socialJoin(authMemberMap);
		  return authMember;
	  }
	  return Member.builder()
			  .email(user.getstr("email"))
			  .login_type(user.getint("user_type"))
			  .user_name(user.getstr("user_name"))
			  .profile(user.getstr("profile"))
			  .authority(user.getstr("authority"))
			  .build();
	}
	
}
