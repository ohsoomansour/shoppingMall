package com.shoppingmall.product;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shoppingmall.toaf.object.DataMap;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class VueProductAction {
		@Autowired
		VueProductService productService;
		
		@GetMapping("/products")
		public List<DataMap> doGetProduct(){

			  List<DataMap> products = this.productService.getProducts();
				
				return products;
		}
		
		/**@Date : 2024.8.27 (화)
		 	*@Explain : JSON 데이터 -> Object 객체로 바인딩
		 	*@Param : @RequestBody의 역할은 프론트에서 json 데이터로 보내면 Map 형태의 키, 값의 객체로 바인딩한다. 
		 	*  - 가능한 경우1. @RequestBody Map<String, Object>itemsCart
		 	*  - 안되는 경우1. @ModelAttribute DataMap itemsCart "이유는 RequestInterceptor에서 json 형시이라서 enumeration에서 while문 분기를 못 탐
		 	*@Function : 카트에 있는 제품을 담는다.
		 	*@Return : 0, 1, -1 
		 	**/
		
		@PostMapping("/products/store_items_inCart")
		public int doStoreItemsInCart(@RequestBody Map<String, Object> itemsInCartMap , HttpServletRequest request, HttpServletResponse response)
		throws IOException {
			try {
					log.info("productMap===============> " + itemsInCartMap); 
					log.info("productMap===============>" + itemsInCartMap.getClass().getName()); //java.util.LinkedHashMap
					
					List<DataMap> itemsInCart = (ArrayList<DataMap>) itemsInCartMap.get("items_cart"); 
				   ObjectMapper objectMapper = new ObjectMapper();
				   String jsonItemsInCart = objectMapper.writeValueAsString(itemsInCart);
				   
					log.info("itemsInCart ======>" + itemsInCart);
					log.info("jsonItemsInCart ======>" + jsonItemsInCart);
					//[{id=0, title=sm cosmetic, price=11000, quantity=4, options=[{text=+50ml, value=0_0, price=3000, quantity=20, total=null}, {text=+70ml, value=0_1, price=5000, quantity=24, total=null}], total=null}]
					
					
					HttpSession session = request.getSession();
					String u_email = (String) session.getAttribute("u_email");
					log.info("u_email================>" + u_email);
					DataMap storedItemsMap = new DataMap();
					storedItemsMap.put("u_email", u_email);
					storedItemsMap.put("items_cart", jsonItemsInCart);
					
					int result = this.productService.doStoreItemsInCart(storedItemsMap);
					
					return result;
			} catch (Exception e) {
				 e.printStackTrace();
			}
				return 0;

		}
	 /**
		 * @Date : 2024.08.27
		 * @Function : 사용자에 따른 담기 주문 리스트  
		 * @Param : test1. u_email 값을 파라미터로 자기의 itemsInCart값을 가져온다! 
		 * @Description : 
		 **/
		@GetMapping("/products/get_storedMyItems")
		DataMap doGetStoredProducts(@RequestParam String u_email){
				DataMap userMap = new DataMap();
				userMap.put("u_email", userMap);
				DataMap result = this.productService.doGetStoredMyItemsFromCart(userMap);
				return result;
		}
		
		
		
		
}
