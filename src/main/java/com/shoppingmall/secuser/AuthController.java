package com.shoppingmall.secuser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shoppingmall.jwt.JwtFilter;
import com.shoppingmall.jwt.TokenDto;
import com.shoppingmall.jwt.TokenProvider;

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
@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {
	@Autowired
	SecMemberService secMemberService;
	
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    @PostMapping("/sec_user/login")
    public ResponseEntity<TokenDto> login(@Valid @RequestBody NonSocialMemberSaveForm nonSocialMemberLoginForm) {
    	//UserDetails를 사용해서 만듬
        UsernamePasswordAuthenticationToken authenticationToken =
        												//principal, credentials 
                new UsernamePasswordAuthenticationToken(nonSocialMemberLoginForm.getEmail(), nonSocialMemberLoginForm.getPassword());
        log.info("authetntication manager builder get object = {}",authenticationManagerBuilder.getObject());
        /**************************************** 핵심 코드 *************************************************************
         * @authenticationManagerBuilder.getObject(): AuthenticationManager 인스턴스 -> AuthenticationProvider 호출       *
         * @authenticate메소드가 실행이 될 때 CustomUserDetailsService class의 loadUserByUsername 메소드가 실행 및 db와 대조하여 인증 *
         *  - @DaoAuthenticationProvider가 사용, retrieveUser 메서드 호출 -> SecMemberServce의 UserDetails loadUserByUsername가 
         *     호출 됨, UserDetails타입 반환 -> CustomDetails에 매핑																		  *	 
         *************************************************************************************************************/
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        log.info("authentication info = {}",authentication);
        // 해당 객체를 SecurityContextHolder에 저장하고
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // 인증받은 새로운 authentication 객체를 createToken 메소드를 통해서 JWT Token을 생성
        String jwt = tokenProvider.createToken(authentication);																		
        HttpHeaders httpHeaders = new HttpHeaders();															
        // response header에 jwt token에 넣어줌
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        httpHeaders.add("location","http://localhost:8088/#/sec_user/login");

        // tokenDto를 이용해 response body에도 넣어서 리턴
        return new ResponseEntity<>(new TokenDto(jwt), httpHeaders, HttpStatus.OK);
    }
    //secMemberService
    @PostMapping("/sec_user/join")
    public void doJoin(@RequestBody NonSocialMemberSaveForm nonSocialMemberSaveForm) {
    	//NonSocialMemberSaveForm nonSocialMemberSaveForm, login_type = 0, ]
    	try {
    		this.secMemberService.join(nonSocialMemberSaveForm);
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    /** 
	 *@Author: osm
	 *@CreatedDate: 2024.9.11 
	 *@Param: - 
	 *@return: 0 또는 1이상의 값 
	 *@Function: 아이디 및 사업자등록번호 중복검사아이디 
	*/
	@GetMapping("/sec_user/userDoubleCheck.do")
	public int doMemberDoubleCheck(@RequestParam("gubun") String gubun, @RequestParam("id") String login_id) {
	    int NonExistentUserResult = 0;
		try {
	    	if(gubun.equals("ID")) {
	    		int memberCount = secMemberService.doCounteLoginId(login_id);
	    		return memberCount;
	    	}
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	    return NonExistentUserResult;
	}
    
}
