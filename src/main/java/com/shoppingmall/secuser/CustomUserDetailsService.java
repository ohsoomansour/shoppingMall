package com.shoppingmall.secuser;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import com.shoppingmall.toaf.basemvc.BaseSvc;
import com.shoppingmall.toaf.object.DataMap;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class CustomUserDetailsService extends BaseSvc<DataMap> implements UserDetailsService  {

    
    

    @Override
    public UserDetails loadUserByUsername(String u_email) throws UsernameNotFoundException {
    	
    	DataMap userMap = new DataMap();
    	userMap.put("u_email", u_email);
    	SecUser userEntity = this.dao.selectQuery("SecUserSQL.getSecUserByEmail", userMap);
    			
        if (userEntity == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return User.builder()
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())  //이미 인코딩된 비밀번호를 설정합니다. Spring Security는 사용자가 로그인할 때 입력한 비밀번호를 인코딩된 비밀번호와 비교하여 인증을 처리합니다.
                .roles(userEntity.getRole())
                .build();
    }
}
