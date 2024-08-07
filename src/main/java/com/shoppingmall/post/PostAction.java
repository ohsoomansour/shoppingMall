package com.shoppingmall.post;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.shoppingmall.toaf.object.DataMap;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("post")
public class PostAction {
	
	@Autowired
	PostService postService;
	
	/**
	 * @Function : 게시판 리스트 조회
	 * @Param : - 
	 * @Return : view(postList.jsp)
	 * */
	
	@RequestMapping("/list.do")
	public ModelAndView getContent() {
		ModelAndView mav = new ModelAndView();
		List<DataMap> postList = postService.getAllPostsList();
		log.info("postList" + postList);
		mav.addObject("posts", postList);
		mav.setViewName("/post/postList");
						
		return mav;
	}
	
	
	@RequestMapping("/getBulletinBoardTopub.do")
	public ModelAndView getBoardBulletinTopub() {
			ModelAndView mav = new ModelAndView();
			mav.setViewName("/post/bulletinBoard");
			return mav;
	}
	/**
	 * @Function : 게시판 글 작성 실행
	 * @Param : 1) @ModelAttribute("paraMap") DataMap dataMap으로 작성!!
	 *              - 주로 폼 데이터의 단순 키-값 쌍을 객체에 매핑이고 SSR(Server Side Rendering)
	 *              - "paraMap"은 명시적으로 모델 어트리뷰트의 이름 
	 *              - 실행 순서 : 컨트롤러가 실행 -> 스프링, 이 paraMap 이름을 가진 모델의 어트리뷰트를 파라미터(dataMap)로 전달
	 *   				
	 *   				2) @RequestBody
	 *   				3) @RequestParam("u_email") String u_email,  @RequestParam('file') MultipartFile file  형식으로 받음 
	 * @Return : -
	 * @Description : *Action 클래스 또는 메서드는 AOP로 인해 인터 셉트
	 *   
	 *  
	 * */
	// 매핑 되는 지 확인!!
	@RequestMapping("/pubPost.do")
	public ModelAndView doWriteContentsAction(@ModelAttribute("paraMap") DataMap dataMap,  HttpServletRequest request, HttpServletResponse response
          ) {
		//log.info("u_email ====> " + u_email);
		//log.info("file ====> " + file);
		log.info("paraMap ====> " + dataMap);
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/post/list");

		return mav;
	}
	
	
	
	
	
}
