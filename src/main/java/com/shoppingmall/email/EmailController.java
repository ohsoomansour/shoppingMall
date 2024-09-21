package com.shoppingmall.email;


import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class EmailController {

    @Autowired
    private EmailServiceImpl emailServiceImpl;


	// 임시 비밀번호 발급 
    @PostMapping("/send-mail/password")
    public String sendPasswordMail(@RequestBody EmailPostDto emailPostDto) {
    		log.info("=======================>" + emailPostDto);
        EmailMessage emailMessage = EmailMessage.builder()
                .to(emailPostDto.getEmail())
                .subject("[xXx ShoppingMall] 임시 비밀번호 발급")
                .build();

        String tempPw = emailServiceImpl.sendMail(emailMessage, "password");
        if(!tempPw.isEmpty()) {
        	 return "메일을 확인해주세요. 비밀번호가 정상적으로 발급되었습니다.";
        } else {
        		return "정상적인 메일을 입력해주세요. 비밀번호 발급을 실패하였습니다!";
        }
        
       
    }
    /**
     * @Param  : 클라이언트로부터 전달되는 데이터는 JSON 형식의 객체, DTO EmailPostDto(email=ceoosm@naver.com)
     * @RequestBody : JSON 형식의 객체 ---> 자바 객체 (스프링이 자동으로 매핑)
     *               따라서 ('Content-Type' : 'applicaton/json')이 적합
     * */
	// 회원가입 이메일 인증 - 요청 시 body로 인증번호 반환하도록 작성하였음
    @PostMapping("/send-mail/email")
    public Map<String, Object> sendJoinMail(@RequestBody EmailPostDto emailPostDto,  HttpServletRequest request, HttpServletResponse response) {
    		log.info("===============>" + emailPostDto);
        EmailMessage emailMessage = EmailMessage.builder()
                .to(emailPostDto.getEmail())
                .subject("[SAVIEW] 이메일 인증을 위한 인증 코드 발송")
                .build();
        String code = emailServiceImpl.sendMail(emailMessage, "email");
     
        EmailResponseDto emailResponseDto = new EmailResponseDto();
        emailResponseDto.setCode(code);
        /* 이메일 인증 요청 
         * -> 일시적으로 emailResponseDto에 저장 
         * -> 클라이언트, code 값 = 사용자, 입력 code 하면 
         * -> 회원가입  
         **/
        //ResponseEntity.ok(emailResponseDto);  //인증 코드 반환
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("authNum", code);
        return userMap;
    }
}
