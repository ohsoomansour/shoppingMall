package com.shoppingmall.websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shoppingmall.toaf.object.DataMap;

import lombok.extern.slf4j.Slf4j;

/**
 * @TextWebSocketHandler -> 상속받게 되면, '3개의 메소드'를 오버라이딩
 * @private: 클래스의 모든 인스턴스에 대해 공유되고, 변경 불가능하며, 해당 클래스 내에서만 접근 가능하다는 것을 의미
 * @static : 클래스 자체의 멤버 변수 및 메서드 정의 
 * @final : 1) 한 번 초기화된 후에는 값을 변경할 수 없음  2) final로 선언된 메서드는 서브 클레애스에서 오버라이딩 안됨 
 * @private static final은 이 클래스 내에서만 사용하고 상수로 사용하겠다. 
 * @ConcurrentHashMap : (concurrent가 동시에 발생하는) 멀티스레드 환경에서 안전하게 동작하는 해시맵의 구현체 
 * 
 * */

@Slf4j
@Component
public class SalesWebSocketHandler extends TextWebSocketHandler  {
			
		private static final ConcurrentHashMap<String, WebSocketSession> CLIENTSMAP = new ConcurrentHashMap<String, WebSocketSession>();
	
		/**
		 * @function : 사용자가 웹소켓에 접속하게 되면 동작하는 메소드  
		 * */
		@Override
		public void afterConnectionEstablished(WebSocketSession session) {
				log.info("연결 됨 =======> "+ session.getId());
				CLIENTSMAP.put(session.getId(), session);
				
		}
		/**
		 * @function : 웹소켓 서버접속이 끝났을때 동작하는 메소드,  웹소켓 서버접속이 끝났을때 동작하는 메소드
		 * */
		@Override
		public void afterConnectionClosed(WebSocketSession session, CloseStatus status) 
		{	
				log.info("==== 연결 끊김 =========>" + session.getId() + "=== 끊긴 상태 ===>" + status);
				CLIENTSMAP.remove(session.getId());
		}
		
		/**
		 * @Function : 구매 -> eCosmetic_TotalSales -> 판매 량이 계속 쌓이면서  
		 * @CLIENTSMAP.entrySet(): 맵에 저장된 모든 키-값 쌍의 Set 뷰를 반환
		 *  - set 자료 구조 특징: 중복된 요소 허용x + 순서 없음 + 빠른 검색 + 해시 기반, 검색/추가/삭제가 빠름
		 * @key를 얻는 방법 : arg.getKey().equals(id)
		 * @
		 * */
		@Override
		protected void handleTextMessage(WebSocketSession session, TextMessage msg) throws Exception 
		{

				ObjectMapper objectMapper = new ObjectMapper(); //ObjectMapper를 사용하여 적절히 파싱
				String id = session.getId();
				log.info("msg ========>" + msg);
				Integer id0Total = 0;
				Integer id1Total = 0;
	
				//List<Map<String, Object>> orders = objectMapper.readValue(msg.getPayload(), new TypeReference<List<Map<String, Object>>>() {});
				List<Map<String, Object>> orders = objectMapper.readValue(msg.getPayload(), new TypeReference<List<Map<String, Object>>>() {});
				
				log.info("orders ===============>" + orders);
				DataMap orderMap = new DataMap();
				List<DataMap> salesMapList = new ArrayList<DataMap>();
				for (Map<String, Object> order : orders) {
            log.info("Order received: =======> " +  order);
            //{id=0, name=sample cosmetic, price=11000, quantity=11}
            //{id=1, name=쌔럼 , price=13000, quantity=12}
            if((Integer) order.get("id") != null && (Integer) order.get("id") == 0) {
            		//해당 아이템 총 값을 계산해서 넘겨 줄 지 결정
            		int total = (int) order.get("total");
            		id0Total += total;
            		orderMap.put("O_id_0", id0Total);
            		
            } 
            if ((Integer) order.get("id") != null && (Integer) order.get("id") == 1) {
            		int total = (int) order.get("total");
            		id1Total += total;
            		orderMap.put("O_id_1", id1Total);
            		
            }
        }
				String jsonSalesTotal = objectMapper.writeValueAsString(orderMap);
				TextMessage responseVal = new TextMessage(jsonSalesTotal);
				CLIENTSMAP.entrySet().forEach(arg -> {
					try {


							arg.getValue().sendMessage(responseVal);
					} catch (IOException e) {
							e.printStackTrace();
					}
				});
				
		}

		
}