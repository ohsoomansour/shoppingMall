
package com.shoppingmall.config;


import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.aop.support.RegexpMethodPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import com.shoppingmall.lms.cmm.intercept.DefaultCheck;
import com.shoppingmall.toaf.intercept.RequestInterceptor;


@Repository
@Configuration
public class WebMvcConfig implements WebMvcConfigurer{

	@Autowired
	private RequestInterceptor requestInterceptor;
	
	/**
	 * @UpdatedDate: 2024.08.15 localhost:8080에서 *(전체)로 변경
	 * @addMapping : 모든 경 서브 경로를 포함
	 *  - 예) /send-mail/email
	 * @allowedOrigins : 모든 출처(=도메인) localhost:8080를 의미
	 * @alloowedMethods : GET, POST, PUT, DELETE를 의미
	 * @allowCreddentials: true와 false 의미 - 쿠키나 인증 헤더를 포함하는 허용하고 싶다면 'true' 그리고 allowedOrigins에 특정 출처를 설정 
	 * */
  @Override
  public void addCorsMappings(CorsRegistry registry) {
      registry.addMapping("/**")
              .allowedOrigins("*") 
              .allowedMethods("*") //GET", "POST", "PUT", "DELETE", "OPTIONS
              .allowedHeaders("*")
              .allowCredentials(false);
  }
	// 가로채는 경로 설정 가능
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
    	registry.addInterceptor(new DefaultCheck())
    	         .addPathPatterns("/**/*.do");
    }
    
    //REST API 설정 - josnView
    @Bean
    MappingJackson2JsonView jsonView(){
        return new MappingJackson2JsonView();
    }
    
    @Bean	
    public RegexpMethodPointcutAdvisor requestValueAdvisor() {  	
    	RegexpMethodPointcutAdvisor requestValueAdvisor = new RegexpMethodPointcutAdvisor();
    /** 
     * @Advice: 특정 조인 포인트(Join Point)에서 수행하는 실제 동작을 정의
     * @Joinpoint: 조인 포인트는 애스펙트가 적용될 수 있는 프로그램 실행 지점(예: 메소드 호출, 객체 생성 등)을 의미
     * @Explain1: 어드바이스(처리 방법)를 설정 :  이 어드바이스는 특정 관점에서 '메소드를 가로채 실행 전후에 실행될 코드'를 담고 있음
	 * @Explain2: 어떤 포인트 커트(=requestValueAdvisor): 조인 포인트(*Action.*)와 어떤 어드 바이스(=requestInterceptor) 
	     		  어떤 지점에 결합 할지를  결정하는 필터
	 * @Explain3: .*: 어떤 문자나 문자열˙이든 (빈 문자열 포함) 0회 이상 반복될 수 있음 -> Action 포함된 경우 매칭 
	 */
    	requestValueAdvisor.setAdvice(requestInterceptor);
    	//* @조인 포인트 설정 역할 *패턴 해석:.모든 Action클래스.모든 메서드: 
    	requestValueAdvisor.setPattern(".*Action.*"); //
    	return requestValueAdvisor; 
    }
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    	registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/").setCachePeriod(60 * 60 * 24 * 365); 
    }
    
    /**
     * @Explain: Advisor가 적용될 모든 빈에 대해 자동으로 프록시를 생성하며, 어드바이스(Advice)가 적용될 시점에 메소드 호출을 인터셉트하여 추가 기능을 수행
     * 			 Advisor가 프록시로 변환되어야 한다.
     
    */
    /**/
    @Bean	
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() { 	
    	DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
    	// if not set this, it will use JDK dynamic proxy 
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
    	return defaultAdvisorAutoProxyCreator;
    } 
    
    /*
    @Bean
	public MessageSource messageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasenames("messages/message");
		messageSource.setDefaultEncoding("UTF-8");
		messageSource.setUseCodeAsDefaultMessage(true);
		return messageSource;
	}
    
    @Bean
    public SessionLocaleResolver localeResolver() {
    	SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
    	sessionLocaleResolver.setDefaultLocale(Locale.KOREA);
    	return sessionLocaleResolver;
    }*/
    
    
}
