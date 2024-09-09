package com.shoppingmall.secuser;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import com.shoppingmall.toaf.basemvc.BaseSvc;
import com.shoppingmall.toaf.object.DataMap;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class SecMemberService extends BaseSvc<DataMap> implements UserDetailsService  {

	//이메일-> DB를 통해 유저를 찾아오는 '유저 로드 로직'
    @Override
    public UserDetails loadUserByUsername(String u_email) throws UsernameNotFoundException {
    	DataMap userMap = new DataMap();
    	userMap.put("u_email", u_email);
    	//이메일로 소셜비회원을 찾아옴 
    	Optional<NonsocialMember> nonSocialMember = this.dao.selectQuery("NonSocialMemberSQL.getSecUserByEmail", userMap);
    	if(nonSocialMember.isPresent()) {
    		NonsocialMember member = nonSocialMember.get();
    		return new CustomUserDetails(member.getAuthId(),member.getEmail(), member.getPassword(), true, false);
    	}else {
            throw new UsernameNotFoundException("User not found");
        }

      
    }
}
