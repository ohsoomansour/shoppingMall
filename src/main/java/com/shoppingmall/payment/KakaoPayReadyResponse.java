package com.shoppingmall.payment;


import lombok.Data;

/**
 * @DATE: 24.9.5
 * @Desc :
 * - tid : 결제 고유 번호 (String)
 * - next_redirect_pc_url : 요청한 클라이언트가 PC 웹일 경어, QR코드로 이동하는 url (String)
 * - created_at : 결제 준비 요청 시간(Date)
 * */

@Data
public class KakaoPayReadyResponse {
  private String tid;
  private String next_redirect_pc_url;   
  private String created_at;
}
