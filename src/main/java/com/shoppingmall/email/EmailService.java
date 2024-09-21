package com.shoppingmall.email;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.shoppingmall.member.MemberFrontService;
import com.shoppingmall.toaf.object.DataMap;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService implements EmailServiceImpl {
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine; //Thymeleaf 템플릿 엔진을 사용해 이메일의 HTML 본문을 처리
    
    @Autowired
    MemberFrontService memberFrontService;


    public String sendMail(EmailMessage emailMessage, String type) {
        String authNum = createCode();
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(emailMessage.getTo()); // 메일 수신자
            mimeMessageHelper.setSubject(emailMessage.getSubject()); // 메일 제목
            mimeMessageHelper.setText(setContext(authNum, type), true); // 메일 본문 내용, HTML 여부
            javaMailSender.send(mimeMessage);
            /*로직 : 임시 비밀번호 생성 -> 로그인을 가능하게 한다.  */
            DataMap userMap = new DataMap();
            userMap.put("biz_email", emailMessage.getTo());
            userMap.put("auth_num", authNum);
            if(type.equals("password")) memberFrontService.setTempPassword(userMap);
            

            log.info("sendMail ========> Success");

            return authNum;

        } catch (MessagingException e) {
            log.info("sendMail ========> fail");
            throw new RuntimeException(e);
        }
    }

    // 인증번호 및 임시 비밀번호 생성 메서드
    public String createCode() {
        Random random = new Random();
        StringBuffer key = new StringBuffer(); //객체를 생성하지 않고 문자열을  자유롭게 조합 

        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(4); // index = 0 ~ 3 
            switch (index) {
                case 0: key.append((char) ((int) random.nextInt(26) + 97)); break; //index = 1, ASCI로 97 ~ 122 변경 
                case 1: key.append((char) ((int) random.nextInt(26) + 65)); break; //index = 2, ASCI로 65 ~ 90 변경
                default: key.append(random.nextInt(9)); // index가 2 ~ 3 
            }
        }
        return key.toString();
    }

    // thymeleaf를 통한 html 적용
    public String setContext(String code, String type) {
        Context context = new Context(); //템플릿에서 사용할 변수를 저장
        context.setVariable("code", code); //code = authNum
        return templateEngine.process(type, context); // type: 템플릿 파일의 이름(email.html), 어떤 템플릿 파일이 사용될지가 결정
    }
}
