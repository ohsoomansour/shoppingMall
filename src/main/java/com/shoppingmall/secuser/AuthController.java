package com.shoppingmall.secuser;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.shoppingmall.jwt.JwtFilter;
import com.shoppingmall.jwt.TokenDto;
import com.shoppingmall.jwt.TokenProvider;
import com.shoppingmall.toaf.object.DataMap;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @ResponseEntity사용이유: HTTP 상태 코드, status, headers, body등을 모두 커스터마이징할 수 있다.
 * @AuthenticationManagerBuilder
 *  - AuthenticationManager 인스턴스 호출: authenticationManagerBuilder.getObject()
 *    authenticate(authenticationToken) 메서드를 호출하여 자격 증명(사용자 이름과 비밀번호 등)을 기반으로 인증을 수행
 *  - 인증 토큰을 사용하여 인증을 수행하고, 인증된 사용자 정보를 담은 'Authentication 객체'를 반환  
 *  - @Authentication 객체, 인증된 사용자의 정보와 권한을 포함
 *  Q.authenticationManagerBuilder.getObject().authenticate(authenticationToken);
 *   - 어떻게 인증을 수행? 그리고 인증된 객체는 무엇을 담고 있는지 ? 
 *   A1.Object principal = authentication.getPrincipal();   사용자의 기본 정보 (예: UserDetails 객체)
 *   A2.Object credentials = authentication.getCredentials();  // 인증 자격 증명 (대개 인증 후에는 null)
 *   A3.Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();  // 사용자의 권한
 *  @UsernamePasswordAuthenticationToken 타입: UserDetails를 사용해서 만듬 
 * */
/**************************************** 핵심 코드 *************************************************************
 * #상황: 처음 로그인은 토큰이 없음 - > TokenFilter -> UsernamePasswordAuthenticationFilter
 * @UsernamePasswordAuthenticationFilter
 *  - doFilter 메서드, attemptAuthentacate 메서드, authentication매니저가 authenticate 적절한 authenticationProvider를 찾음 
 *  ->  AbstractUserDetailsAuthenticationProvider의 authenticate 메서드 실행 -> DaoAuthenticationProvider가 오버라이딩해서 구현 및 확장 
 * #역할: authenticationManagerBuilder.getObject(): AuthenticationManager 인스턴스 -> AuthenticationProvider 호출       *
 * 																  *	 
 *************************************************************************************************************/

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController   {
	@Autowired
	SecUserService secMemberService;
	@Autowired
	TokenProvider tokenFilter;
	
	 private final AuthenticationManagerBuilder authenticationManagerBuilder;


	//   UsernamePasswordAuthenticationFilter가 '인터셉터'하기 위해서는 POST + /login 기본 세팅
    @PostMapping("/sec/login")  //@Valid 
    public ResponseEntity<TokenDto> login(@RequestBody Map<String, Object> userMap) {
    	//UserDetails를 사용해서 만듬 
    	log.info("PATH: /login ===> nonSocialMemberLoginForm ===>" + userMap);
    	log.info("======== 24.9.25 PATH: /sec/login DEBUGING!!!!!========");
    		//principal, credentials )
	        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userMap.get("login_id"), userMap.get("password"));
	        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
	        log.info("authentication info = {}",authentication);
	        // 해당 객체를 SecurityContextHolder에 저장하고
	        SecurityContextHolder.getContext().setAuthentication(authentication);
	        // 인증받은 새로운 authentication 객체를 createToken 메소드를 통해서 JWT Token을 생성
	        String jwt = tokenFilter.createToken(authentication);																		
	        HttpHeaders httpHeaders = new HttpHeaders();															
	        // response header에 jwt token에 넣어줌
	        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
	        httpHeaders.add("location","http://localhost:8088/#/login");

        // tokenDto를 이용해 response body에도 넣어서 리턴
        	return new ResponseEntity<>(new TokenDto(jwt), httpHeaders, HttpStatus.OK);
 
    	//HttpHeaders FailhttpHeaders = new HttpHeaders();	
    	//return new ResponseEntity<>(FailhttpHeaders, HttpStatus.NOT_ACCEPTABLE);
    }
    /**
     *@Date: 24.9.23  
     *@Param: (*주의) form형태의 request에서 폼 데이터는 'Content-Type' : application/json 형태 
     *  - const formData = new FormData ->  formData.append("authorities", ["Role_admin"]) 보내질 때   "Role_admin"이렇게 인식
     *   유형1. application/json -> @RequestBody + Map<String, Object> userMap 
     *   유형2. multipart/from-data -> @ModelAttribute NonSocialUserSaveForm nonSocialMemberSaveForm  
     * */
    @PostMapping("/sec/join")
    public void doJoin(@ModelAttribute NonSocialUserSaveForm nonSocialMemberSaveForm) {

    	try {
    		log.info("/sec_user/join's nonSocialMemberSaveForm=========>" + nonSocialMemberSaveForm );
    		this.secMemberService.join(nonSocialMemberSaveForm);
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    /** 
	 *@Author: osm
	 *@Description: 경로에서 .do를 사용하게 되면 메서드 실행 전 후 인터셉터가 가능하다. (WebMvcConfig에서 설정)  
	 *@CreatedDate: 2024.9.11 
	 *@Param: - @RequestBody는 DTO 역할, 이 핸들러에서는 HTTP 요청의 body 데이터를 'Map 인터페이스 타입'으로 변환이 가능 
	 *@return: 0 또는 1이상의 값 
	 *@Function: 아이디 및 사업자등록번호 중복검사아이디 
	*/
	@PostMapping("/sec_user/userDuplicCheck.do")
	public int doDoubleCheckActionUser(@RequestBody DataMap userInfo ) {
		log.info("userInfo ==============> " + userInfo);
		log.debug("debug Testing.....!");
	    int NonExistentUserResult = 0;
		try {
			String gubun = userInfo.getstr("gubun");
			String login_id = userInfo.getstr("login_id");
	    	if(gubun.equals("ID")) {
	    		int memberCount = this.secMemberService.doCountLoginId(login_id);
	    		return memberCount;
	    	}
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	    return NonExistentUserResult;
	}
    
}
