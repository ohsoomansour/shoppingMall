<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<script>
$(document).on('ready', function() {
	setTimeout(function(){
		$('#btnLogin').focus();
	}, 500);
	
});

	var idCheck = false;			//아이디 중복검사 체크
	var bizRegnoCheck = false;		//사업자등록번호 중복검사 체크
	$(document).ready(function(){

	});

	//[회원가입] - 이메일 확인 이동 -> 2021/04/19 추정완
	function fncCheckEmail() {
		var userEmail = $('#emailUrl').val();
		var emailUrl = userEmail.split('@')[1];
		window.open('https://www.' + emailUrl);
	}
	//[회원가입] - 로그인 화면 이동 -> 2021/04/26 추정완
	function fncLoginPage() {
		location.href="/login/login.do";
	}
</script>
<!-- compaVcContent s:  -->
<div id="compaVcContent" class="cont_cv">
	<div id="mArticle" class="assig_app">
		<h2 class="screen_out">본문영역</h2>
		<div class="wrap_cont">
			<!-- page_title s:  -->
			<div class="area_tit">
				<h3 class="tit_corp">가입완료</h3>
                <div class="wrapper-stepper agree_box">
                    <ul class="stepper">
                        <li class="active">약관동의</li>
                        <li class="active">회원정보입력</li>
                        <li class="active">가입완료</li>
                    </ul>
                </div>
			</div>
			<!-- //page_title e:  -->
			<!-- page_content s:  -->
			<div class="area_cont ">
	             <div class="box_complete">
	                 <p class="bc_list"> **쇼핑몰에</p>
	                 <p class="bc_list">회원으로 가입되셨습니다. </p>
	                 <!-- <p class="bc_list">가입하신 이메일에 <strong>임시비밀번호</strong>가 발송되었습니다. 처음 로그인 시 비밀번호를 변경하시기 바랍니다.</p> -->
	             </div>
			</div>      
            <div class="wrap_btn _center">
           		<!-- <a href="javascript:void(0);" class="btn_cancel">이메일 확인</a> -->
            	<a href="javascript:fncLoginPage();" class="btn_confirm" title="로그인하기" id="btnLogin">로그인 하기 </a>
            </div>
		<!-- //page_content e:  -->
		</div>
	</div>
</div>
<!-- //compaVcContent e:  -->