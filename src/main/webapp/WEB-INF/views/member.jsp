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
                <th>ID</th>
                <th>First Name</th>
                <th>Last Name</th>
                <th>Address</th>
                <th>Join Date</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="member" items="${members}">
                <tr>
                    <td>${member.firstName}</td>
                    <td>${member.lastName}</td>
                    <td>${member.address}</td>
                    <td>${member.joinDate}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
</html>
