package com.shoppingmall.secuser;

import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NonSocialMemberDao extends MemberDao<NonSocialMember> {
	//비소셜 회원조회
	public Optional<NonSocialMember> getOneMemberByName(String email);
	public NonSocialMember signUpFor(NonSocialMember member);
	
}
