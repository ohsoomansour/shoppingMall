package com.shoppingmall.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.shoppingmall.jwt.JwtAccessDeniedHandler;
import com.shoppingmall.jwt.JwtAuthenticationEntryPoint;
import com.shoppingmall.jwt.JwtFilter;
import com.shoppingmall.jwt.TokenProvider;
import com.shoppingmall.oauth.CustomOAuth2UserService;
import com.shoppingmall.oauth.OAuth2SuccessHandler;
/** 
 *@FilterChainProxy: Returns the first fistchain matching the supplied URL
 * /user경로와 매칭이 되면 filterChain1이 성공적으로 인증이 되면 filterChain1이 filterChain2보다 먼저 등록이 된다.
 *  -> 따라서 필터체인1이 먼저 등록되어 있어서 /admin은 내부에 등록이 되어있지 않기 때문에 요청 에러가 발생
 * @해결 방법: SecurityFilterChain에 대한 경로 설정(securityMatcher)을 추가적으로 하지 않았기 때문에 “/**” 경로에 대해서 반응하기 때문
 * @csrf(cross site request forgery) 방지
 * @HttpBasic : Http Basic 인증 비활성화, 기본 인증 방식으로 사용자 이름과 비밀번호를 HTTP 헤더에 담아 전송한다. 
 *              비활성화, 기본 인증이 적용되지 않는다. 
 * @formLogin: 기본 제공되는 폼 로그인 기능(html파일) 설정 -> 비활성화
 * @defaultSuccessUrl:  This is a shortcut for calling successHandler(AuthenticationSuccessHandler).              
 * @securityMatchers: : 특정 요청 경로에 대해 보안 필터를 설정
 * @hasRole("ADMIN")=hasAuthority("ROLE_ADMIN")
 *   [SecurityContextHolder에 저장되어 있는 Authentication 객체] 
 *  "Authentication.getAuthorities()로 얻은 권한 리스트에 "ROLE_ADMIN"이 포함되어 있는지 검사함으로써 이루어진다. "
 * @requestMatchers("/sec_user/signup", "/sec_user/login").permitAll() "이 경로에는 모든 사용자가 접근이 가능"
 * @anyRequest().authenticated(): 위에 명시된 경로 외의 모든 요청에 대해서는 인증이 필요하다는 것을 의미 즉, 사용자가 로그인하지 않은 상태에서는 
 *  다른 모든 요청을 수행할 수 없음
 * @logout - invalidateHttpSession(true) 의미는 로그아웃 후 '전체 세션 삭제 여부'
 * @enum SessionCreationPolicy 는 '상수 열거형'  
 * @jwt 필터는 어디서? 어떻게? 
 * Q. Creation of SecureRandom instance for session ID generation using [SHA1PRNG] took [116] milliseconds. 
 * A.  세션 ID 생성을 위해 암호학적으로 안전한 난수 생성기(SecureRandom)를 사용하는데, 이를 초기화하는 데 116밀리초가 걸렸다는 것   
 * ################################################ oAuth2 ######################################################
 * @oAuth설정:  https://console.cloud.google.com/
 *  - Authorized redirect URIs : http://localhost:8080/login/oauth2/code/google
 *  구글 로그인 페이지 - 클라이언트, Authorization Code Request -> Authorization server(구글서버) -> Authorization Code Response
 *  -리다이렉트-> 서버, Access Token Request -> Authorization server, Access Token Response -> 서버 
 *   User Info Request('사용자 정보'를 요청) -> Resource Server, 구글의 사용자 정보는 https://www.googleapis.com/oauth2/v3/userinfo (엔드포인트)에서
 *   '사용자 정보'를 가져온다. -> 사용자 정보를 DB에 저장 그리고 서비스 자체 엑세스 토큰과 리프레쉬 토큰을 생성 -> 프론트, 쿠키에 두 토큰을 저장 
 *   *리프레쉬 토큰: 새로운 액세스 토큰을 발급받기 위해 사용, 유효 기간이 길다. 
 * @구글server: access Token을 발급 -> 클라이언트 -> @리소스server: 이 jwt를 받아, 그토큰이 유효한지 검증한 후 사용자가 요청한 자원(예:사용자 data)을
 *  제공
 * @OAuth2LoginAuthenticationFilter 설정 및 호출
 *  - 언제? spring-boot-starter-oauth2-client 의존성을 추가
 *  - 상세:  Spring Security 설정을 구성하여 OAuth2 로그인 기능을 활성화합니다. 
 *          이때 OAuth2LoginAuthenticationFilter가 자동으로 필터 체인에 등록
 *  - 역할: Authorization Code Grant 흐름에서 Authorization Code를 Access Token으로 교환하는 필터
 *  - 어디? oauth2Login() 안에 
 *  
 *  @oauth2Login 호출은 언제 ? A)로그인 시도, Spring Security는 자동으로 OAuth2 인증 흐름을 시작        
 *  @OAuth2LoginAuthenticationProvider 호출은 언제? 
 *  A. AuthenticationMangaer 의 authenticate() -> AuthennticationProvider -(implements)-> OAuth2LoginAuthenticationProvider
 *  <OAuth2LoginAuthenticationFilter>
 *  OAuth2LoginAuthenticationToken authenticationResult = (OAuth2LoginAuthenticationToken) this
			.getAuthenticationManager()
			.authenticate(authenticationRequest);
			
     *핵심: 이 필터에서 반환 'oauth2Authentication' -> AbstractAuthenticationProcessingFilter의 doFilter 메서드로 전달 	
          -> successfulAuthentication 메서드로 전달 되면서 SecurityContextHolder에 저장		
 */


