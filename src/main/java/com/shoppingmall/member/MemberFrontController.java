package com.shoppingmall.member;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.shoppingmall.toaf.object.DataMap;


@RequestMapping("/member")
@Controller
public class MemberFrontController {

	/* 
	 *@Author: osm
	 *@Date: 2024.5.14
	 *@Param: - 
	 *@return: ModelAndView
	 *@Function:회원 가입 페이지 이동
	*/
	@RequestMapping("/memberJoinFormPage.do")
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
	@RequestMapping("/memberJoinConfirm.do")
	public ModelAndView doMemberJoiCompletePage() {
		ModelAndView mav = new ModelAndView("/member/memberJoinComplete");
	    try {
	    	
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
		return mav;
	}
	
	/* 
	 *@Author: osm
	 *@Date: 2024.5.14
	 *@Param: - 
	 *@return: ModelAndView
	 *@Function:회원 가입 페이지 이동
	*/
	@RequestMapping("/")
	public ModelAndView doMemberJoin() {
		ModelAndView mav = new ModelAndView("jsonView");
		return mav;
		
	}
	/* 
	 *@Author: osm
	 *@Date: 2024.5.18
	 *@Param: - 
	 *@return: ModelAndView
	 *@Function: 아이디 및 사업자등록번호 중복검사아이디 
	*/
	
	@RequestMapping("/memberDoubleCheck.do")
	public ModelAndView doMemberDoubleCheck(@ModelAttribute("dataMap") DataMap paraMap) {
		ModelAndView mav = new ModelAndView("jsonView");
		
		return mav;
	}
	
	
	
}
