package com.shoppingmall.secuser;



import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

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
	 private static final String BCryptPattern = "^\\$2[aby]?(\\$[0-9]{2})?\\$.{22}.*$";
	  
	@Autowired
	PasswordEncoder passwordEncoder;

    

	public int join (MemberSaveForm memberSaveForm) throws NoSuchAlgorithmException, JsonProcessingException {
		int login_type = memberSaveForm.getLogin_type();
		validateDuplicateName(memberSaveForm, login_type);
		int user_authid = -1;
		switch(login_type) {
			case 0:{
				try {
					DataMap membereInfo = new DataMap();
					membereInfo.put("login_type", memberSaveForm.getLogin_type());
					membereInfo.put("login_id", memberSaveForm.getLogin_id());
					membereInfo.put("email", memberSaveForm.getEmail());
					String password = memberSaveForm.getPassword();				
					membereInfo.put("password", passwordEncoder.encode(password));
					membereInfo.put("email_verified", true);
					membereInfo.put("locked", false);
					membereInfo.put("authority", memberSaveForm.getAuthority()); 
					membereInfo.put("address", memberSaveForm.getAddress());			
					membereInfo.put("user_name", memberSaveForm.getUser_name());
					membereInfo.put("u_ph", memberSaveForm.getU_ph());
					log.debug("nonSocialMember =========>" + membereInfo);
					int userCount = this.dao.countQuery("NonSocialMemberSQL.countMemberByLoginId", membereInfo);
					if(userCount > 0) {
						log.error("사용자가 존재합니다! 다른 아이디를 선택해주세요!");					
						throw new Error("사용자가 존재합니다! 다른 아이디를 선택해주세요!");					
					} else {
						this.dao.insertQuery("MemberSQL.signUpFor", membereInfo);
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
	public int socialJoin(Member member) {
		return 0;
	}
	public void validateDuplicateName(MemberSaveForm memberForm, Integer login_type){
		int memberCount = 0;
		
		switch(login_type) {
			case 0:{
				DataMap userMap = new DataMap();
				userMap.put("login_type", memberForm.getLogin_type());
				memberCount
				= this.dao.countQuery("MemberSQL.countMemberByName", userMap);
				log.info("memberCount 카운트 =======>" + memberCount);
				break;
			}
			case 1:{
				DataMap userMap = new DataMap();
				userMap.put("login_type", memberForm.getLogin_type());
				memberCount = this.dao.countQuery("MemberSQL.countMemberByName", userMap);
				
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
	 *    SimpleGrantedAuthority는 GrantedAuthority 인터페이스를 구현한 클래스이며, 문자열로 권한을 표현
    	  SimpleGrantedAuthority("ROLE_ADMIN")과 같은 형태의 객체로 변환
	 * */
    @Override
    public UserDetails loadUserByUsername(String login_id) throws UsernameNotFoundException {
    	
    	log.info("user_name ====> "+ login_id);
    	DataMap userMap = new DataMap();
    	userMap.put("login_id", login_id);
    	log.info("SecUserService's loadUserByUsername의 userMap" + userMap);
    	//DB -> authorities를 가져옴  
    	DataMap nonSocialMember = this.dao.selectQuery("MemberSQL.getOneMemberByUserId", userMap);
    	log.debug("loadUserByUsername's nonSocialMember =======> " + nonSocialMember); 


    	if(!nonSocialMember.isEmpty()) {
    		log.info("loadUserByUsername= ====> user존재");
    		return new CustomUserDetails(
    				nonSocialMember.getint("auth_id"),
    				nonSocialMember.getstr("login_id"),
    				nonSocialMember.getstr("user_name"),
    				nonSocialMember.getstr("email"),
    				nonSocialMember.getstr("password"),
    				 true,
    				 false,
    				 nonSocialMember.getstr("authority")
    				);
    	}else {
            throw new UsernameNotFoundException("User not found");
        }
    }
    /*회원가입 전 아이디 중복 검사*/
    @Transactional(value="postgresqlTransactionManager", propagation= Propagation.REQUIRED, rollbackFor=Exception.class)
    public int doCountLoginId(String login_id) {
    	DataMap userMap = new DataMap();
    	userMap.put("login_id", login_id);
		return this.dao.countQuery("MemberSQL.countMemberByLoginId", userMap);
	}
    /*email로 회원 찾기*/
    public DataMap getMemberByEmail(String email) {
    	DataMap userMap = new DataMap();
    	userMap.put("email", email);
    	return this.dao.selectQuery("MemberSQL.getOneMemberByEmail", userMap);
    }

}
