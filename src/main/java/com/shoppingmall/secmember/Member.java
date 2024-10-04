package com.shoppingmall.secmember;
import lombok.Builder;


@Builder
public record Member(
		int 	 login_type,
		String 	 memberKey,
	    String   login_id,
	    String   email,
	    boolean  email_verified,
	    String   password,
	    String   address,
	    String   authority,
	    String   user_name,
	    String   u_ph,
	    String   profile
		) {

	public String getAuthority(){
		return authority;
	}
		
}

