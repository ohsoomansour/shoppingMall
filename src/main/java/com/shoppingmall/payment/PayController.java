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

import com.shoppingmall.toaf.object.DataMap;

import jakarta.servlet.http.HttpSession;
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
	/**
	 * @Function: Open API에서 성공 후 /payment/success 경로로 request 요청되는 기능  
	 * @param : pgToken 
	 * @return: 승인된 값 *KaokaoPayApproveResponse 타입 참조 
	 * @Description: 영수증 만드는 값 반환 
	 * @추천x방법1. 쿼리 스트링으로 반환하는 것은 결제 내역 전부를 넘겨주는 것은 옳지 못함, aid(요청 고유번호)approvedResponseByAid 또는 tid 
	 * @추천o방법2. HttpSession session -> session.setAttribute("approveResponse", approveResponse);
	 * @보완코드: 스프링 시큐리티로 사용자 계정이 인증이 전제 조건 
	 * */
	@GetMapping("/success")						  //pgToken 알아서 들어온다
	public String KakaoPayApprove(@RequestParam("pg_token") String pgToken, HttpSession session) {
		KaokaoPayApproveResponse approvedResponse = payService.KakaopayApprove(pgToken);
		log.info("approveResponse ========>" + approvedResponse);
		//쿼리 스트링으로 결제 고유번호(Aid)값 넘겨준다 ? -> 프론트에서 tid와 getBill했을 때 일치하면 보여주고  
		String appprovedAid = "approvedAid_" + approvedResponse.getAid();
		log.info("path:/pay/success =======> " + appprovedAid);
		session.setAttribute(appprovedAid, approvedResponse);
		return "redirect:http://localhost:8088/#/payment/approval?aid=" + approvedResponse.getAid();
	}
	@GetMapping("/getMyBill")
	@ResponseBody
	public Object getKakaoPayBill(@RequestParam("aid") String aid,HttpSession session) {
		try {
			log.info(" QueryString aid ====>" + aid);
			KaokaoPayApproveResponse approvedResponseByAid = (KaokaoPayApproveResponse) session.getAttribute("approvedAid_" + aid);
			log.info("[ path:/pay/getMyBill ]=============> " + approvedResponseByAid);
			/*
			DataMap payMap = new DataMap();
			payMap.put("payResult", approvedResponseByAid);
			return payMap;*/
			return approvedResponseByAid;
			
		} catch(Exception e) {
			e.printStackTrace();
			DataMap failMap = new DataMap();
			return failMap;
		}
	}
	
	
	@GetMapping("/fail")
	public String  KakaoPayFail() {
	   log.info("카카오 페이 결제에 실패하였습니다!");
		return "redirect:http://loalhost:8088/#/payment/fail";
	}
	@GetMapping("/cancel")
	public String KakaoPayCancel() {
		log.info("카카오 페이 결제가 취소되었습니다.");
		return "redirect:http://localhost:8088/#/payment/cancel";
	}
	
	
}
