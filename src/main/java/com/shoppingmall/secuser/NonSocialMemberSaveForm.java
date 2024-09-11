package com.shoppingmall.secuser;

import lombok.Data;

@Data
public class NonSocialMemberSaveForm {
	private int 	auth_id; //DB에서 PK 값
	private int 	login_type;
    private String  login_id;		// 로그인용 ID 값
    private String  password;	// 비밀번호
    private String  user_name;
    private String  email;	//이메일
    private boolean email_verified = false;	//이메일 인증 여부
    private String  address;
    private boolean locked = false;	//계정 잠김 여부
    private String  authorities;
}
