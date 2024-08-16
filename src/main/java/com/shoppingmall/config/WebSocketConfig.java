package com.shoppingmall.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.shoppingmall.websocket.SalesWebSocketHandler;

import lombok.RequiredArgsConstructor;
@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer{
		
		//private final WebSocketHandler webSocketHandler;
		@Autowired
		SalesWebSocketHandler salesWebSocketHandler;
		
		@Override
		public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
				//ex) ws://127.0.0.1:8080/ws/sales (웹소켓 서버의 엔드포인트를 정의)
				registry.addHandler(salesWebSocketHandler, "/ws/sales").setAllowedOrigins("*");
		}

}
