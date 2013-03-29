<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="include/top.jsp"%>
<%@ include file="include/tag_libs.jsp"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.ted.domain.User"%>
<title>Λίστα Χρηστών</title>
<%@ include file="include/head.jsp"%>
<%@ include file="include/admin_menu.jsp"%>
<h1>Λίστα Χρηστών</h1>
<%@ include file="include/error_begin.jsp"%>
<c:choose>
	<c:when test="${empty allUsers}">Δεν υπάρχουν εγγεγραμμένοι χρήστες</c:when>
	<c:otherwise>
		<table style="width: 200" border="1">
			<tr>
				<th scope="col">A/A</th>
				<th scope="col">username</th>
				<th scope="col">Ρόλος</th>
			</tr>
			<c:forEach var="user" items="${allUsers}" varStatus="i">
				<tr>
					<td class="tables"><div align="center">${i.count}</div></td>
					<td class="tables"><a href="${u:encodeURI('profile?user=',user.username)}">${user.username}</a></td>
					<td class="tables">
					<c:choose>
						<c:when test="${user.getRole().toString() eq 'GUEST'}">Επισκέπτης</c:when>
						<c:when test="${user.getRole().toString() eq 'STAFF'}">Προσωπικό</c:when>
						<c:when test="${user.getRole().toString() eq 'MANAGER'}">Υπεύθυνος
										Έργου</c:when>
					</c:choose>
					</td>
				</tr>
			</c:forEach>
		</table>
		<h1>&nbsp;</h1>
	</c:otherwise>
</c:choose>
<%@ include file="include/error_end.jsp"%>
<%@ include file="include/bottom.jsp"%>
