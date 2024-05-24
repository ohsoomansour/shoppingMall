package com.shoppingmall.member.model;
import java.util.Date;
import lombok.Data;

@Data
public class Member {
	private int    		no;
    private String 		id;
    private String 		pw;
    private String		member_type;
    private String 		user_name;
    private String 		user_email1;
    private String		user_email2;
    private String 		user_email;
    private String 		address;
    private Date 		created_at;
    private String 	agree_flag;
}
