package com.shoppingmall.login;

import org.springframework.stereotype.Service;

import com.shoppingmall.toaf.basemvc.BaseSvc;
import com.shoppingmall.toaf.object.DataMap;

@Service
public class LoginService extends BaseSvc<DataMap>  {
	public int countUserInfo(DataMap paraMap) {
		return this.dao.countQuery("LoginSQL.countUserInfo", paraMap);
	}
	
	public DataMap getOneUserInfo(DataMap paraMap) {
		return this.dao.selectQuery("LoginSQL.getOneUserInfo", paraMap);
	}
    
}
