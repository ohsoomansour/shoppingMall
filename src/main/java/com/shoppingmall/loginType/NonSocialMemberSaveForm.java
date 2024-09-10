package com.shoppingmall.loginType;

import lombok.Data;
/**
 * @DTO: Controller와 Service 사이에서 데이터를 주고받을 때 사용되며, 클라이언트와 서버 간의 데이터를 전달
 *  - 순수한 데이터 구조로, 로직이 포함되지 않고 단순히 데이터를 전달 + 직렬화 가능하게 설계되어, 데이터를 효율적으로 전달
 *  - Getter/Setter 메서드를 통해 데이터에 접근
 * @Model 객체는 데이터베이스의 테이블과 직접 매핑되는 객체(엔티티)로도 사용
 * @VO: 불변성이 중요한 특성, getter만 가능 
 * */

//Q. DTO와 model, VO의 차이
@Data
public class NonSocialMemberSaveForm {
	private int login_type;
	
}
