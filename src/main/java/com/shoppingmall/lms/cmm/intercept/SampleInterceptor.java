package com.shoppingmall.lms.cmm.intercept;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


@Slf4j
@Component
public class SampleInterceptor implements HandlerInterceptor {
	
	@Override
	public boolean preHandle(jakarta.servlet.http.HttpServletRequest request,
			jakarta.servlet.http.HttpServletResponse response, Object handler) throws Exception {
		 log.info("preHandle");
		// TODO Auto-generated method stub
		return HandlerInterceptor.super.preHandle(request, response, handler);
		
	}
	@Override
	public void afterCompletion(jakarta.servlet.http.HttpServletRequest request,
			jakarta.servlet.http.HttpServletResponse response, Object handler, Exception ex) throws Exception {
		log.info("afterCompletion");
		// TODO Auto-generated method stub
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
