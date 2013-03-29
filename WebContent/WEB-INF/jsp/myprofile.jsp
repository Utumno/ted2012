<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="include/top.jsp"%>
<%@ include file="include/tag_libs.jsp"%>
<title>Προφιλ</title>
<%@ include file="include/head.jsp"%>
<%@ include file="include/menu.jsp"%>
<h1>Προφιλ</h1>
<%@ include file="include/error_begin.jsp"%>
<form id="myProfileForm" name="myProfileForm" method="post"
	action="profile">
	<table style="width: 596">
		<tr>
			<td width="185">Username</td>
			<td width="178"><c:out value="${username}" /></td>
		</tr>
		<tr>
			<td>Όνομα</td>
			<td><input type="text" name="name" id="name"
				value="<c:out value="${name}" />" /></td>
			<td class="error">
				<div id="errorName">
					<c:choose>
						<c:when test="${requestScope.emptyName != null}">Συμπληρώστε Όνομα</c:when>
					</c:choose>
				</div>
			</td>
		</tr>
		<tr>
			<td>Επίθετο</td>
			<td><input type="text" name="surname" id="surname"
				value="<c:out value="${surname}" />" /></td>
			<td class="error">
				<div id="errorSurname">
					<c:choose>
						<c:when test="${requestScope.emptySurname != null}">Συμπληρώστε Επίθετο</c:when>
					</c:choose>
				</div>
			</td>
		</tr>
		<tr>
			<td>Email</td>
			<td><input type="text" name="email" id="email"
				value="<c:out value="${email}" />" /></td>
			<td class="error">
				<div id="errorEmail">
					<c:choose>
						<c:when test="${requestScope.emptyEmail != null}">Συμπληρώστε Email</c:when>
						<c:when test="${requestScope.invalidEmail != null}">Λάθος μορφή email</c:when>
					</c:choose>
				</div>
			</td>
		</tr>
		<tr>
			<td>Ρόλος</td>
			<td><c:choose>
					<c:when test="${requestScope.role == 'GUEST'}">Επισκέπτης</c:when>
					<c:when test="${requestScope.role == 'STAFF'}">Προσωπικό</c:when>
					<c:when test="${requestScope.role == 'MANAGER'}">Υπεύθυνος Έργου</c:when>
					<c:when test="${requestScope.role == 'ADMIN'}">Admin</c:when>
				</c:choose>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td><input type="submit" name="updateMyProfile"
				id="updateMyProfile" value="Ενημέρωση" /></td>
		</tr>
	</table>
	<p>&nbsp;</p>
</form>
<%@ include file="include/error_end.jsp"%>
<%@ include file="include/bottom.jsp"%>
