package com.shoppingmall.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.shoppingmall.member.model.Member;
import com.shoppingmall.toaf.object.DataMap;
import com.shoppingmall.toaf.util.AES256Util;

@RequestMapping("/member")
@Controller
public class MemberFrontController {
	
	@Autowired
	private MemberFrontService memberFrontService;
	
	
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
	 *@Date: 2024.5.18
	 *@Param: - 
	 *@return: ModelAndView
	 *@Function: 아이디 및 사업자등록번호 중복검사아이디 
	*/
	 //Q. @ModelAttribute는 HTTP Body를 어떻게 받나? {gubun:'ID', id: 1}
	@RequestMapping("/memberDoubleCheck.do")
	public ModelAndView doMemberDoubleCheck(@ModelAttribute("gubun") String gubun, @ModelAttribute("id") String id) {
		ModelAndView mav = new ModelAndView("jsonView");

	    DataMap paraMap = new DataMap();
	    paraMap.put("id", id);
	    System.out.println(gubun);
	    System.out.println(id);
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
	 *@return: ModelAndView
	 *@Function: 비번 생성 
	*/
	
	@RequestMapping("/memberJoin.do")
	//@ModelAttribute Member member
	public ModelAndView doMemberJoin(@ModelAttribute Member member) {
		System.out.println(member.toString());
		ModelAndView mav = new ModelAndView("jsonView");
		DataMap paraMap = new DataMap();
		try {
			String pw = AES256Util.strEncode(member.getPw().toString());
		    
		    
			//paraMap.put("pw", AES256Util.strEncode(pw));
		    member.setPw(pw);
			this.memberFrontService.doInsertMember(member);
			
		} catch(Exception e) {
			e.printStackTrace();
			return new ModelAndView("error");
		}
		
		return mav;
	}
	
	
}