@Configuration
@EnableWebSecurity(debug=true)
public class SecurityConfig {
    
	@Autowired
	private CustomOAuth2UserService oAuth2UserService;
	
	@Autowired
	private OAuth2SuccessHandler oAuth2SuccessHandler;
	
	@Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @Autowired
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;
    
    @Autowired
    private TokenProvider tokenProvider;
	/*web -> web.ignoring().requestMatchers("/resources/**");   */
    
    @Bean 
    public WebSecurityCustomizer WebSecurityCustomizer(){
	  return web -> web.ignoring().requestMatchers("/resources/static/**");
	}
    
     
	 @Bean
	 public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	     http	 .csrf(AbstractHttpConfigurer::disable)
	     		 .httpBasic(AbstractHttpConfigurer::disable)
	     		 .addFilterBefore(new JwtFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)
	     		 .formLogin(AbstractHttpConfigurer::disable)
	     		 .logout(AbstractHttpConfigurer::disable)
	     		 .exceptionHandling((exceptionHandling) -> 
	     		  	exceptionHandling
	     		  		.accessDeniedHandler(jwtAccessDeniedHandler)
	     		  		.authenticationEntryPoint(jwtAuthenticationEntryPoint)
	     		 )
	     		 .authorizeHttpRequests((authorize) -> authorize
	     		    .requestMatchers("/admin/**").hasRole("ADMIN")
	     		    																			// 10.9 /auth/success 삭제 
	     		    .requestMatchers("/sec/login","/sec/join","/sec_user/userDuplicCheck.do", "/send-mail/email", "/sec/gAuthSuccess").permitAll()
	     		    .requestMatchers("/error/**").permitAll()
	     		    
	     		    .anyRequest().authenticated()
	     		  )
	     		 .sessionManagement(sessionMng ->
	     		    sessionMng.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	     		 )
	     		.oauth2Login(oauth -> // 'OAuth2 로그인 기능'에 대한 여러 설정의 진입점
	     		   /* <OAuth2LoginAuthenticationFilter> 자동으로 설정 후 이 시점에 호출
	     		    -> redirect_uri: http://localhost:8080/login/oauth2/code/google?state= "" &code= "ABCD"
	     		    -> OAuth2LoginAuthenticationFilter의 attemptAuthentication, request.getParameterMap()    		 
	     		    -> OAuth2LoginAuthenticationProvider는 언제 호출? authenticationManager가 authenticate()
	     		       AuthenticationManager는 여러 AuthenticationProvider를 관리하는데, 여기에서 적합한 '프로바이더'의 authenticate()를
	     		       찾아서 호출함! - (implements) -> OAuth2LoginAuthenticationProvider
	     		    -> OAuth2LoginAuthenticationProvider, code  OAuth 2.0 권한 부여 응답 
	     		       OAuth2LoginAuthenticationToken`을 생성하여 AuthenticationManager`에 전달
	     		    -> 리소스 서버에서 '사용자 세부 정보'를 로드하기 위해 가 호출, OAuth2UserService 호출!  
	     		    -> OAuth2 로그인 성공 이후 '사용자 정보'를 가져올 때의 설정을 담당 
	     		       
	     		   */
	     		   oauth.userInfoEndpoint(c -> c.userService(oAuth2UserService))  
	     		   .authorizationEndpoint(authorizationEndpointConfig -> 
	     		   		authorizationEndpointConfig.baseUri("/oauth2/authorize")	
	     		    )
	     			//로그인 성공 시 핸들러
	     		   .successHandler(oAuth2SuccessHandler)
	     	     );

	     return http.build();
	 }

    
}