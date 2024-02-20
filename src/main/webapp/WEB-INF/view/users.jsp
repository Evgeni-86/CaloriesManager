<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!--<fmt:setBundle basename="messages.app"/>-->

<html>
<jsp:include page="fragments/headTagUser.jsp"/>
<body>
<jsp:include page="fragments/header.jsp"/>
<h3><a href="${pageContext.request.contextPath}/"><fmt:message key="users.home"/></a></h3>
<hr>
<h2><fmt:message key="users.heading1"/></h2>
    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
            <tr>
                <th><fmt:message key="users.name"/></th>
                <th><fmt:message key="users.email"/></th>
                <th>password</th>
                <th><fmt:message key="users.enabled"/></th>
                <th><fmt:message key="users.registered"/></th>
                <th><fmt:message key="users.caloriesPerDay"/></th>
                <th><fmt:message key="users.roles"/></th>
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
    <h3><a href="meals"><fmt:message key="users.heading2"/></a></h3>
</body>
<jsp:include page="fragments/footer.jsp"/>
</html>