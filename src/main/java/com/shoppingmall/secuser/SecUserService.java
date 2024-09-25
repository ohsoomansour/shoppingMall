package com.shoppingmall.secuser;



import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
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
	

	public int join (NonSocialUserSaveForm nonSocialMemberSaveForm) throws NoSuchAlgorithmException, JsonProcessingException {
		int login_type = nonSocialMemberSaveForm.getLogin_type();
		validateDuplicateName(nonSocialMemberSaveForm, login_type);
		int user_authid = -1;
		switch(login_type) {
			case 0:{
				try {
					DataMap nonSocialMember = new DataMap();
					nonSocialMember.put("login_type", nonSocialMemberSaveForm.getLogin_type());
					nonSocialMember.put("login_id", nonSocialMemberSaveForm.getLogin_id());
					nonSocialMember.put("email", nonSocialMemberSaveForm.getEmail());
					nonSocialMember.put("password", passwordEncoder.encode(nonSocialMemberSaveForm.getPassword()) );
					nonSocialMember.put("email_verified", true);
					nonSocialMember.put("locked", false);
					List<String> authorList = new ArrayList<>();
					authorList.add(nonSocialMemberSaveForm.getAuthorities());
					log.info("authorList ==========> " + authorList);
					nonSocialMember.put("authorities", authorList); 
					nonSocialMember.put("address", nonSocialMemberSaveForm.getAddress());			
					nonSocialMember.put("user_name", nonSocialMemberSaveForm.getUser_name());
					nonSocialMember.put("u_ph", nonSocialMemberSaveForm.getU_ph());
					log.debug("nonSocialMember =========>" + nonSocialMember);
					int userCount = this.dao.countQuery("NonSocialMemberSQL.countMemberByLoginId", nonSocialMember);
					if(userCount > 0) {
						log.error("사용자가 존재합니다! 다른 아이디를 선택해주세요!");					
						throw new Error("사용자가 존재합니다! 다른 아이디를 선택해주세요!");					
					} else {
						this.dao.insertQuery("NonSocialMemberSQL.signUpFor", nonSocialMember);
					}
				    user_authid = 1;
				} catch (Exception e) {
					e.printStackTrace();
				}
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
		  log.info("정상적으로 사용가능한 ");
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
    public UserDetails loadUserByUsername(String user_name) throws UsernameNotFoundException {
    	
    	//이메일로 소셜비회원을 찾아옴 
    	log.info("user_name ====> "+ user_name);
    	DataMap userMap = new DataMap();
    	userMap.put("user_name", userMap);
    	DataMap nonSocialMember = this.dao.selectQuery("NonSocialMemberSQL.getOneMemberByName", userMap);
    	log.debug("loadUserByUsername's nonSocialMember =======> " + nonSocialMember);
    	
    	//이게 컬렉션 타입이어야 하는데그게 아니면 error ee
    	if(nonSocialMember.isEmpty()) {
    		log.info("loadUserByUsername= ====> user존재");
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
