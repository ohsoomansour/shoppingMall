<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<%----%>
<script src="/js/lms/control/LocalStorageCtrl.js"></script> 
<script type="text/javascript">


function alert_popup_focus(message, selector){
	alert(message);
	$(selector).focus();
}
var LoginCtrl = {
	doLogin : function(){
		if($.trim($("#userid").val()) == ""){
			alert_popup_focus("아이디를 입력해 주세요.","#userid")		
			return false;
		}else if($.trim($("#userpw").val()) == "" ){
			alert_popup_focus("비밀번호를 입력해 주세요.", "#userpw")
			return false;
		}
		// 실제 로그인 처리 -> addSessionLoginInfo & appendSessionInfo 
		$.ajax({
			url : "/login/loginx.do",
			data: $("#frm_login").serialize(),
			type : "POST", 
			dataType: "json",
			success: function(res){
				console.log("로그인 성공 ===" );
				console.log(res);
			},
			error:function(e){
				console.log(e);
			}
		});
		
	}
}

$(document).ready(function(){
	$("#btnLogin").click(function(e){
		e.preventDefault();
		LoginCtrl.doLogin();
	})
})

</script>
<div class="login-box">
	<div class="login-logo"><b>osm shoppingmall</b></div>
	
	<div class="login-box-body">
		<p class="login-box-msg">로그인</p>

		<form id="frm_login" method="post">
			<input type="hidden" id="gourl" name="gourl" value="/" />

			
			<div class="form-group has-feedback">
				<input type="text" id="userid" name="userid" class="form-control" placeholder="아이디" title="아이디">
				<span class="glyphicon glyphicon-envelope form-control-feedback"></span>
			</div>
			<div class="form-group has-feedback">
				<input type="password" id="userpw" name="userpw" class="form-control" placeholder="비밀번호" title="비밀번호">
				<span class="glyphicon glyphicon-lock form-control-feedback"></span>
			</div>
			<div class="row">
				<div class="col-xs-8">
					<div class="checkbox icheck">
						<label>
							<a href="javascript:void(0);" title="비밀번호 찾기">비밀번호 찾기</a>
						</label>
					</div>
				</div>
				<div class="col-xs-4">
					<button id="btnLogin" class="btn btn-primary btn-block btn-flat" title="로그인" >로그인</button>	

				</div>

			</div>
		</form>
	</div>
</div>