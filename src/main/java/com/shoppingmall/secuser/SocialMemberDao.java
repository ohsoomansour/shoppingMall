package com.shoppingmall.secuser;

import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SocialMemberDao extends MemberDao<SocialMember> {
	public Optional<SocialMember> getOneMemberByEmail(String email);
	public SocialMember signUpFor(SocialMember member);
}
