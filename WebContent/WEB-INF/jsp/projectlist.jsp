<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="include/top.jsp"%>
<%@ taglib prefix="u" uri="functions" %>
<title>Λίστα Έργων</title>
<%@ include file="include/head.jsp"%>
<%@ include file="include/admin_menu.jsp"%>
<h1>Λίστα Έργων</h1>
<p>
	<a href="createproject">Δημιουργία νέου Έργου</a>
</p>
<h3>Έργα</h3>
<%@ include file="include/error_begin.jsp"%>
<c:choose>
	<c:when test="${empty projectlist}">Δεν υπάρχουν Διαθέσιμα Έργα</c:when>
	<c:otherwise>
		<form action="deleteproject" method="post" name="deleteProjectForm">
			<table style="width: 200">
				<tr>
					<th class="tables" scope="col">A/A</th>
					<th class="tables" scope="col">Όνομα</th>
					<td class="hidden" scope="col"><input
						name="deleteProjectButton" type="submit" value="Διαγραφή" /></td>
				</tr>
				<c:forEach var="projectName" items="${projectlist}" varStatus="i">
					<tr>
						<td class="tables"><div align="center">${i.count}</div></td>
						<td class="tables"><a href="${u:encodeURI('project?name=',projectName)}"><c:out value="${projectName}"></c:out></a></td>
						<td class="hidden"><input name="deleteProject"
							type="checkbox" value="${projectName}" /></td>
					</tr>
				</c:forEach>
			</table>
		</form>
		<h1>&nbsp;</h1>
	</c:otherwise>
</c:choose>
<%@ include file="include/error_end.jsp"%>
<%@ include file="include/bottom.jsp"%>
