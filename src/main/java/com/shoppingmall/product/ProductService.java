package com.shoppingmall.product;

import java.util.List;

import org.springframework.stereotype.Service;

import com.shoppingmall.toaf.basemvc.BaseSvc;
import com.shoppingmall.toaf.object.DataMap;

@Service
public class ProductService extends BaseSvc<DataMap> {
	
		public List<DataMap> getProducts(){
				return this.dao.selectListQuery("ProductSQL.getProductsInfo", null);
		}
}
