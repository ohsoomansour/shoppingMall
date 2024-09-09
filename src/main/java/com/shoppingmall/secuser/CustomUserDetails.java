package com.shoppingmall.secuser;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
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


@Data
public class CustomUserDetails implements UserDetails, Serializable  {

	private String id;	// DB에서 PK 값
    private String loginId;		// 로그인용 ID 값
    private String password;	// 비밀번호
    private String email;	//이메일
    private boolean emailVerified;	//이메일 인증 여부
    private boolean locked;	//계정 잠김 여부
    private String nickname;	//닉네임
    private Collection<GrantedAuthority> authorities;	//권한 목록
    public CustomUserDetails(Long authId, String userEmail, String userPw, boolean emailVerified,boolean locked) {
        this.id = String.valueOf(authId);
        this.email = userEmail;
        this.password = userPw;
        this.emailVerified = emailVerified;
        this.locked = !locked;
    }
	
	//사용자의 id반환 
	@Override
	public String getUsername(){
		return id;
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
		return Collections.emptyList();
	}
	@Override
	public String getPassword() {
		return password;
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
