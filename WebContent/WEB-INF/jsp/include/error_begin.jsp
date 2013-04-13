<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="java.util.Map"%>
<%
	if (request.getAttribute("ErrorString") != null) {
%>
Υπήρξε λάθος : ${requestScope.ErrorString}
<%} else { %>
<!-- taglib declaration does nothing - it is included via top.jsp in all jsps - I added it here to shut Eclipse warnings and "just in case"-->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:if test="${param.r != null}">
	<div class="success" align="center">
		<%
		HttpSession ses = (HttpSession) request.getSession(false);
		if (ses != null) {
			Map<String, String> m = (Map<String, String>) ses
					.getAttribute("messages");
			if (m != null) {
		%>
		<%=m.get(request.getParameter("r"))%>
		<%
			}
		}
		%><c:set var="key" value="${param.r}" /><br />
		<c:out value="cout : key : ${key}" />
		<br />
		<!-- prints the key alright -->
		sessionScope.messages[key] : ${sessionScope.messages[key]}<br />
		sessionScope.messages : ${sessionScope.messages}<br /> sess scope :
		${sessionScope}
		<%-- prints {} --%>
		<br />
		<c:out
			value="cout : sessionScope.messages[key] : ${sessionScope.messages[key]}" />
		<br />
		<c:out
			value="cout : messages[param.r] : ${sessionScope.messages[param.r]}" />
	</div>
</c:if>
<c:forEach items="${messages}" var="entry">
    Key = ${entry.key}, value = ${entry.value}<br>
</c:forEach>
