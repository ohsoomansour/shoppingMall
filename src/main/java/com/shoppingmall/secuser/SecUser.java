package com.shoppingmall.secuser;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Explain: DB에 있는 정보를 가져온다? 
 * */

@NoArgsConstructor(access= AccessLevel.PROTECTED)
@Data
public class SecUser implements UserDetails {

	private Long id;
	private String u_email;
	private String u_pw;
	
	//사용자의 id반환 
	@Override
	public String getUsername(){
		return u_email;
	}
	/**
	 * @getAuthorities: 인증된 사용자가 가진 권한 목록을 제공하기 위해 UserDetails 인터페이스의 getAuthorities 메서드 구현
	 * @List.of(): Java9부터 제공되는 메서드, 불변 리스트를 생성
	 * @SimpleGrantedAuthority("user"): 사용자가 가지고 있는 권한, "user"라는 권한을 가진 것으로 설정
	 * @return: user라는 권한을 가진 사용자가 권한 목록을 반환
	 * */
	//사용자가 가진 권한(역할)을 GrantedAuthorigy '객체를 list'로 반환  
	@Override 
	public Collection<? extends GrantedAuthority> getAuthorities(){
		return List.of(new SimpleGrantedAuthority("user"));
	}
	@Override
	public String getPassword() {
		return u_pw;
	}
	//계정 만료 여부 확인
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	//계정 잠금 여부 반환
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	//패스워드 만료 여부 확인
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	//계정 사용 가능 여부 반환 
	@Override
	public boolean isEnabled() {
		return true;
	}
		
}
