package com.shoppingmall.payment;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class KaKaoPayService implements KaKaoPayServiceImpl {
	@Value("${kakaopay.admin-key}")
	private String ADMIN_KEY;
	
	
	private KakaoPayReadyResponse kReadyResponse;
	
	private HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		log.info("ADMIN_KEY =======>" + ADMIN_KEY);
		headers.add("Authorization", "KakaoAK " + ADMIN_KEY);
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		headers.add("Accept", "application/json"); 
		return headers;
	}
	
	/**
	 * @Date: 24.9.5
	 * @Description:
	 * - cid : 대리점 코드이고 '테스트 cid'는 TC0ONETIME를 사용
	 *   *docs 참고: https://developers.kakaopay.com/docs/payment/online/single-payment
	 *    테스트 결제는, 가맹점 코드로 "TC0ONETIME"와 "Secret key(dev)"를 통해 결제 호출이 가능합니다.
	 *  - item_name(상품명), quantity(상품수), total_amount
	 * @HttpEntity:  HTTP 요청 또는 응답의 바디와 헤더를 담는 컨테이너 역할
	 * @kReadyResponse: tid=T6d9696b4d6b11c541c8, next_redirect_pc_url=https://online-pay.kakao.com/mockup/v1/7282638d7c2c64334d0a1071a6d87e761256bffbb357758ddba93fe602be584d/info, created_at=2024-09-05T17:18:51
	 * */
//https://developers.kakaopay.com/docs/payment/online/single-payment
	public KakaoPayReadyResponse doKaokaoPayReady(Map<String, Object> params) {
		MultiValueMap<String, Object> payParams = new LinkedMultiValueMap<>();
		payParams.add("cid", "TC0ONETIME");
		payParams.add("partner_order_id", "KA20240905");
		payParams.add("partner_user_id", "affiliateMember_osm");
		payParams.add("item_name", params.get("item_name"));
		payParams.add("quantity", params.get("quantity"));
		payParams.add("total_amount", params.get("total_amount"));
		payParams.add("tax_free_amount", params.get("tax_free_amount"));
		payParams.add("approval_url", "http://localhost:8080/pay/success"); //결제 완료 페이지
		//[결제하기 버튼] -> http://localhost:8080/pay/success?pg_token=ff8556d9ac6e232070d0
		payParams.add("fail_url", "http://localhost:8080/pay/fail");
		payParams.add("cancel_url", "http://localhost:8080/pay/cancel");
		
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(payParams, this.getHeaders());
		RestTemplate template = new RestTemplate();
		String url = "https://kapi.kakao.com/v1/payment/ready";
		kReadyResponse = template.postForObject(
				url,
				requestEntity,
				KakaoPayReadyResponse.class
		);
		log.info("kReadyResponse: =======>" + kReadyResponse);
		return kReadyResponse;
	}
	
	@Transactional
	public KaokaoPayApproveResponse KakaopayApprove(String pgToken) {
	
		MultiValueMap<String, Object> payParams = new LinkedMultiValueMap<>();
		payParams.add("cid", "TC0ONETIME");
		payParams.add("tid", kReadyResponse.getTid()); 	//tid : 결제 고유번호 
		payParams.add("partner_order_id", "KA20240905");
		payParams.add("partner_user_id", "affiliateMember_osm");
		payParams.add("pg_token", pgToken);
		
		HttpEntity<Map> requestEntity = new HttpEntity<>(payParams, this.getHeaders());
		RestTemplate template = new RestTemplate();
		String url = "https://kapi.kakao.com/v1/payment/approve";
		
		KaokaoPayApproveResponse approvedResponse = template.postForObject(
	      url, 
	      requestEntity,
	      KaokaoPayApproveResponse.class
		);
		
		return approvedResponse;
				
	}
	
	
}
