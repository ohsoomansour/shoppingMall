
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
    /** 
     * @Joinpoint: 조인 포인트는 Aspect가 적용될 수 있는 프로그램 실행 지점(예: *Action.* 메소드 호출, 객체 생성 등)을 의미
     * @Aspect: 공통적인 관심사를 모아 놓은 것(로깅, 트랜잭션 관리, 보안 등) 여러 모듈에서 사용되는 기능을 모아 놓은 것 
     * @PointCut: 어떤 포인트 커트(=requestValueAdvisor): 조인 포인트=특정지점(*Action.*)와 어떤 어드 바이스(=requestInterceptor)를 실행할지 결정
     * @Advice: 특정 조인 포인트(Join Point)에서 수행하는 실제 동작을 정의
     * @Explain1: 어드바이스(처리 방법)를 설정: 이 어드바이스는 특정 관점에서 '메소드를 가로채 실행 전후에 실행될 코드'를 담고 있음
	     		  어떤 지점에 결합 할지를  결정하는 필터
	 * @Explain2: .*: 어떤 문자나 문자열˙이든 (빈 문자열 포함) 0회 이상 반복될 수 있음 -> Action 포함된 경우 매칭 
	 */
    	RegexpMethodPointcutAdvisor requestValueAdvisor = new RegexpMethodPointcutAdvisor();
    	requestValueAdvisor.setAdvice(requestInterceptor);
    	//* @조인 포인트 설정 역할 *패턴 해석:.모든 Action클래스.모든 메서드: 
    	requestValueAdvisor.setPattern(".*Action.*"); 
    	return requestValueAdvisor; 
    }
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    	registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/").setCachePeriod(60 * 60 * 24 * 365); 
    }
    
    /**
     * @Explain1: Advisor가 적용될 모든 빈에 대해 자동으로 프록시를 생성하며, 어드바이스(Advice)가 적용될 시점에 메소드 호출을 인터셉트하여 추가 기능을 수행
     * 			 Advisor가 프록시로 변환되어야 한다.
     * @Explain2: DefaultAdvisorAutoProxyCreator는 빈 생성 과정에서 '프록시를 자동으로 생성하는 빈 후처리기'이기 때문에, 
     *           다른 빈이 생성되기 전에 해당 빈이 먼저 준비되어야 합니다. 인스턴스와 무관하게 먼저 호출
     * @사용 주의: aop를 사용하기 위해서는  
     *  1. CGLIB 프록시 사용, defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
     *  2. 그러면 Service의 클래스 기반으로 로그인 컨트롤러 사용해야 한다. 이유는 클래스 기반 Proxy이기 때문 
     *  3. pom.xml 에서 주석처리 필요
     *   <dependency>
	 *	   <groupId>org.springframework.boot</groupId>
	 *	   <artifactId>spring-boot-starter-security</artifactId>
	 *	 </dependency> 
     *  결론. (스프링 시큐리티) 충돌로 인해 사용하지 않는 것이 좋음 -> 해결하기 위해 많은 시간이 소요!
     *  

    @Bean	
    public  DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() { 	
    	DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
    	// if not set this, it will use JDK dynamic proxy 
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
    	return defaultAdvisorAutoProxyCreator;
    } */
    
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
