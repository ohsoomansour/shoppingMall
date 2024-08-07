package com.shoppingmall.lms.cmm.intercept;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import jakarta.servlet.http.HttpSession;


/**
 * @WebMvcConfig : 여기에서 설정
 * @참고 : AOP와별개 
 * */

@Slf4j							// HandlerInterceptor
@Component   					//implements HandlerInterceptorAdapter
public class DefaultCheck implements HandlerInterceptor {
	
	@Override
	public boolean preHandle(jakarta.servlet.http.HttpServletRequest request,
			jakarta.servlet.http.HttpServletResponse response, Object handler) throws Exception {
		log.info("preHandle");	
		HttpSession sesssion = request.getSession();
			
		return HandlerInterceptor.super.preHandle(request, response, handler);
		//return true;
	}
	@Override
	public void afterCompletion(jakarta.servlet.http.HttpServletRequest request,
			jakarta.servlet.http.HttpServletResponse response, Object handler, Exception ex) throws Exception {
		log.info("afterCompletion");
		// 요청  인터셉터
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}
	
    @Override
    public void postHandle(jakarta.servlet.http.HttpServletRequest request,
    		jakarta.servlet.http.HttpServletResponse response, Object handler, ModelAndView modelAndView)
    		throws Exception {
    	log.info("postHandle");
    	// TODO Auto-generated method stub
    	HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }
	
}
