package com.shoppingmall.secuser;

import lombok.Data;

@Data
public class NonSocialMemberLoginForm {
	private String user_email;
	private String user_pw;
}
