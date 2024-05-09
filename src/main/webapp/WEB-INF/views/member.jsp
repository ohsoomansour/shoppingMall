<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<html>
<head>
    <title>Members List</title>
</head>
<body>
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
    <script>
    
    
    
    </script>
    
</body>
</html>
