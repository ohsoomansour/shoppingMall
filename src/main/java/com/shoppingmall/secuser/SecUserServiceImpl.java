package com.shoppingmall.secuser;

import java.security.NoSuchAlgorithmException;

public interface SecUserServiceImpl {
	public int join (MemberSaveForm memberSaveForm) throws NoSuchAlgorithmException;
	public void validateDuplicateName(MemberSaveForm memberSaveForm, Integer login_type);
	public int doCountLoginId(String login_id); 
}
