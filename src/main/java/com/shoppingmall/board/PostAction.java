package com.shoppingmall.board;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.shoppingmall.toaf.object.DataMap;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("post")
public class PostAction {
	
	@Autowired
	PostService postService;
	                      
	@RequestMapping("/list.do")
	public ModelAndView getContent() {
		ModelAndView mav = new ModelAndView();
		List<DataMap> postList = postService.getAllPostsList();
		log.info("postList" + postList);
		mav.addObject("posts", postList);
		mav.setViewName("/post/postList");
						
		return mav;
	}			
  
}
