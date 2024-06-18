package com.shoppingmall.login;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shoppingmall.toaf.basemvc.BaseSvc;
import com.shoppingmall.toaf.object.DataMap;
import com.shoppingmall.toaf.util.AES256Util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LoginService extends BaseSvc<DataMap>  {
	public int countUserInfo(DataMap paraMap) {
		return this.dao.countQuery("LoginSQL.countUserInfo", paraMap);
	}
	
	public DataMap getOneUserInfo(DataMap paraMap) {
		return this.dao.selectQuery("LoginSQL.getOneUserInfo", paraMap); 
		
	}
	
	// 6.12 사용자 메뉴 권한 (+Tbiz 참조)
	public List<DataMap> getUserMenuByMembertype(DataMap paraMap) {
		return this.dao.selectListQuery("LoginSQL.getUserMenuByMembertype", paraMap);
	}
	// 6.12 사용자 자식 메뉴 권한 (+Tbiz 참조)
	public List<DataMap> getUserChildMenuByMembertype(DataMap paraMap) {
		return this.dao.selectListQuery("LoginSQL.getUserChildMenuByMembertype", paraMap);
	}
	public boolean login(DataMap dataMap)  {
	  
	   //String loginPw = (String) dataMap.get("pw"); 
	   
	    try {
	     //1.admin이 인정? -> 메뉴권한을 가지고 들어가고 	
	     
	     //2.접속 로그 기록
	    	
	    //3. 유저 세션 정보기록
	    	
	      DataMap userMap =  this.dao.selectQuery("LoginSQL.getOneUserInfo", dataMap);
	  	  log.info("login'service dataMap  ===:" + userMap); 	
	  	  log.info("db 암호화된 비번 :" + userMap.getstr("pw"));
	  	  log.info("현재 로그인 비번: " + dataMap.getstr("pw")); 
	  	
	  	  log.info("복호화된 비번:"  +  AES256Util.strDecode(userMap.getstr("pw")) ); 
	  	  
			boolean result = AES256Util.strDecode(userMap.getstr("pw")).equals(dataMap.getstr("pw"));
			log.info("login service result=== " + result);
			if (result) {
				log.info("성공적으로 복호화 되었습니다!");
			    return true;
			} else {
				log.info("비밀번호 복호화에 실패하였습니다.");
				return false;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false; 
	}
	

}