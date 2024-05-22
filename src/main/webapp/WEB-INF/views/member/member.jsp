
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


  /* 
 member_type이 ADMIN의 경우 가입  가능
 if(member_type === 'ADMIN'){
	 	  
	 	}
  */

 function funcSignUpApproval(no){
	 	  console.log("Sending AJAX request for id:", no);
	 	  $.ajax({
	 	  	     type : 'GET',
	 	  	     url :'/admin/signUpApproval.do?no='+ no,
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
 	/**/
 function funcTest(){
	 	  
	 	  $.ajax({
	 	  	     type : 'POST',
	 	  	     url :'/admin/doTest.do',
	 	  	     data: {
			        key1: "SUCCESS",
			        key2: "SUCCESS",
			     },
	 	  	     dataType : 'json',
	 	  	     success: function(res){
	                // 요청이 성공한 경우 추가적인 작업 수행
	                console.log('Success: ' + res);
	             },
	 	  	     error:function(){
	 	  	      
	 	  	     },
	 	  })
	 	
 }	

 



</script>

  <h1>Members</h1>
  <button onClick='funcTest()'>테스트</button>  
  <table border="1">
      <thead>
          <tr>
              <th>ID         </th>
              <th>User Name  </th>
              <th>User Email </th>
              <th>Address    </th>
              <th>사용 승인</th>
              <th>Join date  </th>
          </tr>
      </thead>
      <tbody>
 
          <c:forEach var="member" items="${members}">
              <tr>
                  <td>%{member.no}</td>
              	  <td>${member.id}</td>
                  <td>${member.user_name}</td>
                  <td>${member.user_email}</td>
                  <td>${member.address}</td>
                  <td>${member.created_at}</td>
                  <td><button onClick="funcSignUpApproval('${member.no}')">가입 승인</button>${member.agree_flag}</td>
              </tr>
          </c:forEach>
          
      </tbody>
  </table>

 </body>   

</html>	
