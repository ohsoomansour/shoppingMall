package com.shoppingmall.user.model;

import lombok.Data;

@Data
public class User{

    private String  u_email;	//이메일
    private boolean  email_verified;
    private String  u_pw;	// 비밀번호
    private String  u_name;
    private String  biz_email1;
    private String  biz_email2;
    private String  address;
    private String  u_type;
    private String  user_name;
    private String  u_ph;
}
