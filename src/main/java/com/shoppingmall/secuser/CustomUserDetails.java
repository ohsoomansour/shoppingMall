package com.shoppingmall.secuser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;


/**
 * @CreationDate:24.9.10
 * @Explain: UserDetails타입의 멤버, DB에 있는 정보 -> 
 * @매핑시점: AuthenticationProvider의 retrieveUser를 호출해서 UserDetails 타입의 '멤버'를 로드했을 때 
 * @Collection타입: 자바의 모든 컬렉션 클래스(ArrayList, HashSet, LinkedList 등)의 상위 인터페이스
 * */


@Data
public class CustomUserDetails implements UserDetails, Serializable  {


	
	private static final long serialVersionUID = 1L;
	private int id;	// DB에서 PK 값
    private String login_id;		// 로그인용 ID 값
    private String password;	// 비밀번호
    private String user_name;
    private String email;	//이메일
    private boolean email_verified;	//이메일 인증 여부
    private boolean locked;	//계정 잠김 여부
    private String authority;	//권한 목록: 예)ROLE_ADMIN, ROLE_CUSTOMER
    public CustomUserDetails(
    		int auth_id,
    		String login_id,
    		String user_name,
    		String email,
    		String password,
    		boolean email_verified,
    		boolean locked,
    		String authority
    		) {
        this.id = auth_id;
        this.login_id = login_id;
        this.password = password;
        this.user_name = user_name;
        this.email = email;
        this.email_verified = email_verified;
        this.locked = !locked;
        this.authority = authority;
    }
	
	@Override
	public String getUsername(){
		return user_name;
	}
	/**
	 * @getAuthorities: 인증된 사용자가 가진 권한 목록을 제공하기 위해 UserDetails 인터페이스의 getAuthorities 메서드 구현
	 * @List.of(): Java9부터 제공되는 메서드, 불변 리스트를 생성
	 * @SimpleGrantedAuthority("user"): 사용자가 가지고 있는 권한, "user"라는 권한을 가진 것으로 설정
		SimpleGrantedAuthority는 GrantedAuthority 인터페이스를 구현한 클래스이며, 문자열로 권한을 표현

	 * @return: user라는 권한을 가진 사용자가 권한 목록을 반환
	 * Collection<GrantedAuthority> collectors = new ArrayList<>();
        람다 표현식: ()->{return "ROLE_"+ authority;} GrantedAuthority타입의 getAuthority() 메서드를 구현
	 * 
	 * 
	 * */
	//사용자가 가진 권한(역할)을 GrantedAuthorigy '객체를 list'로 반환  
	@Override 
	public Collection<? extends GrantedAuthority> getAuthorities(){
		Collection<GrantedAuthority> collectors = new ArrayList<>();
		collectors.add(()->{return "ROLE_"+ authority;}); //add에 들어올 파라미터는 GrantedAuthority밖에 없으니 
		
		return collectors;
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
