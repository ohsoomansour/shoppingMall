package com.shoppingmall.admin;


import java.util.List;

import org.springframework.stereotype.Service;

import com.shoppingmall.toaf.basemvc.BaseSvc;
import com.shoppingmall.toaf.object.DataMap;

@Service
public class VueAdminService extends BaseSvc<DataMap> {

	
	public List<DataMap> searchForUsers(DataMap userMap) {
		return this.dao.selectListQuery("SecMemberSQL.searchForUsers", userMap);
	}
}
