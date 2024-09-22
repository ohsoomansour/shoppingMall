package com.shoppingmall.secuser;



import java.security.NoSuchAlgorithmException;

import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.shoppingmall.toaf.basemvc.BaseSvc;
import com.shoppingmall.toaf.object.DataMap;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
/**
 *@순서: 인증 요청 ->  AbstractUserDetailsAuthenticationProvider -> DaoAuthenticationProvider의 retrieveUser
 *      -> this.userDetailsService.loadUserByUsername(username); 호출 -> SecUserService의 loadUserByUsername
 * */


@Slf4j
@RequiredArgsConstructor
@Service
public class SecUserService extends BaseSvc<DataMap> implements UserDetailsService {
	private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


	public int join (NonSocialUserSaveForm nonSocialMemberSaveForm) throws NoSuchAlgorithmException {
		int login_type = nonSocialMemberSaveForm.getLogin_type();
		validateDuplicateName(nonSocialMemberSaveForm, login_type);
		int user_authid = -1;
		switch(login_type) {
			case 0:{
				DataMap nonSocialMember = new DataMap();

				nonSocialMember.put("authorities", nonSocialMemberSaveForm.getAuthorities());
				nonSocialMember.put("login_id", nonSocialMemberSaveForm.getLogin_id());
				nonSocialMember.put("password", passwordEncoder.encode(nonSocialMemberSaveForm.getPassword()) );
				nonSocialMember.put("user_name", nonSocialMemberSaveForm.getUser_name());
				nonSocialMember.put("email", nonSocialMemberSaveForm.getEmail());
				nonSocialMember.put("email_verified", false);
				nonSocialMember.put("u_ph", nonSocialMemberSaveForm.getU_ph());
				
				int userCount = this.dao.countQuery("NonSocialMemberSQL.countMemberByLoginId", nonSocialMember);
				if(userCount > 0) {
					log.error("사용자가 존재합니다! 다른 아이디를 선택해주세요!");					
					throw new Error("사용자가 존재합니다! 다른 아이디를 선택해주세요!");					
				} else {
					this.dao.insertQuery("NonSocialMemberSQL.signUpFor", nonSocialMember);
				}
				user_authid = 1;
				break;
			}
			case 1: {
	            throw new UnsupportedOperationException("해당 요청은 아직 구현되지 않았습니다.");
	        }
		}
		return user_authid;
		
	}
	
	public void validateDuplicateName(NonSocialUserSaveForm memberForm, Integer login_type){
		int memberCount = 0;
		
		switch(login_type) {
			case 0:{
				DataMap userMap = new DataMap();
				userMap.put("login_type", memberForm.getLogin_type());
				memberCount
				= this.dao.countQuery("NonSocialMemberSQL.countMemberByName", userMap);
				log.info("memberCount 카운트 =======>" + memberCount);
				break;
			}
			case 1:{
				DataMap userMap = new DataMap();
				userMap.put("login_type", memberForm.getLogin_type());
				memberCount = this.dao.countQuery("NonSocialMemberSQL.countMemberByName", userMap);
				
				break;
			}
		}
		if(memberCount > 0) {
		  log.info("존재하는 회원 이름입니다!");
		} else {
		  log.info("존재하지 않는 회원 이릅입니다.");
		}
		
    }
	//조인
	
	//이메일-> DB를 통해 유저를 찾아오는 '유저 로드 로직'
	
	/**
	 * @CreationDate: 24.9.10
	 * @실행순서: DaoAuthenticationProvider에서 retrieveUser, this.userDetailsService.loadUserByUsername(username);
	 *  -> SecMemberService 클래스의 loadUserByUsername 호출!  
	 * */
    @Override
    public UserDetails loadUserByUsername(String u_name) throws UsernameNotFoundException {
    	
    	//이메일로 소셜비회원을 찾아옴 
    	DataMap userMap = new DataMap();
    	userMap.put("u_name", userMap);
    	DataMap nonSocialMember = this.dao.selectQuery("NonSocialMemberSQL.getOneMemberByName", userMap);
    	if(nonSocialMember.isEmpty()) {
    		return new CustomUserDetails(nonSocialMember.getint("auth_id"),nonSocialMember.getstr("email"), nonSocialMember.getstr("password"), true, false);
    	}else {
            throw new UsernameNotFoundException("User not found");
        }
    }
    /*회원가입 전 아이디 중복 검사*/
    @Transactional(value="postgresqlTransactionManager", propagation= Propagation.REQUIRED, rollbackFor=Exception.class)
    public int doCountLoginId(String login_id) {
    	DataMap userMap = new DataMap();
    	userMap.put("login_id", login_id);
		return this.dao.countQuery("NonSocialMemberSQL.countMemberByLoginId", userMap);
	}


}
