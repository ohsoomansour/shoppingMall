package com.shoppingmall.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shoppingmall.toaf.object.DataMap;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class VueAdminAction {
	
	@Autowired
	private VueAdminService vueAdminService;
	
	/**
	 *@Date: 24.9.27
	 *@Param: 검색어,keyword (이름 또는 아이디 또는 email) 
	 *@Function: 관리자 사용자 검색  	
	 * */
	@GetMapping("/admin/searchForUsers")
	public List<DataMap> doSearchForUser(@RequestParam("keyword") String keyword ) {
		log.info("PATH:/admin/searchForUsers keyword ===>" + keyword);
		DataMap userMap = new DataMap();
		userMap.put("keyword", keyword);
		List<DataMap> userBySearch = this.vueAdminService.searchForUsers(userMap);
		log.info("userBySearch ====>" + userBySearch);
		return userBySearch;
	}
	
}
