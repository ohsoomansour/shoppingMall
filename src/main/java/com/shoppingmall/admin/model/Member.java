package com.shoppingmall.admin.model;
import java.util.Date;
import lombok.Data;

@Data
public class Member {
    private int id;
    private String first_name;
	private String last_name;
    private String address;
    private Date created_at;
    private boolean signup_approval;
}
