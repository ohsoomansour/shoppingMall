package com.shoppingmall.secmember;



import org.springframework.stereotype.Service;

import com.shoppingmall.toaf.basemvc.BaseSvc;
import com.shoppingmall.toaf.object.DataMap;


@Service
public class MemberService extends BaseSvc<DataMap> {
	
	public int countMemberByLoginId(DataMap userMap) {
		return this.dao.countQuery("NonSocialMemberSQL.countMemberByLoginId", userMap);
	}
	
	public int countMemberByName(DataMap userMap) {
		return this.dao.countQuery("NonSocialMemberSQL.countMemberByName", userMap);
		
	}
	
	public void signUpFor(DataMap userMap) {
		this.dao.insertQuery("NonSocialMemberSQL.signUpFor", userMap);
	}
	
	public DataMap getOneMemberByName(DataMap userMap){
		
		return this.dao.selectQuery("NonSocialMemberSQL.getOneMemberByName", userMap);
	}
	
}
