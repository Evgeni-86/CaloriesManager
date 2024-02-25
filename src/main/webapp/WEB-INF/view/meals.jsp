<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="ru/caloriesmanager/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<html>
<jsp:include page="fragments/headTagMeals.jsp"/>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <h3><a href="${pageContext.request.contextPath}/"><spring:message code="meals.home"/></a></h3>
    <hr/>
    <h2><spring:message code="meals.heading1"/></h2>
    <form method="get" action="filter">
        <input type="hidden" name="action" value="filter">
        <dl>
            <dt><spring:message code="meals.fromDate"/>:</dt>
            <dd><input type="date" name="startDate" value="${param.startDate}"></dd>
        </dl>
        <dl>
            <dt><spring:message code="meals.toDate"/>:</dt>
            <dd><input type="date" name="endDate" value="${param.endDate}"></dd>
        </dl>
        <dl>
            <dt><spring:message code="meals.fromTime"/>:</dt>
            <dd><input type="time" name="startTime" value="${param.startTime}"></dd>
        </dl>
        <dl>
            <dt><spring:message code="meals.toTime"/>:</dt>
            <dd><input type="time" name="endTime" value="${param.endTime}"></dd>
        </dl>
        <button type="submit"><spring:message code="meals.filter"/></button>
    </form>
    <a href="create?action=create"><spring:message code="meals.addMeal"/></a>
    <br><br>
    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th><spring:message code="meals.date"/></th>
            <th><spring:message code="meals.description"/></th>
            <th><spring:message code="meals.calories"/></th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <c:forEach items="${meals}" var="meal">
<%--            <jsp:useBean id="meal" type="ru.caloriesmanager.model.UserMealWithExcess"/>--%>
            <tr data-mealExcess="${meal.excess}">
                <td>${fn:formatDateTime(meal.dateTime)}</td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="update?action=update&id=${meal.id}"><spring:message code="meals.update"/></a></td>
                <td><a href="delete?action=delete&id=${meal.id}"><spring:message code="meals.delete"/></a></td>
            </tr>
        </c:forEach>
    </table>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>