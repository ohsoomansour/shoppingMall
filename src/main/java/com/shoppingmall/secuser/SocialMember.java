package com.shoppingmall.secuser;

import lombok.Data;

@Data
public class SocialMember {
	private long 	auth_id; //DB에서 PK 값
    private String  login_id;		// 로그인용 ID 값
    private String  password;	// 비밀번호
    private String  email;	//이메일
    private boolean email_verified;	//이메일 인증 여부
    private boolean locked;	//계정 잠김 여부
    private String  address;
    private String  role;
    private String  nickname;	//닉네임
}
