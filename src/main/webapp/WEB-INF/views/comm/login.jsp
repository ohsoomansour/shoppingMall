<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%-- --%>

</script>
<div class="login-box">
	<div class="login-logo"><b>TTM University</b></div>
	
	<div class="login-box-body">
		<p class="login-box-msg">로그인</p>

		<form id="frm_login" method="post">
			<input type="hidden" id="gourl" name="gourl" value="/" />
			<input type="hidden" id="siteid" name="siteid" value="${siteid}" />
			
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
					<button id="btnLogin" class="btn btn-primary btn-block btn-flat" title="로그인">로그인</button>
				</div>
			</div>
		</form>
	</div>
</div>