package com.shoppingmall.toaf.intercept;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.shoppingmall.toaf.object.DataMap;

//import javax.servlet.http.HttpServletRequest; 2024.08.05 수정
import jakarta.servlet.http.HttpServletRequest;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;


/**
 * 
 * @Package	 : com.shoppingmall.toaf.interceptor
 * @File	 : RequestInterceptor.java
 * @Author   : osm
 * @Date	 : 2024.6.19
 * @UpdatedDate : 2024.08.04
 * @Explain  : 요청 인터셉터
 *  - 조인 포인트(=*Action 클래스 실행 ) : 특정 지점, *Action 클래스 또는 메서드 호출 시점
 *  - Q. invoke 메서드는 언제 호출? A. 정된 조인 포인트(Join Point)에 해당하는 메소드가 호출될 때 자동으로 호출
 *  - MethodInvocation 객체를 매개변수로 받아들이며, 이 객체는 호출되는 메소드에 대한 정보'를 포함
 *  - invocation.proceed() : 실제 메소드 호출을 대리하여 실행
 */
@Component
public class RequestInterceptor implements MethodInterceptor {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    
	public Object invoke (MethodInvocation invocation) throws Throwable {

		logger.info("RequestInterceptor start");
		//invocation.getMethod().getReturnType().equals(DataMap.class)
		//#Joint Point: 실제 타겟 메서드 호출
		if (invocation.getMethod().getReturnType().equals(ModelAndView.class) ||
			invocation.getMethod().getReturnType().equals(String.class) ||
			invocation.getMethod().getReturnType().equals(Object.class) ||
			invocation.getMethod().getReturnType().equals(DataMap.class)||
			invocation.getMethod().getReturnType().equals(void.class)
			) {

		  logger.info("invocation.getMethod():" + invocation.getMethod()); //AdminAction.doReturnTestAction
			Class<?>[] params = invocation.getMethod().getParameterTypes();
			logger.info("params:"+ Arrays.toString(params)); // 배열  [] 타입
			//[com.shoppingmall.toaf.object.DataMap dataMap, jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response]
			logger.info("actual params"+Arrays.toString(invocation.getMethod().getParameters()));
			logger.info("invocation.getMethod().getReturnType().equals(ModelAndView.class)"+invocation.getMethod().getReturnType().equals(ModelAndView.class));
			logger.info("invocation.getMethod().getReturnType().equals(String.class)"+invocation.getMethod().getReturnType().equals(String.class));
			logger.info("invocation.getMethod().getReturnType().equals(Object.class)"+invocation.getMethod().getReturnType().equals(Object.class));
			logger.info("invocation.getMethod().getReturnType().equals(DataMap.class)"+invocation.getMethod().getReturnType().equals(DataMap.class));
			int mapIndex = -1;
			int reqIndex = -1;
			for (int idx = 0; idx < params.length; idx++) {
			/*
			 * ===> [0]class com.shoppingmall.toaf.object.DataMap
         ===> [1]interface jakarta.servlet.http.HttpServletRequest
         ===> [2]interface jakarta.servlet.http.HttpServletResponse
			 * */		
			System.out.println("===> ["+idx+"]"+params[idx]);
				if (params[idx].equals(DataMap.class)) mapIndex = idx; // 여기에 주목!!
				if (params[idx].equals(HttpServletRequest.class)) reqIndex = idx;  // 0 1
			}
			

			if (mapIndex >= 0 && reqIndex >= 0) {
				HttpServletRequest request = (HttpServletRequest) invocation.getArguments()[reqIndex];
				logger.info("invocation.getArguments():"+invocation.getArguments());
				DataMap dataMap = new DataMap();
					
				Enumeration<String> enumeration = request.getParameterNames();
				logger.info("enumeration ===>" + enumeration);
				while (enumeration.hasMoreElements()) {
					String name = enumeration.nextElement(); //HTTP 요청에서 파라미터 이름을 읽어오는 코드
					String[] vals = request.getParameterValues(name); //HttpServletRequest 객체를 사용하여 요청 파라미터를 읽어와
          
					if (vals == null) continue; 
				// ==================== 여기에서 컷  ===================
					//HTTP 요청 파라미터 처리: 요청 파라미터의 이름이 "models"의 경우, models에 json ArrayList<DataMap> 객체 저장
					if ("models".equals(name)) {
						JSONArray jsonList = (JSONArray) JSONSerializer.toJSON(request.getParameter(name));
						List<DataMap> gridList = new ArrayList<DataMap>();
						int idx = 0;
						for (Object obj : jsonList) {
							JSONObject jsonObj = (JSONObject) obj;
							gridList.add((DataMap) JSONObject.toBean(jsonObj, DataMap.class));
							if (logger.isDebugEnabled()) logger.debug("GRID models [" + idx + "]\t: " + jsonObj.toString());
							idx++;
						}
						dataMap.putorg(name, gridList);
					}
					//json 제외 파라미터는 보통 아래의 로직에서 처리, 정규 표현식([]와 빈 값은 ""로 대체) 
					else {
						logger.info("============ RequestInterceptor-dataMap설정 ============");
						dataMap.putorg(name.replaceAll("[\\[\\] ]", ""), (vals.length > 1 || name.indexOf("[") > 0) ? vals : request.getParameter(name));

						if (logger.isDebugEnabled()) {
							logger.debug("param [ " +	name.replaceAll("[\\[\\] ]", "") + " ]\t: { " +
								(vals.length > 1 || name.indexOf("[") > 0 ? "[" + StringUtils.join(vals, ", ") + "]" : request.getParameter(name)) + " }");
						}
					}
				}
				// @Explain : 주로 폼 데이터에서 파일 업로드, 멀티파트 요청 		
				if (request instanceof MultipartHttpServletRequest) {
				  logger.info("============ RequestInterceptor - Multipart request 설정 ============");	
					MultipartHttpServletRequest mprequest = (MultipartHttpServletRequest) request;
					// 	`ArrayList`, `LinkedList`, `HashSet에서 컬렉션 사용 가능 
					Iterator<String> iterator = mprequest.getFileNames();
					while (iterator.hasNext()) {
					logger.info("===========  mprequest.getFileNames() OK ========= ");
						String name = iterator.next();
						List<MultipartFile> values = mprequest.getFiles(name);
						logger.info("========== name:" + name+ "========== ");
						if (values == null || values.isEmpty()) continue;
						
						dataMap.putorg(name.replaceAll("[\\[\\] ]", ""), values);

						if (logger.isDebugEnabled()) {
							String names = "{ ";
							for (int idx = 0; idx < values.size(); idx++) {
								names += (idx == 0 ? "" : ", ") + values.get(idx).getOriginalFilename();
							}
							names += " }";
							logger.debug("upload [ " + name + " ]\t: " + names);
						}
					}
				}
				logger.debug("paraMap : " + dataMap);
				invocation.getArguments()[mapIndex] = dataMap;
			}
		}

		return invocation.proceed();
	}
}
