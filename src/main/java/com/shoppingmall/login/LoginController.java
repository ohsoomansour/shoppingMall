package com.shoppingmall.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.shoppingmall.toaf.object.DataMap;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("login")
@Controller
public class LoginController {
	//private final Logger log = LoggerFactory.getLogger(this.getClass()); // 부트는 기본 내장
	
	
	@Autowired
	private LoginService loginService;


	/*
	 *@Author:osm
	 *@Date: 2024.6.5
	 *@Param: 
	 *@Return:
	 *@Function: 
	 *@Description: 3.xx version부터 javax 가 아닌 jakarta 버전만을 지원
	 * */ 
	@RequestMapping("/login.do")
	public ModelAndView doFormLogin( HttpServletRequest request) {
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/common/login");
		//24.6.4 세션 테스트 
		String sessionKey = request.getSession().getId();  //sessionKey:C6111DEC7E8F00F0EE2DD18108D6100E
		log.info("sessionKey:" + sessionKey);
		
		return mav;
	}
	/*
	 *@Author:osm
	 *@Date: 2024.6.5
	 *@Param: 
	 *@Return:
	 *@Function: 로그인 
	 *@Description: @ResponseBody
	 * */ 
	
    @RequestMapping(value="loginx.do", method = RequestMethod.POST)
    public ModelAndView doLogin(@ModelAttribute("paraMap") DataMap paraMap, HttpServletRequest request) {
    	
    	ModelAndView mav = new ModelAndView("jsonView");
    	log.info("login's dataMap:" + paraMap);
    	String error_code = "1";
    	String error_mesg = "로그인 실패 했습니다.";
    	try {
    		HttpSession session = request.getSession();
    		if(log.isDebugEnabled()) {
    			log.debug("로그인 ==" + paraMap.get("userid") +  " | " + paraMap.getint("userpw"));
    		}
    		if(loginService.countUserInfo(paraMap) == 0) {
    			error_code = "2";
    			error_mesg = "아이디와 비밀번호를 다시 한 번 확인해주세요."; throw new Exception();
    		}
    	   DataMap userMap = this.loginService.getOneUserInfo(paraMap);
    	   if(log.isDebugEnabled()) {
    		   log.debug("로그인 RESULT ===> " + userMap.toString());
    	   }
    		
    	   //세션을 담는 부분
    	   
    	} catch(Exception e) {
    		mav.addObject("result_code", error_code);
    		mav.addObject("result_mesg", error_mesg );
    	}
    	
    	return mav;
    }
    
	
	
}
