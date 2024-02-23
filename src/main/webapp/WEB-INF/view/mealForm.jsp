<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<html>
<jsp:include page="fragments/headTagMealForm.jsp"/>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <h3><a href="${pageContext.request.contextPath}/"><spring:message code="mealForm.home"/></a></h3>
    <hr>
    <h2>
    <c:choose>
    <c:when test="${param.action == 'create'}"><spring:message code="mealForm.create"/></c:when>
    <c:otherwise><fmt:message key="mealForm.edit"/></c:otherwise>
    </c:choose>
    </h2>
<%--    <jsp:useBean id="meal" type="ru.caloriesmanager.model.Meal" scope="request"/>--%>
    <form method="post" action="edit">
        <input type="hidden" name="id" value="${meal.id}">
        <dl>
            <dt><spring:message code="mealForm.dateTime"/>:</dt>
            <dd><input type="datetime-local" value="${meal.dateTime}" name="dateTime" required></dd>
        </dl>
        <dl>
            <dt><spring:message code="mealForm.description"/>:</dt>
            <dd><input type="text" value="${meal.description}" size=40 name="description" required></dd>
        </dl>
        <dl>
            <dt><spring:message code="mealForm.calories"/>:</dt>
            <dd><input type="number" value="${meal.calories}" name="calories" required></dd>
        </dl>
        <button type="submit"><spring:message code="mealForm.save"/></button>
        <button onclick="window.history.back()" type="button"><spring:message code="mealForm.cancel"/></button>
    </form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
