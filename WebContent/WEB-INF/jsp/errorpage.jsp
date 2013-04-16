<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page isErrorPage="true"%>
<%@ include file="include/top.jsp"%>
<title>Σφάλμα</title>
<%@ include file="include/head.jsp"%>
<%@ include file="include/no_menu.jsp"%>
<div class="centered">
	<h1>Σφάλμα</h1>
	Έγινε ένα σοβαρό λάθος. Παρακαλώ δοκιμάστε αργότερα. <br /> <a
		href="/ted2012/home">Αρχική σελίδα</a><br /> <br /> StackTrace:<br />
	<div style="text-indent:-50px">${pageContext.exception.message} at</div>
	<div style="font-size: 0.875em">
		<c:forEach var="trace" items="${pageContext.exception.stackTrace}">
		${trace}
	</c:forEach>
	</div>
</div>
<%@ include file="include/bottom.jsp"%>
