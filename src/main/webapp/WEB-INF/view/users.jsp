<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<html>
<jsp:include page="fragments/headTagUser.jsp"/>
<body>
<jsp:include page="fragments/header.jsp"/>
<h3><a href="${pageContext.request.contextPath}/"><spring:message code="users.home"/></a></h3>
<hr>
<h2><spring:message code="users.heading1"/></h2>
    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
            <tr>
                <th><spring:message code="users.name"/></th>
                <th><spring:message code="users.email"/></th>
                <th>password</th>
                <th><spring:message code="users.enabled"/></th>
                <th><spring:message code="users.registered"/></th>
                <th><spring:message code="users.caloriesPerDay"/></th>
                <th><spring:message code="users.roles"/></th>
            </tr>
        </thead>
        <c:set var="user" value="${user}"/>
<%--        <jsp:useBean id="user" type="ru.caloriesmanager.model.UserTO"/>--%>
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
    <h3><a href="meals/meals"><spring:message code="users.heading2"/></a></h3>
</body>
<jsp:include page="fragments/footer.jsp"/>
</html>