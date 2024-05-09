package com.shoppingmall.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.shoppingmall.lms.cmm.intercept.SampleInterceptor;

@Repository
@Configuration
public class WebMvcConfig implements WebMvcConfigurer{

	
	
	//.addPathPatterns("/**/*.do");
	// 가로채는 경로 설정 가능
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
    	registry.addInterceptor(new SampleInterceptor())
    	         .addPathPatterns("/**/*.do");
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
    }
    
    @Bean	
    public RegexpMethodPointcutAdvisor requestValueAdvisor() {  	
    	RegexpMethodPointcutAdvisor requestValueAdvisor = new RegexpMethodPointcutAdvisor();
    	requestValueAdvisor.setAdvice(requestInterceptor);
    	requestValueAdvisor.setPattern(".*Action.*");
    	return requestValueAdvisor;
    }
   
    @Bean	
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() { 	
    	DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
    	// if not set this, it will use JDK dynamic proxy 
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
    	return defaultAdvisorAutoProxyCreator;
    } 
    /*
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    	registry.addResourceHandler("/img/**")
    	.addResourceLocations("file:///"+rootPath+"/")
    	.setCacheControl(CacheControl.noCache().cachePrivate());
    }*/
}
