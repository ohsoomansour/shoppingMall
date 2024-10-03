package com.shoppingmall.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.shoppingmall.member.model.User;
import com.shoppingmall.toaf.object.DataMap;
import com.shoppingmall.toaf.util.AES256Util;

import lombok.extern.slf4j.Slf4j;

@Slf4j

@RequestMapping("/member")
@Controller
public class MemberFrontController {
	
	@Autowired
	MemberFrontService memberFrontService;
	
	/* 
	 *@Author: osm
	 *@Date: 2024.5.14
	 *@Param: - 
	 *@return: ModelAndView
	 *@Function:회원 가입 페이지 이동
	*/
	@RequestMapping("/signUpFormPage.do")
	public ModelAndView doMemberJoinFormPage() {
		ModelAndView mav = new ModelAndView();
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
		    return new ModelAndView("error");
		}
		
	    mav.setViewName("/member/memberJoinForm");
		return mav;
	}
	/* 
	 *@Author: osm
	 *@Date: 2024.5.15
	 *@Param: - 
	 *@return: ModelAndView
	 *@Function:회원 가입 페이지 완료 화면
	*/
	@RequestMapping("/signUpConfirm.do")
	public ModelAndView doMemberJoiCompletePage() {
		ModelAndView mav = new ModelAndView("/member/memberJoinComplete");
	    try {
	    	
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
		return mav;
	}
	
	
	/** 
	 *@Author: osm
	 *@Date: 2024.5.18
	 *@UpdateDate: 2024.8.3 (tb_member -> tb_user, 컬럼 값 변경 f) 
	 *@Param: - 
	 *@return: ModelAndView
	 *@Function: 아이디 및 사업자등록번호 중복검사아이디 
	*/
	 //Q. @ModelAttribute는 HTTP Body를 어떻게 받나? {gubun:'ID', id: 1}
	@RequestMapping("/userDoubleCheck.do")
	public ModelAndView doMemberDoubleCheck(@ModelAttribute("gubun") String gubun, @ModelAttribute("id") String id) {
		ModelAndView mav = new ModelAndView("jsonView");

	    DataMap paraMap = new DataMap();
	    paraMap.put("u_email", id);
	    
	    try {
	    	if(gubun.equals("ID")) {
	    		int memberCount = memberFrontService.doCounteMemberId(paraMap);
	    		mav.addObject("memberCount", memberCount);
	    	}
	    } catch (Exception e) {
	    	System.out.println(e);
	    	return new ModelAndView("error");
	    }
	    //paraMap.put("gubun", gubun);
		return mav;
	}
	
	/* 
	 *@Author: osm
	 *@Date: 2024.5.20 
	 *@Param: - 
	 *@return: ModelAndView("jsonView")
	 *@Function: 비번 생성 
	*/
	
	@RequestMapping("/signUp.do")
	public ModelAndView doSignUp(@ModelAttribute User member) {
		
		ModelAndView mav = new ModelAndView("jsonView");
		DataMap paraMap = new DataMap();
		log.info("signup_Member:"+ member);
		
		try {

			String u_email = member.getU_email();
			String u_pw =  member.getU_pw().toString();    
			String u_type = member.getU_type();
			String u_name = member.getU_name();
			String biz_email1 = member.getBiz_email1();
			String biz_email2 = member.getBiz_email2();
			String u_address = member.getAddress();
			String u_ph = member.getU_ph();
			
			paraMap.put("u_email", u_email);
			paraMap.put("u_pw", AES256Util.strEncode(u_pw));
			paraMap.put("u_name", u_name);
			paraMap.put("biz_email1", biz_email1);
			paraMap.put("biz_email2", biz_email2);
			paraMap.put("u_address", u_address);
			paraMap.put("u_type", u_type);
			paraMap.put("u_ph", u_ph);
			this.memberFrontService.doInsertMember(paraMap);
			
		} catch(Exception e) {
			e.printStackTrace();

		}
		
		return mav;
	}
	
	
}
