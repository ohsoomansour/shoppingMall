package com.shoppingmall.toaf.intercept;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import com.shoppingmall.toaf.object.DataMap;


/**
 * 
 * @Package	 : com.shoppingmall.toaf.interceptor
 * @File	 : RequestInterceptor.java
 * @Author   : osm
 * @Date	 : 2024.6.19
 * @Explain  : 요청 인터셉터
 *
 */
@Component
public class RequestInterceptor implements MethodInterceptor {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	public Object invoke (MethodInvocation invocation) throws Throwable {

		logger.info("RequestInterceptor start");
		//#Joint Point:메서드 호출
		if (invocation.getMethod().getReturnType().equals(ModelAndView.class) ||
			invocation.getMethod().getReturnType().equals(String.class) ||
			invocation.getMethod().getReturnType().equals(Object.class)) {

			Class<?>[] params = invocation.getMethod().getParameterTypes();

			int mapIndex = -1;
			int reqIndex = -1;
			for (int idx = 0; idx < params.length; idx++) {
			//System.out.println("===> ["+idx+"]"+params[idx]);
				if (params[idx].equals(DataMap.class)) mapIndex = idx;
				if (params[idx].equals(HttpServletRequest.class)) reqIndex = idx;
			}

			if (mapIndex >= 0 && reqIndex >= 0) {
				HttpServletRequest request = (HttpServletRequest) invocation.getArguments()[reqIndex];
				DataMap dataMap = new DataMap();
					
				Enumeration<String> enumeration = request.getParameterNames();
				while (enumeration.hasMoreElements()) {
					String name = enumeration.nextElement();
					String[] vals = request.getParameterValues(name); //HttpServletRequest 객체를 사용하여 요청 파라미터를 읽어와

					if (vals == null) continue;
					//HTTP 요청 파라미터 처리: 요청 파라미터를 읽어와 DataMap 객체에 저장
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
					else {
						dataMap.putorg(name.replaceAll("[\\[\\] ]", ""), (vals.length > 1 || name.indexOf("[") > 0) ? vals : request.getParameter(name));

						if (logger.isDebugEnabled()) {
							logger.debug("param [ " +	name.replaceAll("[\\[\\] ]", "") + " ]\t: { " +
								(vals.length > 1 || name.indexOf("[") > 0 ? "[" + StringUtils.join(vals, ", ") + "]" : request.getParameter(name)) + " }");
						}
					}
				}

				if (request instanceof MultipartHttpServletRequest) {
					MultipartHttpServletRequest mprequest = (MultipartHttpServletRequest) request;

					Iterator<String> iterator = mprequest.getFileNames();
					while (iterator.hasNext()) {
						String name = iterator.next();
						List<MultipartFile> values = mprequest.getFiles(name);
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
