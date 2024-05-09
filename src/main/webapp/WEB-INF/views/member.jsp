<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<html>
<head>
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css"/>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <title>Members List</title>
</head>
<body>
<script>
 $(document).ready(function(){
 	// member_type이 ADMIN의 경우 가입 승인 가능
 	function funcSignUpApproval(mode, seqno){
	 	var member_type = '<%=(String)session.getAttribute("member_type") %>';
	 	if(member_type === 'ADMIN'){
	 	  $.ajax({
	 	  	     type: 'GET',
	 	  	     url: '/admin/signUpApproval.do?seqno=' + seqno
	 	  	     success:function(data){
	 	  	     	console.log(data); // {}
	 	  	     
	 	  	     },
	 	  	     error:function(){},
	 	  	     complete: function(){}
	 	  
	 	  })
	 	  
	 	}
 	}
 	
 	
 	
 	
 })

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
                    <td>${member.signup_approval}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    
    
</body>
</html>
