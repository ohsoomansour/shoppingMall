package com.shoppingmall.post;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
		//protected final Logger logger = LoggerFactory.getLogger(this.getClass());
		
	 @Autowired
	 VuePostService postService;
	
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
		 * @throws IOException 
     * @FileInfo 클래스 : 파일의 이름, 경로, 크기, 생성 날짜, 수정 날짜 등과 같은 속성
		 * 								 특정 파일에 대한 정보를 담는 사용자 정의 클래스
		 * @지네릭Type : 타입 안정성, cast 문제 측면에서 좋다.  
		 *  List<Map<String, Object>> fileInfos = new ArrayList<Map<String , Object>>();
		 *  -> List<Map<String, Object>> files =  (List<Map<String, Object>>) articleMap.get("fileInfos");
		 *  -> 틀린 경우1. List<Map<?, ?>> files =  (List<Map<String, Object>>) articleMap.get("fileInfos"); 에러발생!
		 *     why ? 반환은 List<Map<String, Object>> 캐스팅하고 있지만 나중에는 List<Map<?, ?>> files로 사용 
		 *           타입 안정성 측면에서 좋지 못하고 사용도 안됨! 
		 *  -> 맞는 경우1. 제네릭 사용으로 생성 -> 다음 사용 적합한 타입으로 캐스팅  
		 *     VuePostAction - List<Map<?, ?>> fileInfos = new ArrayList<Map<?, ?>>();
		 *     PostService - List<Map<String, Object>> files =  (List<Map<String, Object>>) articleMap.get("fileInfos");  
		 *     
		 * */
		
		@PostMapping("/vuePost/pubPost.do")
		public void postData(@ModelAttribute("paraMap") DataMap articleMap, HttpServletRequest request, HttpServletResponse response) throws IOException{
				log.info("===== pubPost's Controller - paraMap ====> " + articleMap + "===============");
				String realPath = "C:\\Users\\USER\\Desktop\\osm\\file_upload";
			  String today = new SimpleDateFormat("yyMMdd").format(new Date());
			  File folder = new File(realPath);
			  if(!folder.exists()) {
			  		folder.mkdirs();
			  }
			  boolean isList = articleMap.get("files") instanceof List;
			  if(isList) {
			  		@SuppressWarnings("unchecked")
			  		List<MultipartFile> files  =   (List<MultipartFile>) articleMap.get("files");
			  		List<Map<?, ?>> fileInfos = new ArrayList<Map<?, ?>>();
			  		for(MultipartFile mfile: files) {
			  				Map<String, Object> fileInfo = new HashMap<>();
			  				String originalFileName = mfile.getOriginalFilename();
			  				
			  				if(!originalFileName.isEmpty()) {
			  						String saveFileName = UUID.randomUUID().toString()
			  										+ originalFileName.substring(originalFileName.lastIndexOf('.'));
			  						fileInfo.put("regDate", today);
			  						fileInfo.put("originalFileName", originalFileName);
			  						fileInfo.put("saveFileName", saveFileName);
			  						mfile.transferTo(new File(folder, saveFileName)); // 지정된 경로로 파일 이동
			  				}
			  				log.info("fileInfo =====================>" + fileInfo);
			  				fileInfos.add(fileInfo);
			  				log.info("fileInfos =====================>" + fileInfos); 
			  		}
			  		articleMap.putorg("fileInfos", fileInfos);
			  }
			  int cnt = postService.doPublishPost(articleMap);
	
		}
		
		
}
