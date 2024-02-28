<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


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

    <form:form method="POST" action="edit" modelAttribute="meal">
        <form:input type="hidden" path="id"/>
        <dl>
            <dt><spring:message code="mealForm.dateTime"/>:</dt>
            <dd><form:input type="datetime-local" path="dateTime" value="${meal.formattedDateTime}"/></dd>
        </dl>
        <dl>
            <dt><spring:message code="mealForm.description"/>:</dt>
            <dd><form:input type="text" path="description"/>
                <form:errors path="description"/>
            </dd>
        </dl>
        <dl>
            <dt><spring:message code="mealForm.calories"/>:</dt>
            <dd><form:input type="number" path="calories"/>
                <form:errors path="calories"/>
            </dd>
        </dl>
        <button type="submit"><spring:message code="mealForm.save"/></button>
        <button onclick="window.history.back()" type="button"><spring:message code="mealForm.cancel"/></button>
    </form:form>

</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
