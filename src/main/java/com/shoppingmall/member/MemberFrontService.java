package com.shoppingmall.member;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.shoppingmall.toaf.basemvc.BaseSvc;
import com.shoppingmall.toaf.object.DataMap;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Transactional(value="postgresqlTransactionManager", propagation= Propagation.REQUIRED, rollbackFor=Exception.class)
@Service       //REQUIRED: 현재 트랜잭션이 존재하면 그 트랜잭션 내에서 실행하고, 존재하지 않으면 새로운 트랜잭션을 시작 즉, 메소드가 항상 트랜잭션 내에서 실행되도록 보장
public class MemberFrontService extends BaseSvc<DataMap> implements MemberFrontServiceImpl{ 
	
	/*회원가입 후 아이디 중복 검사*/
	public int doCounteMemberId(DataMap paraMap) {
		return this.dao.countQuery("V_MemberSQL.doCountMemberId", paraMap);
	}
	/* 회원 */
	public int doInsertMember(DataMap paraMap) {
		String bizEmail = paraMap.getstr("biz_email1")
				+ "@" + paraMap.getstr("biz_email2");
		paraMap.put("biz_email", bizEmail);

		String u_type = paraMap.getstr("u_type");
		log.info("u_type ===========>"+ u_type);
		if(u_type.equals("CUSTOMER")) {
			 paraMap.put("agree_flag", false);
			 log.info("agree_flag==============>" + paraMap);
		}else if(u_type.equals("A")){
			paraMap.put("agree_flag", true);
			log.info("agree_flag==============>" + paraMap);
		}
		
		return this.dao.insertQuery("V_MemberSQL.doInsertMember", paraMap);
	}
	//2024.08.14 작성 : 비밀번호 변경 요청 사용자 계정, 인증 번호 - String u_email, String authNum
	public int setTempPassword(DataMap userMap) {
			//유저 아이디로 찾아서 임시 패스워드 컬럼에 끼워 넣고
			return this.dao.insertQuery("V_MemberSQL.doSetTempPassword", userMap);
	}
	
}
