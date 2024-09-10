package com.shoppingmall.secuser;


import java.util.Optional;
/**
 * @Explain: MemberDao는 제네릭 인터페이스, Optional<T> 타입의 결과를 반환합니다. T는 구체적인 타입으로 지정
 * */
public interface MemberDao<T> {
	Optional<T> getOneMemberByName(String user_name); //공통 메서드
	Object signUpFor(T user_name); //공통 메서드
}
