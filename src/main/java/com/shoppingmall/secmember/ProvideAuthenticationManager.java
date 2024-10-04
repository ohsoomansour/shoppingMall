package com.shoppingmall.secmember;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class ProvideAuthenticationManager implements AuthenticationManager {
	 
	
	 public Authentication authenticate(Authentication authentication){
		 return authentication;
	 }
}
