package com.shoppingmall.product;

import java.util.List;

import org.springframework.stereotype.Service;

import com.shoppingmall.toaf.basemvc.BaseSvc;
import com.shoppingmall.toaf.object.DataMap;

@Service
public class VueProductService extends BaseSvc<DataMap> {
	
		public List<DataMap> getProducts(){
				return this.dao.selectListQuery("ProductSQL.getProductsInfo", null);
		}
		
		public int doStoreItemsInCart(DataMap storedItemsMap) {
				return this.dao.insertQuery("ProductSQL.doStoreItemsInCart", storedItemsMap);
		}
		
		public DataMap doGetStoredMyItemsFromCart(DataMap userMap){
				return this.dao.selectQuery("ProductSQL.doGetStoredMyItemsFromCart", userMap);
		}
		
}
