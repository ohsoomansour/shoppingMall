package com.shoppingmall.secmember;



import lombok.Data;

@Data
public class MemberSaveForm {
	private int 	login_type;
    private String  login_id;		// 로그인용 ID 값
    private String  email;	//이메일
    private boolean  email_verified;
    private String  password;	// 비밀번호
    private String  address;
    private String  authority;
    private String  user_name;
    private String  u_ph;
}
