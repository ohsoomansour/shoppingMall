package com.shoppingmall.jwt;

import java.security.Key;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;


/**
 * @Date: 24.9.8 
 * @Description: 토큰 설정 및 유효성 검증
 * @Base64인코딩된data의길이: 항상 4의 배수 길이가 되어야 한다. -> secret_key는 32자리(256bit) -> (SHA)new SecretKey
 * */

@Slf4j
@Component
public class TokenProvider implements InitializingBean {
	
	private final String secret_key;
	private final long tokenValidityInSeconds;
	private static final String AUTHORITIES_KEY = "auth";
	private Key key;
	
	public TokenProvider( 
			@Value("${jwt.secret-key}") String secret_key,
			@Value("${jwt.token-validity-in-sconds}") long tokenValidityInSeconds) {
		    
		 	this.secret_key = secret_key;
		 	this.tokenValidityInSeconds = tokenValidityInSeconds * 1000;   
	}
	
	@Override  //모든 빈의 프로퍼티들 설정 후 실행
	public void afterPropertiesSet() {
		byte[] keyBytes = Decoders.BASE64.decode(secret_key);
		this.key = Keys.hmacShaKeyFor(keyBytes);
	}
	/**
	 * 
	 * @getAuthorities: ROLE_ADMIN, ROLE_CUSTOMER
	 * @stream: 반환된 권한 리스트를 스트림으로 변환하여 각 권한 객체를 처리할 수 있게 한다.
	 * @GrantedAuthority:GrantedAuthority는 권한을 나타내는 인터페이스, getAuthority() 메서드는 권한 이름을 반환
	 * @collect(종단연산): Collectors.joining(",")는 추출된 권한 이름들을 하나의 문자열로 함침 예) "ROLE_ADMIN, ROLE_CUSTOER"
	 *  - 다른 예시) List<String> asList = stringStream.collect(Collectors.toList());
	 * */
	public String createToken(Authentication authentication) {
		String authorities = authentication.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(","));
		
		long now = (new Date()).getTime();
		Date validity = new Date(now + this.tokenValidityInSeconds);
		
		return Jwts.builder()
				.setSubject(authentication.getName()) // principal = UserDetails 
				.claim(AUTHORITIES_KEY, authorities)   //사용자 권한 정보를 JWT의 클레임에 저장 -> token값에 넣어서 반환
     			.signWith(key, SignatureAlgorithm.HS512)  //HMAC SHA-512 알고리즘을 사용해 secret 키로 서명
				.setExpiration(validity)
				.compact();   //모든 설정을 압축하여 최종 'JWT 문자열' 반환
	}
	/**
	 *@Collection<? extends GrantedAuthority> authorities = Collections.emptyList();
     *  - GrantedAuthority 타입 또는 그 하위 타입의 권한들이 담길 수 있는 빈 리스트를 생성하는 것 
     *@Jwts.parserBuilder().setSigningKey(key).build() - JWT 파서를 생성
     *@parseClaimsJws(token) 메서드는 제공된 JWT 토큰을 파싱하고 검증합니다. 토큰이 유효하지 않거나 기간이 만료되었거나 서명이 잘못된 경우 예외를 발생
     *@getBody() 메서드는 토큰의 클레임(=페이로드)을 가져온다.
	 * */
    public Authentication getAuthentication(String token) {
    	Claims claims = Jwts
    			.parserBuilder()
    			.setSigningKey(key)
    			.build()
    			.parseClaimsJws(token)
    			.getBody();
    	log.info("authorities = {}", claims.get(AUTHORITIES_KEY).toString().split(","));
    	Collection<? extends GrantedAuthority> authorities = Collections.emptyList();
    	User principal = new User(claims.getSubject(), "", authorities);
    	return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }
    // 토큰의 유효성 검증을 수행
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }
    
	
}
