<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
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

<script>
function execDaumPostcode() {
    new daum.Postcode({
        oncomplete: function(data) {
            // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

            // 각 주소의 노출 규칙에 따라 주소를 조합한다.
            // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
            var addr = ''; // 주소 변수
            var extraAddr = ''; // 참고항목 변수

            //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
            if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                addr = data.roadAddress;
            } else { // 사용자가 지번 주소를 선택했을 경우(J)
                addr = data.jibunAddress;
            }

            // 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
            if(data.userSelectedType === 'R'){
                // 법정동명이 있을 경우 추가한다. (법정리는 제외)
                // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
                if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                    extraAddr += data.bname;
                }
                // 건물명이 있고, 공동주택일 경우 추가한다.
                if(data.buildingName !== '' && data.apartment === 'Y'){
                    extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                }
                // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
                if(extraAddr !== ''){
                    extraAddr = ' (' + extraAddr + ')';
                }
                // 조합된 참고항목을 해당 필드에 넣는다.
                document.getElementById("extraAddress").value = extraAddr;
            
            } else {
                document.getElementById("extraAddress").value = '';
            }

            // 우편번호와 주소 정보를 해당 필드에 넣는다.
            document.getElementById('postcode').value = data.zonecode;
            document.getElementById("address").value = addr;
            
            // 커서를 상세주소 필드로 이동한다.
            document.getElementById("detailAddress").focus();
        }
    }).open();
}
	
var idCheck = false; //아이디 중복검사 체크
var pwCheck = false; //패스워드 중복검사 체크
const idRegex = /^[a-zA-Z0-9]{1,16}$/;//대소문자영문 숫자포함한 정규식
                       //?=.*[a-zA-Z] 적어도 하나 이상의 문자 ㄷ     
