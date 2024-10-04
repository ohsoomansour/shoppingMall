package com.shoppingmall.secmember;

import java.security.NoSuchAlgorithmException;

public interface SecMemberServiceImpl {
	public int join (MemberSaveForm memberSaveForm) throws NoSuchAlgorithmException;
	public void validateDuplicateName(MemberSaveForm memberSaveForm, Integer login_type);
	public int doCountLoginId(String login_id); 
}
