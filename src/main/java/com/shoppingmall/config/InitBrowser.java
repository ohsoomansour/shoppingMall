package com.shoppingmall.config;

import java.awt.Desktop;
import java.net.URI;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;


/**
 * @Explain1 : local 전용서버 어플리케이션(개발 모드, 서버를 시작하면 localhost 브라우저를 자동 실행 
 * @Explain2 : @PostConstruct 빈 생성(스프링 컨테이너에 의해 호출 됨) -> 의존성 주입 -> 빈 등록 후 *Bean의 초기 설정 
 * */

@Component
public class InitBrowser {
	
	/*
	@PostConstruct
	public void init() {
		String url = "http://localhost:8080";
		System.setProperty("java.awt.headless", "false");
		try {
			Desktop.getDesktop().browse(URI.create(url));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}*/
}
