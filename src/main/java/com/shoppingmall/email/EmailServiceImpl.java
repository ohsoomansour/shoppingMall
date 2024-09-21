package com.shoppingmall.email;

public interface EmailServiceImpl {
	public String sendMail(EmailMessage emailMessage, String type);
	public String createCode();
	public String setContext(String code, String type);
}
