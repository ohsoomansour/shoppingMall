package com.shoppingmall.member;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.shoppingmall.toaf.object.DataMap;
import com.shoppingmall.toaf.util.AES256Util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/** 
 *@Author: osm
 *@Date: 2024.8.11
 *@RestController : @Controller +  @ResponseBody 주석이 달린 컨트롤러
 * - @RestController가 붙은 클래스 내의 모든 메서드 @ResponseBody가 적용된 것과 동일하게 동작
 *
 *@ResponseBody  :  이름으로 해석되지 않고, 그대로 HTTP 응답 본문(JSON 형태)에 포함
 *@RequestBody : HTTP 요청 POST 메서드의 data를 java 객체로 변환 
 *@Description : 회원 가입 관련 컨트롤러 
*/

@Slf4j
@RestController 
public class VueMemberAction {
		
		@Autowired
		MemberFrontServiceImpl  memberFrontServiceImpl;
		
		/** 
		 *@Author: osm
		 *@Date: 2024.8.11
		 *@틀린방법 : @ModelAttribute DataMap paraMap (또는 Map<String, Object> paraMap) 값 안나옴 !!
		 *@옳은방법Params : @RequestParam("gubun") String gubun, @RequestParam("u_email") String u_email
		 *@return:
		 *@Function: 아이디  중복검사아이디 
		 *@에러1. Required request parameter 'u_email' for method parameter type String is not present
		 * - 보통 파라미터 값이 제대로 들어오지 않는 경우 발생
		*/
		 //Q. @ModelAttribute는 HTTP Body를 어떻게 받나? {gubun:'ID', id: 1}
		@GetMapping("/member/userDuplicCheck.do")
		public int doMemberDoubleCheckAction(@RequestParam("gubun") String gubun, @RequestParam("u_email") String u_email)throws IOException {
		    try {
		    	log.info("gubun ===========>" + gubun);
		    	log.info("u_email ===========>" + u_email);
		    
		    	if(gubun.equals("ID")) {
		    	  DataMap paraMap = new DataMap();
		    	  paraMap.put("gubun", gubun);
		    	  paraMap.put("u_email", u_email);
		    	  int memberCount = memberFrontServiceImpl.doCounteMemberId(paraMap);
		    	  log.info("memberCount =========>" + memberCount);
		    	  return memberCount; 
		    	}
		    } catch (Exception e) {
		    		e.printStackTrace();
		    }
		    //paraMap.put("gubun", gubun);
			  return 0;
		}
		
		/** 
		 *@Author: osm
		 *@Date: 2024.8.11
		 *@Param: @RequestBody DataMap userMap 
		 *@return:
		 *@Function: 
		 *@Test1 : 초기 벡터가 16바이트로 고정 값의 상태에서 같은 평문을 넣었을 때, 결과는 ?
		 *          dhtnaksen@3 -> dhtnaksen@34 -> tkfkdgo@34 -> dhtnaksen@3
		 *@Test1 결과 :  
		*/
		
		@PostMapping("/member/signUp.do")
		@ResponseBody
		public int doSignUpForMemberShip(@ModelAttribute("paraMap") DataMap userMap, HttpServletRequest request, HttpServletResponse response) throws IOException {
				try {
						log.info("signUp.do userMap ====================> " + userMap);
						//{u_type=A, u_email=admin5@naver.com, u_pw=dhtnaksen@3, u_name=오수만, u_address=서울 서초구 과천대로 786래미안 1201호 , biz_email1=osoomansour, biz_email2=@naver.com, u_ph=01036383330}
						String u_pw = (String) userMap.get("u_pw");
						userMap.put("u_pw", AES256Util.strEncode(u_pw));
						return this.memberFrontServiceImpl.doInsertMember(userMap);
				} catch(Exception e) {
						e.printStackTrace();
				
				}
				return 0;
		}
}
