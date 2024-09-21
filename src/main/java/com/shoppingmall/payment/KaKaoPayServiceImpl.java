package com.shoppingmall.payment;

import java.util.Map;


import org.springframework.transaction.annotation.Transactional;

public interface KaKaoPayServiceImpl {
	public KakaoPayReadyResponse doKaokaoPayReady(Map<String, Object> params);
	
	@Transactional
	public KaokaoPayApproveResponse KakaopayApprove(String pgToken);
;
}
