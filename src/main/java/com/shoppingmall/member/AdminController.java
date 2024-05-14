package com.shoppingmall.member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.shoppingmall.toaf.object.DataMap;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@RequestMapping(value="/admin")
@Controller
public class AdminController {
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private AdminService adminService;
	
	
	/* 
	 *@Author: osm
	 *@Date: 2024.5.9
	 *@Param: - 
	 *@return: ModelAndView
	 *@Function: 회원 리스트 조회
	*/
	@GetMapping("/memberList.do")
	public ModelAndView getMembers(@ModelAttribute ("paraMap") DataMap paraMap, HttpServletRequest request, HttpServletResponse response) {
		log.info("admin/memberList");
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/member/member");
		//System.out.println(this.adminServiceImpl.getMemberList());
		mav.addObject("members", this.adminService.getMemberList(paraMap));
		return mav;
	}
	
	/* 
	 *@Author: osm
	 *@Date: 2024.5.12
	 *@Param:  방법1. @ModelAttribute("seqno"):'일반 HTTP 요청 파라미터', 'seqno'은 실제 HTTP 요청의 파라미터 이름과는 다르며
	 *         방법2.@(@RequestParam("key1") String key1, , @RequestParam("key2") String key2 
	 *@return: ModelAndView
	 *@Function: 가입 승인 
	*/

	@RequestMapping(value="/signUpApproval.do")               
	public ModelAndView doApprovalofMembership(@ModelAttribute("seqno") String seqno) {
		ModelAndView mav = new ModelAndView("jsonView");
		System.out.println("가입승인 id:" + seqno);
		DataMap paraMap = new DataMap();
		paraMap.put("seqno", seqno);
		System.out.println("dataMap 객체:" + paraMap);
		mav.addObject("seqno", paraMap);
		adminService.updateSignUpApproval(paraMap);
		return mav;
	}
	

	@RequestMapping(value="/doTest.do") // HTTP 요청 파라미터를 객체로 바인딩
	public ModelAndView doTest(@ModelAttribute ("paraMap") DataMap paraMap, HttpServletRequest request, HttpServletResponse response) {   //int seqno
	
		ModelAndView mav = new ModelAndView("jsonView");
		System.out.println("doTest:" + paraMap);
		mav.addObject("status", "success");
        mav.addObject("message", "This is a JSON response!");
		//data.put("id", 'A');
		//mav.addObject("id", data);
		return mav;
	}
	
}
