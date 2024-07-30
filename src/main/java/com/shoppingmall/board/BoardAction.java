package com.shoppingmall.board;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("board")
public class BoardAction {
	
	@Autowired
	BoardService boardService;
	
	@RequestMapping("/getAllBoardsList.do")
	public ModelAndView getContent() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/board/board");
		
		mav.addObject("postings","service 로직");
		
		return mav;
	}
	
}
