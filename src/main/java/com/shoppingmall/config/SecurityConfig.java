package com.shoppingmall.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shoppingmall.jwt.JwtAccessDeniedHandler;
import com.shoppingmall.jwt.JwtAuthenticationEntryPoint;
import com.shoppingmall.jwt.TokenProvider;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

   
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
    * @requestMatchers("/sec_user/signup", "/sec_user/login").permitAll() "이 경로에는 모든 사용자가 접근이 가능"
    * @anyRequest().authenticated(): 위에 명시된 경로 외의 모든 요청에 대해서는 인증이 필요하다는 것을 의미 즉, 사용자가 로그인하지 않은 상태에서는 
    *  다른 모든 요청을 수행할 수 없음
    * @logout - invalidateHttpSession(true) 의미는 로그아웃 후 '전체 세션 삭제 여부'
    * @enum SessionCreationPolicy 는 '상수 열거형'  
    * @jwt 필터는 어디서? 어떻게? 
    *  -   
    */

    private final UserDetailsService userDetailsService; 
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final TokenProvider tokenFilter;
    private final ObjectMapper objectMapper;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
	 @Bean
	public WebSecurityCustomizer WebSecurityCustomizer(){
	  return web -> web.ignoring().requestMatchers("/resources/**");          
	}

	 @Bean  
	 public SecurityFilterChain filterChain1(HttpSecurity http)  throws Exception {
	     http	 .csrf(AbstractHttpConfigurer::disable)
	     		 .httpBasic(AbstractHttpConfigurer::disable)
	     		 .exceptionHandling((exceptionHandling) -> 
	     		  	exceptionHandling
	     		  		.accessDeniedHandler(jwtAccessDeniedHandler)
	     		  		.authenticationEntryPoint(jwtAuthenticationEntryPoint)
	     		 )
	     		 .formLogin(formLogin -> formLogin
	     		    .loginPage("/sec_user/login")
	     		    .defaultSuccessUrl("/products")
	     		    .failureUrl("/sec_user/login?error=true")
	     		 )
	     		 .authorizeHttpRequests((authorize) -> authorize
	     		    .requestMatchers("/sec_user/join", "/home").permitAll()
	     		    .requestMatchers("/admin/**").hasRole("ADMIN")
	     		    
	     		    .anyRequest().authenticated()
	     		  )
	     		 .sessionManagement(sessionMng ->
	     		    sessionMng.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	     		 )
	     		 .logout((logout) -> logout
	     			.logoutSuccessUrl("/sec_user/login")
	     			.invalidateHttpSession(true)
	     		  );
	
	     return http.build();
	 }

    
}