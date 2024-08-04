package com.shoppingmall.login;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.shoppingmall.toaf.basemvc.BaseAct;
import com.shoppingmall.toaf.object.DataMap;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("login")
public class LoginAction  extends BaseAct {
	//private final Logger log = LoggerFactory.getLogger(this.getClass()); // 부트는 기본 내장
	
	
	@Autowired
	private LoginService loginService;

   /**
	 *@Author:osm
	 *@Date: 2024.6.4 
	 *@UpdateDate : 2024.8.3 (tb_member.sql -> tb_user.sql)
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
	 *@Date: 2024.6.5
	 *@UpdateDate : 2024.8.3 (tb_member.sql -> tb_user.sql)
	 *@Param: 
	 *@Return:
	 *@Function: 로그인 
	 *@Description: @ModelAttribute("paraMap")에서 paraMap 변수 의미는 컨트롤러에서 뷰로 전달 
	 *   doLogin(@ModelAttribute("paraMap") DataMap paraMap Map<String, Object> paraMap
	 */ 
	
	//dataMap :
    @RequestMapping(value="loginx.do", method = RequestMethod.POST) 
    public ModelAndView doLogin(@RequestParam Map<String, Object> paraMap, HttpServletRequest request) {
    	
    	ModelAndView mav = new ModelAndView("jsonView");
    	log.info("login's dataMap:" + paraMap);
    	String error_code = "1";
    	String error_mesg = "로그인 실패 했습니다.";
    	String result;
    	String resultMsg;
    	
    	
    	DataMap dataMap = new DataMap();
    	
    	try {
    		
    		dataMap.put("u_email", paraMap.get("u_email")); 
        	dataMap.put("u_pw", paraMap.get("u_pw"));
    		boolean loginResult = this.loginService.login(dataMap);
    		if(loginResult) {
    			result = "SUCCESS";
    			DataMap userInfo = this.loginService.getOneUserInfo(dataMap);
    			log.info("userInfo:"+userInfo);
    			dataMap.put("u_type", userInfo.get("u_type"));
    			/*##List 객체는 순서가 있는 컬랙션 + 자동 조정
    			 * 다형성 :  상위 클래스 타입의 참조 변수를 통해서 하위 클래스의 객체를 참조할 수 있도록 허용
    			 * 
    			*/
    			
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
    				mav.addObject("loginMenu", loginMenu);
    			
    			}
    			
    		}
    
    	   
    	   
    	   //세션을 담는 부분
    	   
    	} catch(Exception e) {
    		System.out.println(e);
    		mav.addObject("result_code", error_code);
    		mav.addObject("result_mesg", error_mesg );
    	}
    	
    	return mav;
    }
    
	
	
}
