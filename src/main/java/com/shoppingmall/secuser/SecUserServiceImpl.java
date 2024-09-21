package com.shoppingmall.secuser;

import java.security.NoSuchAlgorithmException;

public interface SecUserServiceImpl {
	public int join (NonSocialUserSaveForm nonSocialMemberSaveForm) throws NoSuchAlgorithmException;
	public void validateDuplicateName(NonSocialUserSaveForm memberForm, Integer login_type);
	public int doCountLoginId(String login_id); 
}
