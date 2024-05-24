package com.shoppingmall.member;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.shoppingmall.member.model.Member;
import com.shoppingmall.toaf.basemvc.BaseSvc;
import com.shoppingmall.toaf.object.DataMap;

@Service       //REQUIRED: 현재 트랜잭션이 존재하면 그 트랜잭션 내에서 실행하고, 존재하지 않으면 새로운 트랜잭션을 시작 즉, 메소드가 항상 트랜잭션 내에서 실행되도록 보장
@Transactional(value="postgresqlTransactionManager", propagation= Propagation.REQUIRED, rollbackFor=Exception.class)
public class MemberFrontService extends BaseSvc<DataMap>{ 
	
	/*회원가입 후 아이디 중복 검사*/
	public int doCounteMemberId(DataMap paraMap) {
		return this.dao.countQuery("V_MemberSQL.doCountMemberId", paraMap);
	}
	/* 회원 */
	public void doInsertMember(Member member) {
		String userEmail = member.getUser_email1()
				+ "@" + member.getUser_email2();
		member.setUser_email(userEmail);
		String member_type = member.getMember_type();
		if(member_type.equals("CUSTOMER") || member_type.equals("BIZ")) {
			member.setAgree_flag("N");
			//paraMap.put("agree_flag", 'N');
			
		}else{
			member.setAgree_flag("Y");
		}
		
		this.dao.insertQuery("V_MemberSQL.doInsertMember", member);
	}
	
}
