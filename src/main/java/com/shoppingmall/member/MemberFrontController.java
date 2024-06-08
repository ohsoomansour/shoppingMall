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
	public ModelAndView doMemberJoin(@ModelAttribute Member member) {
		
		ModelAndView mav = new ModelAndView("jsonView");
		DataMap paraMap = new DataMap();

		/**/
		try {

			String id = member.getId(); 

			String pw =  member.getPw().toString();;    
			String member_type = member.getMember_type();
			String user_name = member.getUser_name();
			String user_email1 = member.getUser_email1();
			String user_email2 = member.getUser_email2();
			String address = member.getAddress();
			String user_mobile_no = member.getUser_mobile_no();
			
			paraMap.put("id", id);
			paraMap.put("pw", AES256Util.strEncode(pw));
			paraMap.put("user_name", user_name);
			paraMap.put("user_email1", user_email1);
			paraMap.put("user_email2", user_email2);
			paraMap.put("address", address);
			paraMap.put("member_type", member_type);
			paraMap.put("user_mobile_no", user_mobile_no);
			this.memberFrontService.doInsertMember(paraMap);
			
		} catch(Exception e) {
			e.printStackTrace();
			return new ModelAndView("error");
		}
		
		return mav;
	}
	
	
}
