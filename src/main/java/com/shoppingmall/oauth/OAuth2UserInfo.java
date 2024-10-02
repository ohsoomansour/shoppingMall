package com.shoppingmall.oauth;

import java.util.Map;

import lombok.Data;

@Data
public class OAuth2UserInfo {
	private String regId;
	private Map<String, Object> attributes; // sub, name, email 
	private String eamil;

	public static OAuth2UserInfo of(String regId, Map<String, Object> attributes) {
		OAuth2UserInfo oAuth2UserInfo = new OAuth2UserInfo();
		oAuth2UserInfo.setRegId(regId);
		oAuth2UserInfo.setAttributes(attributes);
		
		return oAuth2UserInfo;
	}
}
