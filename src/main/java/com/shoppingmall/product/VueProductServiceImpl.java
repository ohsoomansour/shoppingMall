package com.shoppingmall.product;

import java.util.List;

import com.shoppingmall.toaf.object.DataMap;

public interface VueProductServiceImpl {
	public List<DataMap> getProducts();
	public int doStoreItemsInCart(DataMap storedItemsMap);
	public DataMap doGetStoredMyItemsFromCart(DataMap userMap);
}
