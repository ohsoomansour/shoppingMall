package com.shoppingmall.post;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.shoppingmall.toaf.object.DataMap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ModelAttribute;
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
	
	/**
	 * @Function : 게시판 글 작성 
	 * @Param : @ModelAndView("paraMap") DataMap dataMap으로 작성!!
	 * @Return : -
	 * @Description : *Action 클래스 또는 메서드는 AOP로 인해 인터 셉트
	 * */
	// 매핑 되는 지 확인!!
	@RequestMapping("/writing")
	public void doWriteContents(@ModelAttribute("paraMap") DataMap dataMap ) {
		
	}
	
	
}
