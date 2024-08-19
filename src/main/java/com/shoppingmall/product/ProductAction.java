package com.shoppingmall.product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shoppingmall.toaf.object.DataMap;

@RestController
public class ProductAction {
		@Autowired
		ProductService productService;
		
		@GetMapping("/products")
		public List<DataMap> doGetProduct(){
				Map<String, Object> result = new HashMap<>();
			  List<DataMap> products = this.productService.getProducts();
				
				return products;
		}
}
