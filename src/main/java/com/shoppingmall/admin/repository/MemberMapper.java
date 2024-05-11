package com.shoppingmall.admin.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.shoppingmall.admin.model.Member;
import com.shoppingmall.toaf.object.DataMap;

@Mapper
public interface MemberMapper {
    Member getMember(int id);

    List<Member> getMemberList();

    int createMember(Member member);

    int updateMember(Member member);

    int deleteMember(int id);
    
    int updateSignUpApproval(int id);
    
    
}
