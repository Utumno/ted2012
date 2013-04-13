<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="java.util.Map"%>
<% if(request.getAttribute("ErrorString") != null){ %>
Υπήρξε λάθος : ${requestScope.ErrorString}
<%} else { %>
<c:if test="${param.r != null}">
	<div class="success" align="center">
		<%
		HttpSession ses = (HttpSession)  request.getSession(false);
		if(ses != null){
			Map<String, String> m = (Map<String, String>) ses.getAttribute("messages");
			if(m != null){ %>
		<%=m.get(request.getParameter("r")) %>
		<%}
		}
	 %><c:set var="key" value="${param.r}"></c:set>
<%-- 		<c:out value="${key}" /> --%>
<%-- 		${sessionScope.messages[key]} --%>
<%-- 		${sessionScope.messages} prints nothing --%>
		sess scope : ${sessionScope} <%-- prints {} --%>
<%-- 		<c:out value="${sessionScope.messages[key]}" /> --%>
<%-- 		<c:out value=" ${messages[param.r]}" /> --%>
	</div>
</c:if>
<c:forEach items="${messages}" var="entry">
    Key = ${entry.key}, value = ${entry.value}<br>
</c:forEach>
