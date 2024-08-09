package com.shoppingmall.post;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.shoppingmall.toaf.basemvc.BaseSvc;
import com.shoppingmall.toaf.object.DataMap;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(value="postgresqlTransactionManager", propagation= Propagation.REQUIRED, rollbackFor=Exception.class)
public class VuePostService extends BaseSvc<DataMap> {
	
	public List<DataMap> getAllPostsList(){
		return this.dao.selectListQuery("PostSQL.getAllPostsList", null);
	}
	
	public int doPublishPost(DataMap articleMap) throws IOException {
    //
    try {
    		//[파일 테이블] 
    		Object filesObject = articleMap.get("fileInfos");

    		if(filesObject instanceof Object) {
    				log.info("================isObjecT?" + filesObject+ "==========================="); 
    				@SuppressWarnings("unchecked")
						List<Map<String, Object>> files =  (List<Map<String, Object>>) articleMap.get("fileInfos");
    				log.debug("doPublishPost's Service ===============> " + files);
    				if(files != null && !files.isEmpty()) {
    						for (int i = 0 ; i < files.size(); i++) {
    								String reg_date = (String)files.get(i).get("regDate");
    								String original_file = (String) files.get(i).get("originalFileName");
    								String save_file = (String) files.get(i).get("saveFileName");
    								log.debug("doPublishPost's Service - Uploaded reg_data ===>" + reg_date);
    								log.debug("doPublishPost's Service - Uploaded originalName ===> : " + original_file);
    								log.debug("doPublishPost's Service - Uploaded saveName ===> : " + save_file);
    								//file_info 테이블에 저장
    								DataMap fileInfoMap = new DataMap();
    								fileInfoMap.put("reg_date", reg_date);
    								fileInfoMap.put("original_file", original_file);
    								fileInfoMap.put("save_file", save_file);
    								this.dao.insertQuery("", articleMap);
    								
    						}
    				}
    		}
    		log.info("articleMap=====================> " + articleMap); 
    	 //[파일 외 내용들 테이블] - article_no, 저장 날짜 
    		this.dao.insertQuery("PostSQL.doPublishPost", articleMap);

    		
    } catch (Exception e) {
    		System.out.println(e);
    }
		return 0;
	  
	}
	
}
