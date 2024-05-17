<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<style>
.ui-autocomplete {
  max-height: 200px;
  overflow-y: auto;
  /* prevent horizontal scrollbar */
  overflow-x: hidden;
  height: auto;
}
.ui-menu-item div.ui-state-hover,
.ui-menu-item div.ui-state-active {
  color: #ffffff;
  text-decoration: none;
  background-color: #f6B664;
  border-radius: 0px;
  -webkit-border-radius: 0px;
  -moz-border-radius: 0px;
  background-image: none;
  border:none;
}
</style>

<script > <!-- jsp 파일 구조를 아래와 같이 작성 시 페이지 파싱 후 script 실행-->
var idCheck = false; //아이디 중복검사 체크
var pwCheck = false; //패스워드 중복검사 체크
const idRegex = /^[a-zA-Z0-9]{1,16}$/;//대소문자영문 숫자포함한 정규식
const passwordRegex = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[@#$%^&*!])[A-Za-z\d@#$%^&*!]{8,16}$/; //영문,숫자,특수문자를 포함한 8자이상 16자 이하 정규식

$(document).ready(function(){
	// 유저 이메일 주소 직접 입력이 아닌 경우 1.
	$('userEmail2').keyup(){
		$(this).val($(this).val().replace(/[\ㄱ-ㅎㅏ-ㅣ가-힣\]/g, '')); //문자열을 반환 -> 그 값을 기입
		if($('#userEmail3').val() !== '직접입력'){
			$('#userEmail3').val("직접입력");
			console.log("틀림", $('#userEmail3').val());
		}
	}
	// 비즈니스 계정이 주소 직접 입력이 아닌 경우
	$('#bizEmail2').keyup(function(){
		$(this).val($(this).val().replace(/[ㄱ-ㅎㅏ-ㅣ가-힣]/g, ''));
		if($('#bizEmail3').val() !== "직접입력"){
			$('#bizEmail3').val() !== "직접입력";
			console.log("틀림 " + $('#bizEmail3').val());
		}
	});
	
	
	// 아이디 중복체크 클릭
	$("#btnIdCheck").click(function(){
		fncDoubleCheck("ID");
	});
	//[회원가입] - 아이디 및 사업자등록번호 중복확인 -> 추정 중 2024/05/17(수)
	function fncDoubleCheck(gubun){
		if(gubun == 'ID'){
			var id = $('#id').val();
			console.log(id);
		}
		if(id == '' || id == null){
			alert_popup_focus('아이디를 입력 후 중복확인 버튼을 클릭해주세요.', '#id');
			return false;
	    }
	}
	
	
	
	
	
	
	
	
	
	
})



function alert_popup_focus(message, selector){
	alert(message);
	$(selector).focus();
}

function alert_popup(message, selector){
	alert(message);
	$(selector).focus();
}
//빈칸 검사 
function isBlank(message, id){
	if($.trim($(id).html()) == ''){
		alert(message);
	}
	
}
// 사용자가 클릭한 요소 $(this)



//회원가입 -> 2024/05/16 osm  $.trim($("selector").html())
function fncMemberJoin(){
	//개인정보 유효성 검사
	if(!isBlank('아이디', '#id')){
		var id = $('#id').val();
		if(idRegex.test(id)){
			if(!idCheck){
				alert_popup_focus('아이디 중복확인을 해주세요.',"#id");
				return false;
			}else{
				console.log("왜죠 " + idCheck)
				if(!isBlank('비밀번호', '#pw')){
					if(!isBlank('비밀번호 확인', '#passWordCk')){
						var pw = $('#pw').val();
						var pwChk = $('#passWordCk').val();
						if(passwordRegex.test(pw)){
							if(pw == pwChk){
								console.log("왜죠 " + pw + pwChk)
								if(!isBlank('이름', '#userName')){
									if(!isBlank('개인이메일', '#userEmail1')){
										if(!isBlank('개인이메일 도메인', '#userEmail2')){
											if(!isBlank('휴대전화번호', '#userMobileNo')){
												if(!isBlank('회사명', '#bizName')){
													if(!isBlank('부서명', '#userDepart')){
														if(!isBlank('직급', '#userRank')){
															if(!isBlank('업무용이메일', '#bizEmail1')){
																if(!isBlank('업무용이메일도메인', '#bizEmail2')){
																	if(!isBlank('회사용직통전화번호', '#bizTelNo')){
																		var url = "/member/memberJoinConfirm.do"
																			var form = $('#frm')[0];
																		    console.log(form); //뭐 나옴?  
																			var data = new FormData(form);
																				$.ajax({
																				       url : url,
																				       type: "post",
																				       processData: false,
																				       contentType: false,
																				       data: data,
																				       dataType: "json",
																				       success : function(res){
																					       alert_popup("회원가입이 완료되었습니다. 로그인창으로 이동합니다.","/techtalk/login.do");
																					    	//alert("성공") 
																					    	//location.href="/techtalk/login.do"
																				       },
																				       error : function(){
																					       //alert_popup("에러가 발생하였습니다. 관리자에게 문의해주세요","/techtalk/memberJoinFormPage.do");
																				    	//alert('게시판 등록에 실패했습니다.');    
																				       },
																				       complete : function(){
																				       }
																				});
																		}
																	}
																}
															}
														}
													}
												}
											}
										}
									}
							}else{
								alert_popup_focus("비밀번호와 비밀번호 확인이  일치하지않습니다.","#pw");
							}
						}else{
							alert_popup_focus("비밀번호는 영문, 숫자, 특수문자를 포함하여 8자이상 16자이하로 설정해주세요",'#pw');
						}
						}
					}
				}
		}else{
			alert_popup_focus("아이디는 영문, 숫자를 포함하여 16자 이하로 설정해주세요",'#id');
		}
	}
}



//[회원가입] - 회원가입 완료 화면 이동 ->
function fncCompeletePage() {
	location.href = "/techtalk/memberJoinCompletePage.do";
}


function popup() {
	var url = "/images/techtalk/example.jpg";
	var name = "popup test";
	var option = "width = 500, height = 500, top = 100, left = 200, location = no "
	window.open(url, name, option);
}

function changeText(text, id){
	$(id).empty();
	$(id).html(text);
	}
</script>

<!-- compaVcContent s:  -->
<div id="compaVcContent" class="cont_cv">
	<div id="mArticle" class="assig_app">
		<h2 class="screen_out">본문영역</h2>
		<div class="wrap_cont">
			<!-- page_title s:  -->
			<div class="area_tit">
				<h3 class="tit_corp">회원정보입력</h3>
			</div>
			<!-- //page_title e:  -->
			<!-- page_content s:  -->
			<form id="frm">
				<div class="area_cont ">
					<div class="subject_corp">
						<h4>개인정보 입력</h4>
					</div>
					<div class="tbl_view tbl_public">
						<table class="tbl">
							<caption>개인정보 입력폼</caption>
							<colgroup>
								<col style="width: 15%">
								<col>
							</colgroup>
							<thead></thead>
							<tbody class="view">
								<tr>
									<th scope="col">회원유형 <span class="red">*</span></th>
									<td class="ta_left">
										<div class="form-inline">
											<div class="box_radioinp">
												<input type="radio" class="inp_radio" name="member_type"
													id="R" value="R" title="연구자" checked /><label for="R"
													class="lab_radio"><span class="icon ico_radio"></span>연구자</label>
											</div>
											<div class="box_radioinp">
												<input type="radio" class="inp_radio" name="member_type"
													id="B" value="B" title="기업" /><label for="B"
													class="lab_radio"><span class="icon ico_radio"></span>기업</label>
											</div>
											<div class="box_radioinp">
												<input type="radio" class="inp_radio" name="member_type"
													id="TLO" value="TLO" title="TLO" /><label for="TLO"
													class="lab_radio"><span class="icon ico_radio"></span>TLO</label>
											</div>
										</div>
									</td>
								</tr>
								<tr>
									<th scope="col">아이디 <span class="red">*</span></th>
									<td class="ta_left">
										<div class="form-inline">
											<input type="text" class="form-control form_id" id="id"
												name="id"
												onkeyup="this.value=this.value.replace(/[\ㄱ-ㅎㅏ-ㅣ가-힣]/g, '');"
												title="아이디"> <a href="javascript:void;"
												class="btn_step2" title="중복확인" id="btnIdCheck">중복확인</a> <span
												style="margin-left: 10px;">아이디는 영문, 숫자를 포함하여 16자 이하로 설정 </span>
										</div>
										<div>
											<p id="idCheck" />
										</div>
									</td>
								</tr>
								<tr>
									<th scope="col">비밀번호 <span class="red">*</span></th>
									<td class="ta_left">
										<div class="form-inline">
											<input type="password" class="form-control form_pw" id="pw"
												name="pw" title="비밀번호"> <span
												style="margin-left: 125px;">비밀번호는 영문, 숫자, 특수문자를 포함하여 8자이상 16자이하로 설정.</span>
										</div>
									</td>
								</tr>
								<tr>
									<th scope="col">비밀번호 확인 <span class="red">*</span></th>
									<td class="ta_left">
										<div class="form-inline">
											<input type="password" class="form-control form_pw"
												id="passWordCk" name="passWordCk" title="비밀번호 확인">
										</div>
									</td>
								</tr>
								<tr>
									<th scope="col">이름 <span class="red">*</span></th>
									<td class="ta_left">
										<div class="form-inline">
											<input type="text" class="form-control form_man_name"
												id="userName" name="user_name" maxlength="20" title="이름">
										</div>
									</td>
								</tr>
								<tr>
									<th scope="col">개인이메일 <span class="red">*</span></th>
									<td class="ta_left">
										<div class="form-inline">
											<input type="text" class="form-control form_email1" id="userEmail1" name="user_email1" onkeyup="this.value=this.value.replace(/[\ㄱ-ㅎㅏ-ㅣ가-힣]/g, '');" maxlength="30" title="담당자 이메일아이디">
											@ 
											<input type="text" class="form-control form_email2" id="userEmail2" name="user_email2" maxlength="30" title="담당자 이메일 도메인 직접입력"> 
											<select class="form-control form_email3" id="userEmail3" name="user_email3" onChange="fncChangeEmail(this);" title="담당자 이메일주소3">
												<option title="직접입력">직접입력</option>
												<option title="네이버">naver.com</option>
												<option title="G메일">gmail.com</option>
												<option title="다음">daum.net</option>
											</select>
										</div>
									</td>
								</tr>
								<tr>
									<th scope="col">휴대전화 번호 <span class="red">*</span></th>
									<td class="ta_left">
										<div class="form-inline">
											<input type="text" 
												onKeyup="this.value=this.value.replace(/[^0-9]/g,'');"
												class="form-control form_man_name" id="userMobileNo"
												name="user_mobile_no" title="휴대전화">
										</div>
									</td>
								</tr>

							</tbody>
							<tfoot></tfoot>
						</table>
					</div>
				</div>
				<div class="area_cont area_cont2">
					<div class="subject_corp w_top">
						<h4>기업정보</h4>
						<p class="es">* 표시는 필수 입력 사항입니다.</p>
					</div>
					<div class="tbl_view tbl_public">
						<table class="tbl">
							<caption>회원가입 회원 기업정보</caption>
							<colgroup>
								<col style="width: 15%">
							</colgroup>
							<thead></thead>
								<col>
							<tbody class="view">
								<tr>
									<th scope="col">회사명 <span class="red">*</span></th>
									<td class="ta_left">
										<div class="form-inline">
											<input type="text" 
												id="bizName" name="biz_name" title="회사명">
										</div>
									</td>
								</tr>
								<tr>
									<th scope="col">부서명 <span class="red">*</span></th>
									<td class="ta_left">
										<div class="form-inline">
											<input type="text" class="form-control form_dept"
												id="userDepart" name="user_depart" maxlength="50"
												title="담당자 부서">
										</div>
									</td>
								</tr>
								<tr>
									<th scope="col">직급 <span class="red">*</span></th>
									<td class="ta_left">
										<div class="form-inline">
											<input type="text" class="form-control form_spot"
												id="userRank" name="user_rank" maxlength="20" title="담당자 직위">
										</div>
									</td>
								</tr>
								<tr>
									<th scope="col">업무용이메일 <span class="red">*</span></th>
									<td class="ta_left">
										<div class="form-inline">
											<input type="text" class="form-control form_email1"	id="bizEmail1" name="biz_email1" onkeyup="" maxlength="30" title="담당자 이메일아이디">
											@ 
											<input type="text" class="form-control form_email2" id="bizEmail2" name="biz_email2" maxlength="30" title="담당자 이메일 도메인 직접입력"> 
												<select class="form-control form_email3" id="bizEmail3"name="biz_email3" onChange="fncChangeEmail(this);" title="담당자 이메일주소3">
												<option title="직접입력">직접입력</option>
												<option title="네이버">naver.com</option>
												<option title="G메일">gmail.com</option>
												<option title="다음">daum.net</option>
											</select>
										</div>
									</td>
								</tr>
								<tr>
									<th scope="col">회사용직통전화번호 <span class="red">*</span></th>
									<td class="ta_left">
										<div class="form-inline">
											<input type="text"
												onKeyup="this.value=this.value.replace(/[^0-9]/g,'');"
												class="form-control form_man_name" id="bizTelNo" name="biz_tel_no"
												title="회사용직통전화번호">
										</div>
									</td>
								</tr>
							</tbody>
							<tfoot></tfoot>
						</table>
					</div>
				</div>
			</form>
			<div class="wrap_btn _center">
				<a href="javascript:history.back();" class="btn_cancel" title="취소">취소</a>
				<a href="javascript:void(0);" onclick="fncMemberJoin();" class="btn_confirm"title="회원가입">회원가입 </a> 
				<a href="javascript:fncSetData();"class="btn_confirm">데이터입력 </a>
			</div>
			<!-- //page_content e:  -->
		</div>
	</div>
</div>