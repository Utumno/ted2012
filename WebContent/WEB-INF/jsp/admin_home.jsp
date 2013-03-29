<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="include/top.jsp"%>
<title>Σελίδα Διαχείρισης</title>
<%@ include file="include/head.jsp"%>
<%@ include file="include/admin_menu.jsp"%>
<h1>Σελίδα Διαχείρισης</h1>
<form id="form1" name="form1" method="post" action="userlist">
	<p>
		<input type="submit" name="userlist" id="userlist"
			value="Λίστα Χρηστών" />
	</p>
</form>
<form id="form2" name="form2" method="post" action="projectlist">
	<p>
		<input type="submit" name="projectlist" id="projectlist"
			value="Λίστα Έργων" />
	</p>
</form>
<h1>&nbsp;</h1>
<%@ include file="include/bottom.jsp"%>
