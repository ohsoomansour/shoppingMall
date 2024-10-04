package com.shoppingmall.secmember;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DaoAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
	//..생략된 변수 많음
	@Autowired
	PasswordEncoder passwordEncoder;

	
	private UserDetailsService userDetailsService;
	private UserDetailsPasswordService userDetailsPasswordService;

	/*
	public DaoAuthenticationProvider() {
		this(PasswordEncoderFactories.createDelegatingPasswordEncoder());
	}
	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}
	//시점: 객체가 초기화되는 시점
	
	public DaoAuthenticationProvider(PasswordEncoder passwordEncoder) {
		setPasswordEncoder(passwordEncoder);
	}*/
	/**
	 *@explain1. Spring이 객체의 의존성을 모두 주입한 후 @Autowired가 적용
	 *@순서: (AbstractUserDetailsAuthenticationProvider) The class is designed to respond to authentication requests.
     * 
	*/
	@Override
	protected final UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {
		//prepareTimingAttackProtection();
		try {
			UserDetails loadedUser = this.userDetailsService.loadUserByUsername(username);
			if (loadedUser == null) {
				throw new InternalAuthenticationServiceException(
						"UserDetailsService returned null, which is an interface contract violation");
			}
			return loadedUser;
		}
		catch (UsernameNotFoundException ex) {
			//mitigateAgainstTimingAttack(authentication);
			throw ex;
		}
		catch (InternalAuthenticationServiceException ex) {
			throw ex;
		}
		catch (Exception ex) {
			throw new InternalAuthenticationServiceException(ex.getMessage(), ex);
		}
	}
    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails,
			UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
		if (authentication.getCredentials() == null) {
			this.logger.debug("Failed to authenticate since no credentials provided");
			throw new BadCredentialsException(this.messages
					.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
		}
		String presentedPassword = authentication.getCredentials().toString();
		log.debug("presentedPassword =====>" + presentedPassword);
		log.debug("db의 password========>" + userDetails.getPassword());
		if (!this.passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
			this.logger.debug("Failed to authenticate since password does not match stored value");
			throw new BadCredentialsException(this.messages
					.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
		}
	}
	
    /**
     * @호출시점:인증이 성공적으로 완료되면 호출
     * @method설명: 사용자의 비밀번호 인코딩 여부(인코딩 방식 및 정책 변경시)를 확인하고, 비밀번호가 업그레이드가 필요한 경우 업데이트한다 
     * - upgradeEncoding: 현재 저장된 비밀번호의 인코딩 방식이 오래되었거나, 더 강력한 인코딩 방식이 있는 경우
     * @return: Authentication 객체를 생성하여 반환  
     *  Q. Object principal, Authentication authentication, UserDetails user 인수는 어디서 가져오나 ?
     *  A. 스프링 시큐리티에서 인증이 성공하면 Authentication 객체 내부에 principal을 저장(='로그인한 사용자에 대한 정보'=UserDetails)
     *   - retrieveUser 메서드 성공시 반환하는 UserDetails 
     *  성공하면 -> AuthenticationSuccessHandler 호출 -> redirect! 
     *  - 
     * */
	@Override
	protected Authentication createSuccessAuthentication(Object principal, Authentication authentication,
			UserDetails user) {
		boolean upgradeEncoding = this.userDetailsPasswordService != null
				&& this.passwordEncoder.upgradeEncoding(user.getPassword()); 
		if (upgradeEncoding) {
			String presentedPassword = authentication.getCredentials().toString();
			log.debug("createSuccessAuthentication에 들어오나??" + presentedPassword); 
			String newPassword = this.passwordEncoder.encode(presentedPassword);
			user = this.userDetailsPasswordService.updatePassword(user, newPassword);
		}
		return super.createSuccessAuthentication(principal, authentication, user);
	}
}
