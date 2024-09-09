package com.shoppingmall.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 *@Data:@Getter+@Setter+@RequiredArgsConstructor+@ToString+@EqualsAndHashCode. 
 *@RequiredArgsConstructor: final이나 @NonNull로 선언된 필드만을 초기화하는 생성자가 생성 
 *@ToString: toString() 메서드를 자동으로 생성
 * - 기본: 모든 필드 포함 
 * - 제외: @ToString(exclude = "password")
 *@AllArgsConstructor: 모든 필드를 파라미터로 받는 생성자가 생성 
 * */

@Data
@AllArgsConstructor
public class TokenDto {
	private String token;  // JWT 토큰 값만 포함
}
