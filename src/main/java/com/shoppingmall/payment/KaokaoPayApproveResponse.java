package com.shoppingmall.payment;

import lombok.Data;

/*📦 요청에 필요한 정보 (필수만 *)
- cid 가맹점코드, 10자 (String)
- tid 결제 고유번호 (String)
- partner_order_id 가맹점 주문번호, 최대 100자 (String)
- partner_user_id 가맹점 회원, 최대 100자 (String)
- pg_token 결제 승인시 요청을 인증하는 토큰 (String)

 📦 KAKAO PAY가 응답해주는 정보
- aid 요청 고유번호 (String)
- tid 결제 고유번호 (String)
- cid 가맹점 코드, 10자 (String)
- sid 정기 결제용 id (String)
- partner_order_id 가맹점 주문번호, 최대 100자 (String)
- partner_user_id 가맹점 회원, 최대 100자 (String)
- payment_method_type 결제수단 (CARD or MONEY / String)
- item_name 상품명, 최대 100자 (String)
- quantity 상품수량 (Integer)
- created_at 결제 준비 요청 시각 (String)
- approved_at 결제 승인 시각 (String)
- amount 전체 결제 금액, 비과세 금액, 부가세 금액, 사용한 포인트 금액, 할인금액 (Amount)
 * 
 * */

@Data
public class KaokaoPayApproveResponse {
  private String aid;
  private String tid;
  private String cid;
  private String sid;
  private String partner_order_id;
  private String partner_user_id;
  private String payment_method_type;
  
  private String item_name;
  private String item_code;
  private Integer quantity;
  private String created_at;
  private String approved_at;

  private Amount amount;
}
