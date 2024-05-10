package com.shoppingmall.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.shoppingmall.admin.model.Member;
import com.shoppingmall.admin.repository.MemberMapper;
import com.shoppingmall.toaf.object.DataMap;




/* DataMap 사용
 * ListOrderedMap은 HashMap과 같은 일반적인 맵(Map)과는 달리 요소들이 추가된 순서를 기억합니다.
 *  따라서 이 맵을 순서가 중요한 상황에서 사용하면 요소들의 순서를 유지하면서 키-값 매핑을 관리
 * */
/* SqlSession은 MyBatis와 함께 사용되며 데이터베이스와의 상호 작용을 담당합니다. 
 * SqlSession은 MyBatis 프레임워크에서 세션을 나타내며, 데이터베이스와의 트랜잭션 관리, 
 * SQL 쿼리의 실행, 그리고 결과 매핑 등을 처리
 * */


interface AdminService {
	int updateSignUpApproval(int id);
	Member getMember(int id);
	List<Member> getMemberList();
    int createMember(int id);
    int updateMember(int id);
    int deleteMember(int id);
}

@Service
@Transactional(value="postgresqlTransactionManager", propagation= Propagation.REQUIRED, rollbackFor=Exception.class)
public class AdminServiceImpl implements AdminService {
	
	@Autowired
	private MemberMapper memberMapper;
	
	@Override
	public Member getMember(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Member> getMemberList() {
	
		return memberMapper.getMemberList();
	}
	
	
	@Override
	public int updateSignUpApproval(int id) {
		return memberMapper.updateSignUpApproval(id);
	}

	@Override
	public int createMember(int id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateMember(int id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deleteMember(int id) {
		// TODO Auto-generated method stub
		return 0;
	}


	
}
