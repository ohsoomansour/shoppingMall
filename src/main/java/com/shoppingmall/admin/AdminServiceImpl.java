package com.shoppingmall.admin;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.shoppingmall.toaf.object.DataMap;

@Transactional
public interface AdminServiceImpl {
	public List<DataMap> getMemberList(DataMap dataMap);
	public int updateSignUpApproval(DataMap dataMap);
}
