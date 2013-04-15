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
					<option ${ publik == 'private' ? 'selected':'' } value="private">Ιδιωτικό</option>
					<option ${ publik == 'publik' ? 'selected':'' } value="publik">Δημόσιο</option>
			</select></td>
			<td class="error"><c:choose>
					<c:when test="${requestScope.emptyPublik != null}">Το πεδίο είναι υποχρεωτικό</c:when>
				</c:choose></td>
		</tr>
		<tr>
			<td>Υπεύθυνος Έργου</td>
			<td><c:choose>
					<c:when test="${not empty allManagers}">
						<c:set var="selectedMan"
							value="${not empty selectedManager ? selectedManager : allManagers[0]}"></c:set>
						<%-- <c:out value="${selectedMan}"></c:out> --%>
						<select name="manager" id="manager">
							<c:forEach var="man" items="${allManagers}">
								<option value="${man}" ${selectedMan == man ? 'selected':''}>
									<c:out value="${man}" />
								</option>
							</c:forEach>
						</select>
						<c:remove var="selectedMan" />
					</c:when>
					<c:otherwise>Δεν υπάρχουν διαθέσιμοι managers
					</c:otherwise>
				</c:choose></td>
			<td class="error"><c:choose>
					<c:when test="${requestScope.emptyManager != null}">Το πεδίο είναι υποχρεωτικό</c:when>
				</c:choose></td>
		</tr>
		<tr>
			<td valign="top">Προσωπικό</td>
			<td><c:choose>
					<c:when test="${empty addedStaff}">Δεν έχει προστεθεί προσωπικό</c:when>
					<c:otherwise>
						<table width="100%" border="1">
							<c:forEach var="staf" items="${addedStaff}">
								<tr>
									<td><input type="hidden" name="added" value="${staf}" />
										<c:out value="${staf}" /></td>
								</tr>
							</c:forEach>
						</table>
					</c:otherwise>
				</c:choose></td>
			<td class="error"><c:choose>
					<c:when test="${requestScope.emptyStaff != null}">Το πεδίο είναι υποχρεωτικό</c:when>
				</c:choose></td>
		</tr>
		<tr>
			<td></td>
			<c:choose>
				<c:when test="${empty allStaff}">
					<td>Δεν υπάρχει διαθέσιμο προσωπικό</td>
					<td></td>
				</c:when>
				<c:otherwise>
					<td><select name="staffMember" id="staffMember">
							<c:forEach var="staf" items="${allStaff}">
								<option value="${staf}">
									<c:out value="${staf}" />
								</option>
							</c:forEach>
					</select></td>
					<td><input type="submit" name="addStaff" id="addStaff"
						value="Add Staff" /></td>
				</c:otherwise>
			</c:choose>
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
