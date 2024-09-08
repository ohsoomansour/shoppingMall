package com.shoppingmall.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.shoppingmall.secuser.CustomUserDetailsService;

import lombok.RequiredArgsConstructor;
/**
 * @Filter: 톰캣과 같은 웹 컨테이너에 관리되는 서블릿의 기술, Client 요청이 전달되기 전후의 URL 패턴에 맞는 요청에 필터링을 해준다.
 *  - Spring Security는 요청이 들어오면 Filter를 chain 형태로 묶어놓은 형태인 Servlet FilterChain을 자동으로 구성한 후 거치게 한다.
 * @SpirngFilterChain: 스프링의 보안 필터를 결정하는데 사용되는 필터이다.
 *  - session, jwt등 인증방식을 사용할 때 필요한 설정을 '서비스 로직 구현으로부터 분리할 수 있는 환경'을 제공 
 * @UsernamePasswordAuthenticationFilter: Form Login 기반에서 username과 password를 확인하여 인증한다.
 *  - 인증이 필요한 URL 요청이 들어왔을 때 인증이 되지 않았다면 로그인 페이지를 반환한다. 
 * @SecurityContextHolder : Spring Security로 인증한 사용자의 상세 정보를 저장한다.
 * @SecurityContext: SecurityContextHolder로 접근할 수 있으며, 'Authentication' 객체를 갖는다.
 * @Authentication: 현재 인증된 사용자를 나타내며, SecurityContext에서 가져올 수 있다.
 *  - Principal :사용자를 식별한다. username/password 방식으로 인증할 때 보통 'UserDetails 인스턴스'이다.
 *  - credentials : 주로 비밃전호 정보이다. 대부분 사용자 이즏ㅇ에 사용한 다음 비운다.
 *  - authorities: 사용자에게 부여한 권한을 'GrantedAuthority'로 추상화하여 사용한다.
 * */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SpringSecurityConfiguration extends WebSecurityConfiguration {
	
	private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
	
	@Autowired
    private CustomUserDetailsService customUserDetailsService;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService) //1.사용자 정보를 가져오기 위해 CustomUserDetailsService를 사용
            .passwordEncoder(passwordEncoder());  // 어떤 패스워드 앤코더를 설정할지
    }
    //@설명: 특정 HTTP 요청에 대한 보안 검사를 비활성화하는 역할
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // 정적 resources 접근 허용 설정
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
     
    //version 6.1 최신 
	@Bean
    public SecurityFilterChain securityFilterChains(HttpSecurity http) throws Exception {
	 return http	
		.csrf(AbstractHttpConfigurer::disable) //csrf 사용 안함
		.headers((headers) -> 
			headers.contentTypeOptions(contentTypeOptionConfig -> 
				headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
		))
		// 특정 URL에 대한 권한 설정.
        .authorizeHttpRequests((authorizeRequests) -> {
            authorizeRequests.requestMatchers("/user/**").authenticated();

            authorizeRequests.requestMatchers("/posts/**")
                    // ROLE_은 붙이면 안 된다. hasAnyRole()을 사용할 때 자동으로 ROLE_이 붙기 때문이다.
                    .hasAnyRole("ADMIN", "CUSTOMER");

            authorizeRequests.requestMatchers("/admin/**")
                    // ROLE_은 붙이면 안 된다. hasRole()을 사용할 때 자동으로 ROLE_이 붙기 때문이다.
                    .hasRole("ADMIN");
                    
            authorizeRequests.anyRequest().permitAll();
        })
        .sessionManagement(sessionManagement -> 
        	sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )
        .formLogin((formLogin) -> {
        /* 권한이 필요한 요청은 해당 url로 리다이렉트 */
		    formLogin.loginPage("/login");
        })
        
        .build();

    }
    
	

	
}
