package com.shoppingmall.board;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("board")
public class BoardAction {
	
	@RequestMapping("/getContent.do")
	public ModelAndView getContent() {
		ModelAndView mav = new ModelAndView();
		
		return mav;
	}
	
}
