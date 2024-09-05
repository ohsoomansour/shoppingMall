package com.shoppingmall.payment;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/pay")
public class PayController {
	@Autowired
	PayService payService;
	
	/**
	 * @에러1. org.springframework.web.client.HttpClientErrorException$Unauthorized: 401 Unauthorized: [no body]] with root cause
	 *  - 문제: '인증 자격 증명'을 제공하지 않았을 경우 
	 *  - 원인 추정1.: approval_url, cancel_url, fail_url에 사용되는 도메인에 대해 플랫폼에 정상등록되지 않아 발생되는에러
	 *     카카오페이 개발자 콘솔에서 리다이렉트 가능한 도메인을 사전 등록
	 * @에러2. org.springframework.web.client.HttpClientErrorException$BadRequest: 400 Bad Request: "{"msg":"item_name can't be null.","code":-2}"
	 * */
	@PostMapping("/ready")
	@ResponseBody
	public KakaoPayReadyResponse KakaoPayReady(@RequestBody Map<String, Object> params) {
		//{item_name=honey_essence, quantity=10, total_amount=120000, tax_free_amount=0}
		log.info(" KakaoPayReady's controller ======> "+ params);
		log.info("item_name:" + params.get("item_name"));
		log.info("quantity:" + params.get("quantity"));
		log.info("total_amount:" + params.get("total_amount"));
		log.info("tax_free_amount:" + params.get("tax_free_amount"));
		KakaoPayReadyResponse readyResponse = payService.doKaokaoPayReady(params);
		
		return readyResponse;
	}
	@RequestMapping("/success")						  //pgToken 알아서 들어온다
	public String  KakaoPayApprove(@RequestParam("pg_token") String pgToken) {
		KaokaoPayApproveResponse approveResponse = payService.KakaopayApprove(pgToken);
		log.info("approveResponse ========>" + approveResponse);
		//http://localhost:8080/payment/approval
		return "redirect:http://localhost:8088/#/product";
	}
	@RequestMapping("/fail")
	public void  KakaoPayFail() {
	   log.info("카카오 페이 결제에 실패하였습니다!");
		
	}
	@RequestMapping("/cancel")
	public void KakaoPayCancel() {
		
	}
	
	
}
