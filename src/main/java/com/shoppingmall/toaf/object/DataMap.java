package com.shoppingmall.toaf.object;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


import org.apache.commons.collections4.map.ListOrderedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <PRE>
 * 기  능	: VO Map
 * 파일명	: DataMap.java
 * 패키지	: com.ttmsoft.lms.object
 * 설  명	: Model 대신에 사용
 * </PRE>
 * 변경이력	: 
 * 2020.03.03	[PUDDING] - 최초작성
 * 
 */
@SuppressWarnings ({ "rawtypes" })
public class DataMap extends ListOrderedMap implements Map, Serializable {

	/** serialVersionUID */
	private static final long	serialVersionUID	= 6494663396504728352L;
	protected	Logger	logger	= LoggerFactory.getLogger(this.getClass());
//	private		ListOrderedMap	baseMap;
//	public DataMap () { baseMap = new ListOrderedMap(); }

	/**
	 * <PRE>
	 * 기  능	: Model의 null값을 입력값으로 변경
	 * 함수명	: nvl
	 * 설  명	: 콤마로 구분된 문자열 키에 대해 null 체크 후 입력 value값으로 변경
	 * </PRE>
	 * @param items - 콤마(,)로 구분된 문자열
	 * @param value - null 값을 대치할 Object 값
	 */
	@SuppressWarnings("unchecked")
	public void nvl (String items, Object value) {
		String[] list = items.split(",");
		for (int idx = 0; idx < list.length; idx++) {
			String key = list[idx].trim();
			if ("".equals(key)) continue;

			Object val = super.get(key);
			if (val == null || "".equals(val.toString())) {
				if (logger.isDebugEnabled()) logger.debug("NULL초기화 : nvl [ " + key + " ]\t<== { " + value + " }");
				super.put(key, value);
			}
		}
	}

	/**
	 * <PRE>
	 * 기  능	: 필수 Model 데이타 체크
	 * 함수명	: check
	 * 설  명	: 체크 통과시에 "" return, 데이타 미존재시에 key 필드명 return
	 * </PRE>
	 * 
	 * @param items - 콤마(,)로 구분된 문자열
	 * @return 체크통과:"", 오류발생:key필드명
	 */
	public String check (String items) {
		
		String[] list = items.split(",");
		for (int idx = 0; idx < list.length; idx++) {
			String key = list[idx].trim();
			if ("".equals(key)) continue;

			Object val = super.get(key);
			if (val == null || "".equals(val.toString())) {
				System.out.println("check error ===> " + key);
				if (logger.isDebugEnabled()) logger.debug("필수DATA check [ " + key + " ]\t==> invalid data!!");
				return key;
			}
		}
		return "";
	}

	/**
	 * <PRE>
	 * 기  능	: 
	 * 함수명	: get
	 * 설  명	: 
	 * </PRE>
	 * @param key
	 * @return
	 */
	@Override
	public Object get (Object key) {
		return (super.get(key) == null ? (String) "" : (Object) super.get(key));
	}

	/**
	 * <PRE>
	 * 기  능	: 
	 * 함수명	: getint
	 * 설  명	: 
	 * </PRE>
	 * @param key
	 * @return
	 */
	public int getint (String key) {
		return Integer.parseInt((super.get(key) == null ? "0" : super.get(key).toString()), 10);
	}

	/**
	 * <PRE>
	 * 기  능	: 
	 * 함수명	: getstr
	 * 설  명	: 
	 * </PRE>
	 * @param key
	 * @return
	 */
	public String getstr (String key) {
		return (super.get(key) == null ? "" : super.get(key).toString());
	}

	/**
	 * <PRE>
	 * 기  능	: 맵키를 소문자로 변환 
	 * 함수명	: put
	 * 설  명	: java.util.Map#put(K, V)와 다름 (K 값이 소문자)
	 * </PRE>
	 * @param key
	 * @param value
	 * @return
	 */
	@Override
	public Object put (Object key, Object value) {
		return (value == null) ? (String) "" : super.put(key.toString().toLowerCase(), value);
	}

	/**
	 * <PRE>
	 * 기  능	: 맵키값이 원본 그대로 사용 
	 * 함수명	: putorg
	 * 설  명	: java.util.Map#put(K, V)와 동일
	 * </PRE>
	 * @param key
	 * @param value
	 * @return
	 */
	public Object putorg (String key, Object value) {
		return (value == null) ? (String) "" : super.put(key, value);
	}

	/**
	 * <PRE>
	 * 기  능	: 맵키를 소문자로 변환
	 * 함수명	: putlower
	 * 설  명	:
	 * </PRE>
	 * @param key
	 * @param value
	 * @return
	 */
	public Object putlower (String key, Object value) {
		return (value == null) ? (String) "" : super.put(key.toLowerCase(), value);
	}

	/**
	 * <PRE>
	 * 기  능	: 맵키를 대문자로 변환 
	 * 함수명	: putupper
	 * 설  명	:
	 * </PRE>
	 * @param key
	 * @param value
	 * @return
	 */
	public Object putupper (String key, Object value) {
		return (value == null) ? (String) "" : super.put(key.toUpperCase(), value);
	}

	/**
	 * <PRE>
	 * 기  능	: 맵키를 카멜(camel)로 변환
	 * 함수명	: putcamel
	 * 설  명	:
	 * </PRE>
	 * @param key
	 * @param value
	 * @return
	 */
	public Object putcamel (String key, Object value) {
		return (value == null) ? (String) "" : super.put(toCamelCase(key), value);
	}

	/**
	 * <PRE>
	 * 기  능	: 맵키를 Camel => UnderScore로 변환 
	 * 함수명	: putunder
	 * 설  명	: java.util.Map#put(K, V)와 다름 (K 값이 소문자)
	 * </PRE>
	 * @param key
	 * @param value
	 * @return
	 */
	public Object putunder (String key, Object value) {
		return (value == null) ? (String) "" : super.put(toUnderScore(key), value);
	}

	/**
	 * underScore -> camelCase 표기법으로 변경
	 * 
	 * @param underScore - 변경문자열
	 * @return
	 */
	private static String toCamelCase (String underScore) {
		StringBuffer result = new StringBuffer();
		boolean isUpper = false;
		String lowStr = underScore.toLowerCase();

		for (int i = 0; i < lowStr.length(); i++) {
			char idxChar = lowStr.charAt(i);
			if (idxChar == '_') isUpper = true;
			else {
				if (isUpper) { idxChar = Character.toUpperCase(idxChar); isUpper = false; }
				result.append(idxChar);
			}
		}
		return result.toString();
	}

	/**
	 * camelCase -> underScore 표기법으로 변경
	 * 
	 * @param str - 변경문자열
	 * @return
	 */
	private static String toUnderScore (String camelCase) {
		String result = "";
		for (int i = 0; i < camelCase.length(); i++) {
			char idxChar = camelCase.charAt(i);
			if (i > 0 && Character.isUpperCase(idxChar)) result = result.concat("_");
			result = result.concat(Character.toString(idxChar).toLowerCase());
		}
		return result;
	}

	

}