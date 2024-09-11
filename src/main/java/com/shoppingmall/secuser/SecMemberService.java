package com.shoppingmall.secuser;



import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class SecMemberService implements UserDetailsService {
	
	@Autowired
	MemberDao memberDao; 
	
	@Autowired
	NonSocialMemberDao nonSocialMemberDao;
	@Autowired
	SocialMemberDao socaialMemberDao;
	@Autowired
	PasswordEncoder pwdEncoder;

	public int join (NonSocialMemberSaveForm nonSocialMemberSaveForm) throws NoSuchAlgorithmException {
		int login_type = nonSocialMemberSaveForm.getLogin_type();
		validateDuplicateName(nonSocialMemberSaveForm,login_type);
		int user_authid = -1;
		switch(login_type) {
			case 0:{
				NonSocialMember member = new NonSocialMember();
				member.setLogin_type(nonSocialMemberSaveForm.getLogin_type());
				member.setEmail(nonSocialMemberSaveForm.getEmail());
				member.setPassword(pwdEncoder.encode(member.getPassword()));
				member.setUser_name(nonSocialMemberSaveForm.getUser_name());
				member.setEmail_verified(nonSocialMemberSaveForm.isEmail_verified());				
				//####해시 적용 -> 유닛 테스트 필요!!####
				NonSocialMember savedMember = nonSocialMemberDao.signUpFor(member);
				user_authid = savedMember.getAuth_id();
			}
			case 1:{
				throw new UnsupportedOperationException("해당 요청은 아직 구현되지 않았습니다.");
		    }
		}
		return user_authid;
		
	}
	
	public <T> void validateDuplicateName(NonSocialMemberSaveForm memberForm, Integer login_type){
		switch(login_type) {
			case 0:{
				memberDao = nonSocialMemberDao;
				log.info("memberDao 할당=======>" + memberDao);
				break;
			}
			case 1:{
				memberDao = socaialMemberDao;
				break;
			}
		}
		Optional<T> member = memberDao.getOneMemberByName(memberForm.getUser_name());
		if(member.isPresent()) {
		  log.info("존재하는 회원 이름입니다!");
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
    public UserDetails loadUserByUsername(String u_email) throws UsernameNotFoundException {
    	
    	//이메일로 소셜비회원을 찾아옴 
    	Optional<NonSocialMember> nonSocialMember = nonSocialMemberDao.getOneMemberByName(u_email);
    	if(nonSocialMember.isPresent()) {
    		NonSocialMember member = nonSocialMember.get();
    		return new CustomUserDetails(member.getAuth_id(),member.getEmail(), member.getPassword(), true, false);
    	}else {
            throw new UsernameNotFoundException("User not found");
        }
    }
    /*회원가입 후 아이디 중복 검사*/
    @Transactional(value="postgresqlTransactionManager", propagation= Propagation.REQUIRED, rollbackFor=Exception.class)
    public int doCounteLoginId(String login_id) {
		return memberDao.countUserId(login_id);
	}
    
    
    
}
