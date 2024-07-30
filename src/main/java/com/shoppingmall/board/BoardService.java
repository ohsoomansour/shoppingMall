package com.shoppingmall.board;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.shoppingmall.toaf.basemvc.BaseSvc;
import com.shoppingmall.toaf.object.DataMap;

@Service
@Transactional(value="postgresqlTransactionManager", propagation= Propagation.REQUIRED, rollbackFor=Exception.class)
public class BoardService extends BaseSvc<DataMap> {
	
	public List<DataMap> getAllBoardsList(){
		return this.dao.selectListQuery("BoardSQL.getAllBoardsList", null);
	}
}
