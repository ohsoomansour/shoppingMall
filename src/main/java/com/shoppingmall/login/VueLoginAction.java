package com.shoppingmall.login;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.shoppingmall.toaf.object.DataMap;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
public class VueLoginAction {
		
		@Autowired
		private LoginService loginService;

	   /**
		 *@Author:osm
		 *@Date: 2024.08.13
		 *@Param: 
		 *@Return:
		 *@Function: 
		 *@Description: 3.xx version부터 javax 가 아닌 jakarta 버전만을 지원
		 * */ 
		@RequestMapping("/login.do")
		public ModelAndView doFormLogin( HttpServletRequest request) {
			log.debug("login debug test");
			ModelAndView mav = new ModelAndView();
			mav.setViewName("/common/login");
			//24.6.4 세션 테스트 
			String sessionKey = request.getSession().getId();  //sessionKey:C6111DEC7E8F00F0EE2DD18108D6100E
			log.info("sessionKey:" + sessionKey);
			
			return mav;
		}
	   /**
		 *@Author:osm
		 *@Date: 2024.08.13
		 *@Param: 
		 *@Return:
		 *@Function: 로그인 
		 *@ModelAttribute HTTP 요청 파라미터를 자바 객체에 바인딩
		 *@Test1 : 반환 갑이 ModelAndView 형태는 REST API에서 적절하지 않음 
		 *@Test2 : request body 값(= FormData) => @RequestBody Map<String, Object> userMap (O)
		 * - Annotation indicating a method parameter should be bound to the body of the web 
		 * - HTTP 요청 본문을 메서드의 파라미터로 지적된 자바 객체애 바인딩
		 *
		  @Test3 : 클라이언트, request body 값 => @RequestBody DataMap userMap (x)
		  -> DataMap userMap은 (사용자 정의)모델이다. 따라서 요청 body에 바인딩이 안됨 그러나 Map<String, Object> userMap은 가능
		           클라이언트, request body 값({u_email:this.u_email, u_pw:this.p_pw}) => @ModelAttribute  DataMap userMap (x)
		 					 					request body 값(= FormData) => @ModelAttribute  DataMap userMap (O)
		 					 					->Annotation that binds a method parameter (or method return value )to a named 'model attribute'
		 					 					-> @
		 */   
		
		//dataMap :
	    @PostMapping("/login/Vueloginx.do")
	    public int doLogin(@RequestBody Map<String, Object> userMap,  HttpServletRequest request, HttpServletResponse response) throws IOException {
	    	
	    	log.info("login's userMap =============>" + userMap);
	    	String error_code = "1";
	    	String error_mesg = "로그인 실패 했습니다.";
	    	String result;
	    	String resultMsg;
	    	
	    	
	    	DataMap dataMap = new DataMap();
	    	
	    	try {
	    		
	    		dataMap.put("u_email", userMap.get("u_email")); 
	        dataMap.put("u_pw", userMap.get("u_pw"));
	    		boolean loginResult = this.loginService.login(dataMap);
	    		log.info("loginResult===============>" +  loginResult);
	    		if(loginResult) {
	    			result = "SUCCESS";
	    			
	    			DataMap userInfo = this.loginService.getOneUserInfo(dataMap);
	    			log.info("userInfo:"+userInfo);
	    			dataMap.put("u_type", userInfo.get("u_type"));
	    		  //mav.addObject("u_id", userInfo.get("u_id"));
	    			//mav.addObject("u_email", userInfo.get("u_email"));

	    			List<DataMap> listUserMenuAttr = loginService.getUserMenuByMembertype(dataMap);
	    			List<DataMap> loginMenu = new ArrayList<DataMap>();
	    			log.info("listUserMenuAttr:"+listUserMenuAttr);
	    			for(DataMap list : listUserMenuAttr) {
	    				log.info("list:" + list);
	    				DataMap resultMapSub = new DataMap();
	    				resultMapSub.put("u_email", userInfo.get("u_email"));
	    				resultMapSub.put("parent_menu_id", list.get("menu_id"));  //0depth - 예)A0
	    				resultMapSub.put("u_type", userInfo.get("u_type")); //A		    
					    list.put("children", loginService.getUserChildMenuByMembertype(resultMapSub));
					    /* 2024.08.04 적합 
					     * list:{menu_id=A0, text=회원관리, parent_menu_id=A0, menu_url=/admin/memberList.do, depth=0, auth_seqno=1,
					     *      children=[{menu_id=A1, text=회원 정보 수정, menu_url=/admin/AeditMember.do, depth=1, auth_seqno=2}, {menu_id=A2, text=포인트 얻기, menu_url=/admin/getPoint.do, depth=1, auth_seqno=3}]}
	    				 * ##menuList로 변경
	    				 * 
	    				*/
	    				
	    				loginMenu.add(list);
	    				log.info("LoginController-loginMenu:" + loginMenu);
	    				//mav.addObject("loginMenu", loginMenu);

	    				
	    				//mav.addObject("no")
	    			
	    			}
	    			
	    		}
	    
	    	} catch(Exception e) {
	    		System.out.println(e);
	    		//mav.addObject("result_code", error_code);
	    		//mav.addObject("result_mesg", error_mesg );
	    	}
	    	
	    	return 1;
	    }
	    
		
		
}
