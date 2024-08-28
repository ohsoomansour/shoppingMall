package com.shoppingmall.login;

import java.util.List;

import org.springframework.stereotype.Service;

import com.shoppingmall.toaf.basemvc.BaseSvc;
import com.shoppingmall.toaf.object.DataMap;
import com.shoppingmall.toaf.util.AES256Util;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class VueLoginService extends BaseSvc<DataMap> {
		public int countUserInfo(DataMap paraMap) {
				return this.dao.countQuery("VueLoginSQL.countUserInfo", paraMap);
			}
			
			public DataMap getOneUserInfo(DataMap paraMap) {
				
				return this.dao.selectQuery("VueLoginSQL.getOneUserInfo", paraMap);
			
			}
			
			// 6.12 사용자 메뉴 권한 (+Tbiz 참조)
			public List<DataMap> getUserMenuByMembertype(DataMap paraMap) {
				return this.dao.selectListQuery("VueLoginSQL.getUserMenuByMembertype", paraMap);
			}
			// 6.12 사용자 자식 메뉴 권한 (+Tbiz 참조)
			public List<DataMap> getUserChildMenuByMembertype(DataMap paraMap) {
				return this.dao.selectListQuery("VueLoginSQL.getUserChildMenuByMembertype", paraMap);
			}
			public boolean login(DataMap dataMap)  {
			  
			   //String loginPw = (String) dataMap.get("pw"); 
			   
			    try {
			      DataMap userMap =  this.dao.selectQuery("VueLoginSQL.getOneUserInfo", dataMap);
			  	  log.info("login'service dataMap  ===:" + userMap); 	
			  	  log.info("db 암호화된 비번 :" + userMap.getstr("u_pw"));
			  	  log.info("현재 로그인 비번: " + dataMap.getstr("u_pw")); 
			  	  log.info("복호화된 비번:"  +  AES256Util.strDecode(userMap.getstr("u_pw")) ); 
			  	  
					boolean result = AES256Util.strDecode(userMap.getstr("u_pw")).equals(dataMap.getstr("u_pw"));
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
