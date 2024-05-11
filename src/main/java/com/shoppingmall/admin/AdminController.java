package com.shoppingmall.admin;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.shoppingmall.admin.model.Member;
import com.shoppingmall.service.AdminServiceImpl;
import com.shoppingmall.toaf.object.DataMap;

@RequestMapping(value="/admin")
@Controller
public class AdminController {
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private AdminServiceImpl adminServiceImpl;
	
	@GetMapping("/memberList.do")
	public ModelAndView getMembers() {
		log.info("admin/memberList");
		ModelAndView mav = new ModelAndView();
		mav.setViewName("member");
		//System.out.println(this.adminServiceImpl.getMemberList());
		mav.addObject("members", this.adminServiceImpl.getMemberList());
		return mav;
	}
	
	/*##URL Parameter 디버깅##: 
	 *@ModelAttribute: '일반 HTTP 요청 파라미터'
	 *@param:  @ModelAttribute("seqno"):"seqno"은 실제 HTTP 요청의 파라미터 이름과는 다르며
	*/
	@RequestMapping(value="/signUpApproval.do") // HTTP 요청 파라미터를 객체로 바인딩                
	public ModelAndView doApprovalofMembership(@ModelAttribute("seqno") int id) {   //int seqno
		adminServiceImpl.updateSignUpApproval(id);
		Map<String, Character> data = new HashMap<>();
		System.out.println("가입승인 id:" + id);
		//ModelAndView mav = new ModelAndView("member");
		ModelAndView mav = new ModelAndView("jsonView");
		mav.setViewName("member");
		mav.addObject("status", "success");
        mav.addObject("message", "This is a JSON response!");
		data.put("id", 'A');
		mav.addObject("id", data);
		return mav;
	}
	
	@ResponseBody   //없어도 가능함
	@RequestMapping(value="/doTest.do") // HTTP 요청 파라미터를 객체로 바인딩
	public ModelAndView doTest() {   //int seqno
		
		Map<String, Character> data = new HashMap<>();

		 
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("status", "success");
        mav.addObject("message", "This is a JSON response!");
		//data.put("id", 'A');
		//mav.addObject("id", data);
		return mav;
	}
	
}
