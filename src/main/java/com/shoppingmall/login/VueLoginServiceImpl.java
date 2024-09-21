package com.shoppingmall.login;

import java.util.List;

import com.shoppingmall.toaf.object.DataMap;

public interface VueLoginServiceImpl {
	int countUserInfo(DataMap paraMap);
	public DataMap getOneUserInfo(DataMap paraMap);
	public List<DataMap> getUserMenuByMembertype(DataMap paraMap);
	public List<DataMap> getUserChildMenuByMembertype(DataMap paraMap);
	public boolean login(DataMap dataMap);
}
