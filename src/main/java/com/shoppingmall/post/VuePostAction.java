package com.shoppingmall.post;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shoppingmall.toaf.object.DataMap;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 *  @Explain : Vue.js는 클라이언트 사이드 프레임워크 일반적으로 REST API 1
 * */

@Slf4j
@RestController
public class VuePostAction {
	 
		
	 @Autowired
	 PostService postService;
	
	 /**
	 * @Function : 게시판 리스트 조회
	 * @Param : - 
	 * @Return : view()
	 * */
		
		@GetMapping("/vuePost/list")
		public Object getContentAction() {
			List<DataMap> postList = postService.getAllPostsList();
			log.info("postList" + postList);
			DataMap postResult = new DataMap();
			postResult.put("postList", postList);				
			return postResult;
		}
		
		/**
		 * @Function : 게시판 리스트 조회
		 * @Param : p_title, p_contents,  
		 * @Return : - 
		 * */
		
		@PostMapping("/vuePost/pubPost.do")
		public Object postData(@ModelAttribute("paraMap") DataMap paraMap, HttpServletRequest request, HttpServletResponse response){
				log.info("paraMap ====> " + paraMap);
				
				DataMap result = new DataMap();
				result.put("key1", "아나! 성공!!");
				return result;
		}
		
		
}
