<%@page import="com.ted.domain.User.RolesENUM"%>
<%@page import="com.ted.domain.User"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="include/tag_libs.jsp"%>
<%@ include file="include/top.jsp"%>
<title>Δημιουργία Έργου</title>
<%@ include file="include/head.jsp"%>
<%@ include file="include/admin_menu.jsp"%>
<h1>Δημιουργία Νέου Έργου</h1>
<%@ include file="include/error_begin.jsp"%>
<form id="createProjectForm" name="createProjectForm" method="post"
	action="createproject">
	<table style="width: 590">
		<tr>
			<td width="170">Όνομα</td>
			<td width="170"><input type="text" name="name" id="name"
				<c:if test="${requestScope.name != null}">
							value="${requestScope.name}"</c:if> /></td>
			<td class="error" width="250"><c:choose>
					<c:when test="${requestScope.emptyName != null}">Το πεδίο είναι υποχρεωτικό</c:when>
					<c:when test="${requestScope.duplicateProjectName != null}">Το όνομα χρησιμοποιείται ήδη</c:when>
				</c:choose></td>
		</tr>
		<tr>
			<td>Περιγραφή</td>
			<td><input type="text" name="description" id="description"
				<c:if test="${requestScope.description != null}">	value="${requestScope.description}"</c:if> /></td>
			<td class="error"><c:choose>
					<c:when test="${requestScope.emptyDescription != null}">Το πεδίο είναι υποχρεωτικό</c:when>
				</c:choose></td>
		</tr>
		<tr>
			<td>Δημόσιο/Ιδιωτικό</td>
			<td><label for="publik"></label> <select name="publik"
				id="publik">
					<%
						String publik = (String) request.getAttribute("publik");
					%>
					<option <%if (publik != null && publik.equals("private")) {%>
						selected="selected" <%}%> value="private">Ιδιωτικό</option>
					<option <%if (publik != null && publik.equals("publik")) {%>
						selected="selected" <%}%> value="publik">Δημόσιο</option>
			</select></td>
			<td class="error"><c:choose>
					<c:when test="${requestScope.emptyPublik != null}">Το πεδίο είναι υποχρεωτικό</c:when>
				</c:choose></td>
		</tr>
		<tr>
			<%
				String selectedManager = (String) request
							.getAttribute("selectedManager");
					List<String> allManagers = (List<String>) request
							.getAttribute("allManagers");
			%>
			<td>Υπεύθυνος Έργου</td>

			<td>
				<%
					if (allManagers.size() > 0) {
				%> <select name="manager" id="manager">
					<%
						for (int i = 0; i < allManagers.size(); i++) {
									if (i == 0 && selectedManager == null) {
					%>
					<option selected="selected" value="<%=allManagers.get(i)%>"><%=allManagers.get(i)%></option>
					<%
						} else {
					%>
					<option
						<%if (selectedManager != null
									&& selectedManager.equals(allManagers
											.get(i))) {%>
						selected="selected" <%}%> value="<%=allManagers.get(i)%>"><%=allManagers.get(i)%></option>
					<%
						}
								}
					%>
			</select> <%
 	} else {
 %> Δεν υπάρχουν διαθέσιμοι managers <%
 	}
 %>
			</td>
			<td class="error"><c:choose>
					<c:when test="${requestScope.emptyManager != null}">Το πεδίο είναι υποχρεωτικό</c:when>
				</c:choose></td>
		</tr>
		<tr>
			<td valign="top">Προσωπικό</td>
			<td>
				<%
					List<String> addedStaff = (List<String>) request
								.getAttribute("addedStaff");
						List<String> allStaff = (List<String>) request
								.getAttribute("allStaff");
						if (addedStaff.size() > 0) {
				%>
				<table name="addedStaff" width="100%" border="1">

					<%
						for (int i = 0; i < addedStaff.size(); i++) {
					%>
					<tr>
						<td><input type="hidden" name="added"
							value="<%=addedStaff.get(i)%>" /><%=addedStaff.get(i)%></td>
					</tr>
					<%
						}
					%>
				</table> <%
 	} else {
 %> Δεν έχει προστεθεί προσωπικό <%
 	}
 %>
			</td>
			<td class="error"><c:choose>
					<c:when test="${requestScope.emptyStaff != null}">Το πεδίο είναι υποχρεωτικό</c:when>
				</c:choose></td>
		</tr>
		<tr>
			<td></td>
			<td>
				<%
					if (allStaff.size() > 0) {
				%> <select name="staffMember" id="staffMember">
					<%
						for (int i = 0; i < allStaff.size(); i++) {
					%>
					<option value="<%=allStaff.get(i)%>">
						<%=allStaff.get(i)%></option>
					<%
						}
					%>
			</select>
			</td>
			<td><input type="submit" name="addStaff" id="addStaff"
				value="Add Staff" /></td>
			<%
				} else {
			%>
			Δεν υπάρχει διαθέσιμο προσωπικό
			</td>
			<td></td>
			<%
				}
			%>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td><input type="submit" name="createProject" id="createProject"
				value="Create Project" /></td>
			<td>&nbsp;</td>
		</tr>
	</table>
</form>
<%@ include file="include/error_end.jsp"%>
<%@ include file="include/bottom.jsp"%>
