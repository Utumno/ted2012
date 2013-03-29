<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="include/top.jsp"%>
<%@ include file="include/tag_libs.jsp"%>
<title>Στοιχεία Χρήστη</title>
<%@ include file="include/head.jsp"%>
<%@ include file="include/menu.jsp"%>
<h1>Στοιχεία Χρήστη</h1>
<%@ include file="include/error_begin.jsp"%>
<form id="otherUserProfileForm" name="otherUserProfileForm"
	method="post" action="profile">
	<table style="width: 420">
		<tr>
			<td width="155">Username</td>
			<td width="255"><input type="hidden" name="user"
				value="${userToShow.username}" /><c:out
					value="${userToShow.username}" /></td>
		</tr>
		<tr>
			<td>Όνομα</td>
			<td><c:out value="${userToShow.name}" /></td>
		</tr>
		<tr>
			<td>Επίθετο</td>
			<td><c:out value="${userToShow.surname}" /></td>
		</tr>
		<tr>
			<td>Email</td>
			<td><c:out value="${userToShow.email}" /></td>
		</tr>
		<tr>
			<td>Ρόλος</td>
			<c:choose>
				<c:when test="${requestScope.notAdmin != null}">
					<td><c:choose>
							<c:when test="${userToShow.role == 'GUEST'}">Επισκέπτης</c:when>
							<c:when test="${userToShow.role == 'STAFF'}">Προσωπικό</c:when>
							<c:when test="${userToShow.role == 'MANAGER'}">Υπεύθυνος Έργου</c:when>
							<c:when test="${userToShow.role == 'ADMIN'}">Admin</c:when>
						</c:choose></td>
				</c:when>
				<c:otherwise>
					<c:choose>
						<c:when test="${requestScope.cannotUpdateRole != null}">
							<td>Ο χρήστης συμμετέχει σε projects ως <c:out
									value="${userToShow.role == 'MANAGER' ? 'υπεύθυνος.' : 'προσωπικό.'}" />
							</td>
						</c:when>
						<c:otherwise>
							<td><select name="role" size="1">
									<option
										<c:out value="${userToShow.role == 'GUEST' ? 'selected' : ''}"/>
										value="Guest">Επισκέπτης</option>
									<option
										<c:out value="${userToShow.role == 'STAFF' ? 'selected' : ''}"/>
										value="Staff">Προσωπικό</option>
									<option
										<c:out value="${userToShow.role == 'MANAGER' ? 'selected' : ''}"/>
										value="Manager">Υπεύθυνος Έργου</option>
							</select></td>
						</c:otherwise>
					</c:choose>
				</c:otherwise>
			</c:choose>
		</tr>
		<c:if test="${admin}">
			<tr>
				<td>&nbsp;</td>
				<c:choose>
					<c:when test="${requestScope.cannotUpdateRole != null}">
						<td><input type="submit" name="updateProfile"
							id="updateProfile" value="Ενημέρωση" disabled="disabled" /></td>
					</c:when>
					<c:otherwise>
						<td><input type="submit" name="updateProfile"
							id="updateProfile" value="Ενημέρωση" /></td>
					</c:otherwise>
				</c:choose>
			</tr>
		</c:if>
	</table>
	<p>&nbsp;</p>
</form>
<c:if test="${admin}">
	<p align="center">
		<a href="${u:encodeURI('deleteuser?user=',userToShow.username)}">Διαγραφή
			Χρήστη</a>
	</p>
</c:if>
<%@ include file="include/error_end.jsp"%>
<%@ include file="include/bottom.jsp"%>
