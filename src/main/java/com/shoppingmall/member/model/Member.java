package com.shoppingmall.member.model;
import java.util.Date;
import lombok.Data;

@Data
public class Member {
    private String id;
    private String pw;
    private String user_name;
    private String user_email;
    private String address;
    private Date created_at;
    private boolean signup_approval;
}
