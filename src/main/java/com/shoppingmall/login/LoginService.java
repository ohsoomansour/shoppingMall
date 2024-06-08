package com.shoppingmall.login;

import org.springframework.stereotype.Service;
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
	/*
	 * public boolean login(String username, String password) throws Exception {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return false;
        }
        String decryptedPassword = AESUtil.decrypt(user.getPassword());
        return decryptedPassword.equals(password);
    }
	  
	 * */
	public boolean login(DataMap dataMap)  {
	  
	   //String loginPw = (String) dataMap.get("pw"); 
	   
	    try {
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
