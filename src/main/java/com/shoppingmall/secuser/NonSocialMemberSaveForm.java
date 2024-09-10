package com.shoppingmall.secuser;

import lombok.Data;

@Data
public class NonSocialMemberSaveForm {
	private int login_type;
	private String user_email;
	private String user_pw;
	private String user_name;
}
