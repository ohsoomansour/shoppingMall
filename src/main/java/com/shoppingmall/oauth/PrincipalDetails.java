package com.shoppingmall.oauth;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import com.shoppingmall.secmember.Member;
import lombok.extern.slf4j.Slf4j;
/**
 *@Date:24.10.7 
 *@Explain: 왜  PrincipalDetails(DTO)에 Member 모델의 user_name 컬럼을 리턴해줘야 하나? 
 *   Map<String, Object> oAuth2UserAttributes =  super.loadUser(userRequest).getAttributes();
   -> OAuth2UserInfo[name=삼전, email=ceoosm@gmail.com, profile=null]
   <OAuth2LoginAuthenticationFilter> -> attemptAuthentication 핸들러에서 
   OAuth2AuthorizedClient authorizedClient = new OAuth2AuthorizedClient(
				authenticationResult.getClientRegistration(), ***oauth2Authentication.getName()***,
				authenticationResult.getAccessToken(), authenticationResult.getRefreshToken());
   -> oauth2Authentication.getName()은 '추상 인증토큰'
   *OAuth2AuthorizedClient:사용자가 인증된 후, 획득한 OAuth 2.0 액세스 토큰과 리프레시 토큰 같은 인증 정보를 저장하고 관리
   <AbstractAuthenticationToken>
	if (this.getPrincipal() instanceof UserDetails userDetails) {
		return userDetails.getUsername(); // 삼전 
	}
	<OAuth2LoginAuthenticationFilter> -> 인증 완료 successAuthentication
	 -> AuthenticationSuccessHandler를 호출 -(implements)-> OAuth2SuccessHandler  
 * */

@Slf4j
public record PrincipalDetails(
   Member member, 
   Map<String, Object> attributes,
   String attributeKey) implements OAuth2User, UserDetails {

   @Override
   public String getUsername() {  
     return member.getName();
   }
   @Override
   public String getPassword() {
	   return null;
   }
   @Override
   public String getName() {
	   return attributes.get(attributeKey).toString();
   }
   
   @Override
   public Map<String, Object> getAttributes(){
	   return attributes;
   }
   //============= 10.4 확인 중 ===========
   @Override
   public Collection<? extends GrantedAuthority> getAuthorities(){
	   log.info("member.getAuthority() ===>" + member.getAuthority());
	   return Collections.singletonList(
			   new SimpleGrantedAuthority("ROLE_" + member.getAuthority())
	   );
   }

   @Override
   public boolean isAccountNonExpired() {
	   return true;
   }
   @Override
   public boolean isCredentialsNonExpired() {
       return true;
   }

   @Override
   public boolean isEnabled() {
       return true;
   }
   
}
