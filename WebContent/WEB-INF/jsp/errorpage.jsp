<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page isErrorPage="true"%>
<%@ include file="include/top.jsp"%>
<%@ page import="java.io.PrintWriter"%>
<%@ page import="java.io.StringWriter"%>
<title>Σφάλμα</title>
<%@ include file="include/head.jsp"%>
<%@ include file="include/no_menu.jsp"%>
<div class="centered">
	<h1>Σφάλμα</h1>

	Έγινε ένα σοβαρό λάθος !! Λυπούμαστε για την αναστάτωση. <br />
	Παρακαλώ δοκιμάστε αργότερα.<br /> <br /> Το λάθος είναι : "<%=exception.getMessage()%>"
	<br /> <br /> <a href="/ted2012/home">Συνέχισε
		στην αρχική σελίδα</a><br /> <br /> StackTrace:<br />
	<%
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		exception.printStackTrace(printWriter);
		out.println(stringWriter);
		printWriter.close();
		stringWriter.close();
	%>
</div>
<%@ include file="include/bottom.jsp"%>

