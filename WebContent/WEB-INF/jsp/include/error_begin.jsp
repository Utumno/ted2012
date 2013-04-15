<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="java.util.Map"%>
<%
	if (request.getAttribute("ErrorString") != null) {
%>
Υπήρξε λάθος : ${requestScope.ErrorString}
<%} else { %>
<c:if test="${param.r != null}">
	<div class="success" align="center">
		<c:out value="${sessionScope.messages[param.r]}" />
	</div>
</c:if>
