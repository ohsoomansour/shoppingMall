package com.shoppingmall.post;

import java.io.IOException;
import java.util.List;

import com.shoppingmall.toaf.object.DataMap;

public interface VuePostServiceImpl {
	List<DataMap> getAllPostsList();
	int doPublishPost(DataMap articleMap);
	public int doSaveFile(DataMap articleMap) throws IOException;
	
}
