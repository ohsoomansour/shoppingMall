package com.shoppingmall.admin;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.shoppingmall.toaf.object.DataMap;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping(value="/admin")
@Controller
public class AdminAction {
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	
	@Autowired
	AdminService adminService;
	
	
	/** 
	 *@Author: osm
	 *@Date: 2024.5.9
	 *@Param: - 
	 *@return: ModelAndView
	 *@Function: 회원 리스트 조회
	*/
	@GetMapping("/userList.do")
	public ModelAndView getMembers(@ModelAttribute ("paraMap") DataMap paraMap, HttpServletRequest request, HttpServletResponse response) {
		log.info("admin/memberList");
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/member/member");
		//System.out.println(this.adminServiceImpl.getMemberList());
		mav.addObject("users", this.adminService.getMemberList(paraMap));
		return mav;
	}
	
	/** 
	 *@Author: osm
	 *@Date: 2024.5.12
	 *@Param:  방법1. @ModelAttribute("seqno"):'일반 HTTP 요청 파라미터', 'seqno'은 실제 HTTP 요청의 파라미터 이름과는 다르며
	 *         방법2.@(@RequestParam("key1") String key1, , @RequestParam("key2") String key2 
	 *@return: ModelAndView
	 *@Function: 가입 승인 
	*/

	@RequestMapping(value="/signUpApproval.do")               
	public ModelAndView doApprovalofMembership(@ModelAttribute("u_id") int no) {
		ModelAndView mav = new ModelAndView("jsonView");
		System.out.println("가입승인 id:" + no);
		DataMap paraMap = new DataMap();
		paraMap.put("u_id", no);

		mav.addObject("u_id", paraMap);
		adminService.updateSignUpApproval(paraMap);
		return mav;
	}
	
  /**
   * @Date: 2024.08.07
   * @Target : 반환 값이 ModelAndView에서 'jsonView' 또는 DataMap 가능   
   * @Param  : @ModelAttribute ("paraMap") DataMap paraMap
   *           @RequestParam Map<String, Object> paramMap
   *             - GET 요청, URL 쿼리 스트링의 경우 매핑된다.
   *             - 
   *           @RequestBody Map<String, Object> paramMap
   *             
   *             - POST 요청, Request BODY 
   *             - 헤더의 content-type : 
   *               <data가javascript객체경우>
   *               headers: {
                    "Content-Type": "application/json", 
                   },
                   
                   <form의경우>
                   heasers: {
                   	"Content-Type" : 'multipart/form-data'
                   }
                    
   *           @RequestBody DataMap<String, Object> paramMap
   *            - 개념: parameter(=paraMap) should be bound to the body of the web request 

   * @ReturnType1 :  @ResponseBody + DataMap (toaf)
   * @ReturnType2 :  
   * */
	@RequestMapping("/doTest.do") // HTTP 요청 파라미터를 객체로 바인딩
	@ResponseBody
	public DataMap doReturnTestAction(@RequestBody DataMap paramMap, HttpServletRequest request, HttpServletResponse response) {   //int seqno
		//방법1.	
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("status", "success");
		mav.addObject("message", "This is a JSON response!");
		//방법2.
		log.info("parameter ======>"+ paramMap);
		DataMap resultMap = new DataMap();
		resultMap.put("test", "OK");
	
		return resultMap;
	}
	
}
