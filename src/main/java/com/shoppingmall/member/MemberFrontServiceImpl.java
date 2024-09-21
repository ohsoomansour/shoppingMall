package com.shoppingmall.member;



import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.shoppingmall.toaf.object.DataMap;

@Transactional(value="postgresqlTransactionManager", propagation= Propagation.REQUIRED, rollbackFor=Exception.class)
public interface MemberFrontServiceImpl {
	
	public int doCounteMemberId(DataMap userMa);
	public int doInsertMember(DataMap paraMap);
	public int setTempPassword(DataMap paraMap);
}
