<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <title>User</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>User</h2>
    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
            <tr>
                <th>name</th>
                <th>email</th>
                <th>password</th>
                <th><enabled/th>
                <th>registered</th>
                <th>caloriesPerDay</th>
                <th>roles</th>
            </tr>
        </thead>
        <c:set var="user" value="${user}"/>
        <jsp:useBean id="user" type="ru.caloriesmanager.transferObject.UserTO"/>
        <tr>
            <td>${user.name}</td>
            <td>${user.email}</td>
            <td>${user.password}</td>
            <td>${user.enabled}</td>
            <td>${user.registered}</td>
            <td>${user.caloriesPerDay}</td>
            <td>${user.roles}</td>
        </tr>
    </table>
    <h3><a href="meals">Meals list</a></h3>
</body>
</html>