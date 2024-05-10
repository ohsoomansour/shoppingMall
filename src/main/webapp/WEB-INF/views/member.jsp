<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>



<head>
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css"/>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <title>Members List</title>
</head>
 <script>
 /* 
 $(document).ready(function(){
 	//  가입 승인 확인 함수 
 	
 
 })
 member_type이 ADMIN의 경우 가입  가능
 if(member_type === 'ADMIN'){
	 	  
	 	}
  */

  
 function funcSignUpApproval(id){
	 	  console.log("Sending AJAX request for id:", id);
	 	  $.ajax({
	 	  	     type : 'POST',
	 	  	     url :'/admin/signUpApproval.do?seqno='+id,
	 	  	     //dataType : 'json',
	 	  	     success: function(){
	                // 요청이 성공한 경우 추가적인 작업 수행
	                console.log('Success:');
	             },
	 	  	     error:function(e){
	 	  	      console.error(e);  // 무슨 에러: 
	 	  	     },
	 	  	     
	 	  
	 	  })
	 	
 	}

</script>

   <h1>Members</h1>

  <table border="1">
      <thead>
          <tr>
              <th>ID         </th>
              <th>First Name </th>
              <th>Last Name  </th>
              <th>Address    </th>
              <th>Join date  </th>
              <th>사용 승인</th>
          </tr>
      </thead>
      <tbody>
          <c:forEach var="member" items="${members}">
              <tr>
              	<td>${member.id}</td>
                  <td>${member.first_name}</td>
                  <td>${member.last_name}</td>
                  <td>${member.address}</td>
                  <td>${member.created_at}</td>
                  <td><button onClick="funcSignUpApproval('${member.id}')">가입 승인</button>${member.signup_approval}</td>
                  
              </tr>
          </c:forEach>
      </tbody>
  </table>

    


