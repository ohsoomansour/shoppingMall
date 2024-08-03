package com.shoppingmall.member.model;
import java.util.Date;
import lombok.Data;

@Data
public class Member {
    private String 		u_email;
    private String 		u_pw ;
    private String		u_type;
    private String 		u_name;
    private String 		biz_email1;
    private String		biz_email2;
    private String		u_ph;
    private String 		address;
    private Date 		created_at;
    private String 		agree_flag;
}
