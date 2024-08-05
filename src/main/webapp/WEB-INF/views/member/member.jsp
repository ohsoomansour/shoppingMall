
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
 <html>

<head>
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css"/>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    
    <title>Members List</title>
</head>
<body>
 <script>



 function funcSignUpApproval(no){
	 	  console.log("Sending AJAX request for id:", no);
	 	  $.ajax({
	 	  	     type : 'GET',
	 	  	     url :'/admin/signUpApproval.do?u_id='+ no,
	 	  	     headers: {
					"Content-Type":"application/json;charset=utf-8",
				  },			
	 	  	     dataType:'json',
	 	  	     contentType: 'application/json', // 요청 본문의 타입을 JSON으로 설정
	 	  	     success: function(res){
	                // 요청이 성공한 경우 추가적인 작업 수행
	                console.log('Success:');
	             },
	 	  	     error:function(e){
	 	  	      console.error(e);  
	 	  	     },
	 	  	     complete : function() {
						
				}
	 	  })
	 	
 	}

</script>

  <h1>Members</h1>
  <table border="1">
      <thead>
          <tr>
              <th>ID         </th>
              <th>이름</th>
              <th>아이디</th>
              <th>주소</th>
              <th>사용 승인</th>
              <th>가입 날짜</th>
          </tr>
      </thead>
      <tbody>
 
          <c:forEach var="user" items="${users}">
              <tr>
                  <td>${user.u_id}</td>
                  <td>${user.u_name}</td>
              	  <td>${user.u_email}</td>
                  <td>${user.u_address}</td>
                  <td>${user.agree_flag_date}</td>   
                  <td>${user.created_at}</td>
                  <td><button onClick="funcSignUpApproval('${user.u_id}')">${user.agree_flag ? '승인완료' : '비승인'}</button></td>
              </tr>
          </c:forEach>
          
      </tbody>
      
  </table>

 </body>   

</html>	
