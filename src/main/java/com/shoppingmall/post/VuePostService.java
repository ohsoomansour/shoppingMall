package com.shoppingmall.post;

import java.io.IOException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.shoppingmall.toaf.basemvc.BaseSvc;
import com.shoppingmall.toaf.object.DataMap;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(value="postgresqlTransactionManager", propagation= Propagation.REQUIRED, rollbackFor=Exception.class)
public class VuePostService extends BaseSvc<DataMap> implements VuePostServiceImpl {
	
	public List<DataMap> getAllPostsList(){
		return this.dao.selectListQuery("PostSQL.getAllPostsList", null);
	}
	
  public int doPublishPost(DataMap articleMap) {
  	int a = this.dao.insertQuery("PostSQL.doPublishPost", articleMap);
  	return a;
  }

	public int doSaveFile(DataMap articleMap) throws IOException {
    //
    try {   
    	//[파일 외 내용들 테이블] - article_no, 저장 날짜 
      
    	//[파일 테이블] 
    	Object filesObject = articleMap.get("fileInfos");
			if(filesObject instanceof Object) {
				log.info("================ isObjecT?" + filesObject + "==========================="); 
				
			  int u_id = articleMap.getint("u_id");
			  log.info("u_id ===========================> " + u_id);

				DataMap fileinforms  = new DataMap();
			  //Map<String, Object> fileInforms = new HashMap<>();
				fileinforms.put("u_id", u_id);
				fileinforms.put("fileinforms", articleMap.get("fileInfos"));
				log.debug("fileinforms ===============> " + fileinforms);
				return this.dao.insertQuery("PostSQL.doSaveFiles",fileinforms);   				  	 					
			}  	  	          		
    } catch (Exception e) {
      log.error("Error in doSaveFile" + e);
    	System.out.println(e);
    }
			return 0;
	}
	
}
