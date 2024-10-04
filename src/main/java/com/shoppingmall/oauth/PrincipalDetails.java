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
@Slf4j
public record PrincipalDetails(
   Member member, 
   Map<String, Object> attributes,
   String attributeKey) implements OAuth2User, UserDetails {
	
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
   public String getPassword() {
	   return null;
   }
   @Override
   public String getUsername() {
	   return member.memberKey();
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
