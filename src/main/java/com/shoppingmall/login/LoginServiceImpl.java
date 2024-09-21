package com.shoppingmall.login;

import java.util.List;

import com.shoppingmall.toaf.object.DataMap;

public interface LoginServiceImpl {
	int countUserInfo(DataMap paraMap);
	DataMap getOneUserInfo(DataMap paraMap);
	List<DataMap> getUserMenuByMembertype(DataMap paraMap);
	public List<DataMap> getUserChildMenuByMembertype(DataMap paraMap);
	public boolean login(DataMap dataMap);
}