const passwordRegex = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[@#$%^&*!])[A-Za-z\d@#$%^&*!]{8,16}$/; //영문,숫자,특수문자를 포함한 8자이상 16자 이하 정규식

$(document).ready(function(){
	// 유저 이메일 주소 직접 입력이 아닌 경우 1.
	$('#userEmail2').keyup(function(){
		$(this).val($(this).val().replace(/[\ㄱ-ㅎㅏ-ㅣ가-힣]/g, '')); //문자열을 반환 -> 그 값을 기입
		if($('#userEmail3').val() !== '직접입력'){
			$('#userEmail3').val("직접입력");
			console.log("틀림", $('#userEmail3').val());
		}
	});
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
	
	
}); //ready end
	
	//[회원가입] - 아이디 중복확인 -> 2024/05/18(토)
	function fncDoubleCheck(gubun){
		if(gubun == 'ID'){
			var id = $('#id').val();
			console.log(id)
		}
		if(id == '' || id == null){
			alert_popup_focus('아이디를 입력 후 중복확인 버튼을 클릭해주세요.', '#id');
			return false;
	    }
		$.ajax({
			type : 'POST',
			url : '/member/memberDoubleCheck.do',
			data : {
				gubun : gubun,
				id : id
			},
			dataType : 'json',
			success : function(data) {
				console.log("success Data:" + data.memberCount)
            	var memberCount = data.memberCount;
				if(memberCount == '1'){
					alert_popup_focus('중복된 아이디가 있습니다. 다른 아이디를 사용해주세요!', '#id');
					idCheck = false;
				} else if(id.length < 3){
					alert_popup_focus('아이디를 3글자 이상 입력해주세요.', '#id');
					return false;    //추가적인 동작을 하지 않고 함수를 종료하고자 할 때 사용될 것
				} else if(id.length >= 3){
					console.log("idcheck = true전"+idCheck)
					idCheck = true;
					console.log("idcheck = true후"+idCheck)
					alert_popup_focus('사용가능한 아이디 입니다.', '#pw');
				}
			},
			error:function(){
				console.error('응 에러야')
			},
			complete:function(){
				
			}
	   })     
	
	}
	
function alert_popup_focus(message, selector){
	alert(message);
	$(selector).focus();
}

function alert_popup(message, selector){
	alert(message);
	$(selector).focus();
}



function isBlank(message, id){
		console.log("isBlank:"+ id); //#id
		console.log($(id).val()); // 여기가 문제다!!!   <input> 요소에는 적용되지 않습니다.
		if($.trim($(id).val()) == ''){

			alert(message);
			return true;
		} else {
			return false;
		}
		
	} 
//회원가입 -> 2024/05/16 osm  $.trim($("selector").html())
function fncMemberJoin(){
	//개인정보 유효성 검사 
	if(!isBlank('아이디', "#id")){ // 비어있어?true -> false 
		var id = $('#id').val();
		if(idRegex.test(id)){
			//중복을 체크 안해주고 가입하면 false로 반환
			if(!idCheck){
				alert_popup_focus('아이디 중복확인을 해주세요.',"#id");
				return false;
			}else{
				if(!isBlank('비밀번호', '#pw')){
					if(!isBlank('비밀번호 확인', '#passWordCk')){
						var pw = $('#pw').val();
						var pwChk = $('#passWordCk').val();
						if(passwordRegex.test(pw)){
							if(pw == pwChk){
								if(!isBlank('이름', '#userName')){
									if(!isBlank('개인이메일', '#userEmail1')){
										if(!isBlank('개인이메일 도메인', '#userEmail2')){
											if(!isBlank('휴대전화번호', '#userMobileNo')){
												if(!isBlank('전체 주소', '#fullAddress')){
													var address = $("#address").val();
										            var detailAddress = $("#detailAddress").val();
										            console.log("전체 주소:" + address + detailAddress);
										            $("#full_addr").value(address + detailAddress);
													
												}				            
													var url = "/member/memberJoin.do"
													var form = $('#frm')[0];
													var data = new FormData(form);
													console.log("form:" + form); 
													console.log("data:" + data); 
														$.ajax({
														       url : url,
														       type: "post",
														       processData: false,
														       contentType: false,
														       data: data,
														       dataType: "json",
														       success : function(res){
															       alert_popup("회원가입이 완료되었습니다. 로그인창으로 이동합니다.","/member/login.do");
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
							}else{
								alert_popup_focus("비밀번호와 비밀번호 확인이  일치하지않습니다.","#pws");
							}
						}else{
							alert_popup_focus("비밀번호는 영문, 숫자, 특수문자를 포함하여 8자이상 16자이하로 설정해주세요",'#pw');
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
	location.href = "/member/memberJoinCompletePage.do";
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
													id="A" value="ADMIN" title="관리자" checked /><label for="R"
													class="lab_radio"><span class="icon ico_radio"></span>관리자</label>
											</div>
											<div class="box_radioinp">
												<input type="radio" class="inp_radio" name="member_type"
													id="B" value="BIZ" title="기업" /><label for="B"
													class="lab_radio"><span class="icon ico_radio"></span>기업 계정</label>
											</div>
											<div class="box_radioinp">
												<input type="radio" class="inp_radio" name="member_type"
													id="C" value="CUSTOMER" title="C" /><label for="C"
													class="lab_radio"><span class="icon ico_radio"></span>일반 고객</label>
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
								<!-- daum postal code 넣기 -->
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
											<input 
												type="text" 
												onKeyup="this.value=this.value.replace(/[^0-9]/g,'');"
												class="form-control form_man_name" 
												id="userMobileNo"
												name="user_mobile_no" 
												title="휴대전화"
											>
										</div>
									</td>
								</tr>
								 <tr>
									<td align="center">주소</td> <!-- onclick은 무조건 javascript와 연결. 굳이 javascript 안 적어줘도 됨 -->
									<td>
									<input type="text" id="postcode" placeholder="우편번호">
									<input type="button" onclick="execDaumPostcode()" value="우편번호 찾기"><br>
									<input type="text" id="address" placeholder="주소"><br>
									<input type="text" id="detailAddress" placeholder="상세주소">
									<input hidden type="text" id="fullAddress" name="address">
									<input type="text" id="extraAddress" placeholder="참고항목">
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