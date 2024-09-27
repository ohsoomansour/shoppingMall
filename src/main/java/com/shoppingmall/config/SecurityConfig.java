package com.shoppingmall.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.shoppingmall.jwt.JwtAccessDeniedHandler;
import com.shoppingmall.jwt.JwtAuthenticationEntryPoint;
import com.shoppingmall.jwt.JwtFilter;
import com.shoppingmall.jwt.TokenProvider;
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
 */


@Configuration
@EnableWebSecurity(debug=true)
public class SecurityConfig {
    
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
     /*
      * 
	     		 .formLogin(auth -> 
	     		 	auth.loginPage("/#/sec_user/login")

     		 		.defaultSuccessUrl("/#/products")
     		 		.failureForwardUrl("/#/loginError")
     		 	  )
                .logout((logout) -> logout
	     			.logoutSuccessUrl("/#/sec_user/login")
	     			.invalidateHttpSession(true)
	     		  );
      * */
     
	 @Bean
	 public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	     http	 .csrf(AbstractHttpConfigurer::disable)
	     		 .httpBasic(AbstractHttpConfigurer::disable)
	     		 .addFilterBefore(new JwtFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)
	     		 .formLogin(AbstractHttpConfigurer::disable)
	     		 .exceptionHandling((exceptionHandling) -> 
	     		  	exceptionHandling
	     		  		.accessDeniedHandler(jwtAccessDeniedHandler)
	     		  		.authenticationEntryPoint(jwtAuthenticationEntryPoint)
	     		 )
	     		 .authorizeHttpRequests((authorize) -> authorize
	     		    .requestMatchers("/admin/**").hasRole("ADMIN")
	     		    .requestMatchers("/sec/login","/sec/join","/sec_user/userDuplicCheck.do", "/send-mail/email").permitAll()
	     		    .requestMatchers("/error/**").permitAll()
	     		    
	     		    .anyRequest().authenticated()
	     		  )
	     		 .sessionManagement(sessionMng ->
	     		    sessionMng.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	     		 )
	     		.logout((logout) -> logout
		     			.logoutSuccessUrl("/#/sec_user/login")
		     			.invalidateHttpSession(true)
		     	 );

	     return http.build();
	 }

    
}