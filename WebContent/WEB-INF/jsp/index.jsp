<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="include/top_session_false.jsp"%>
<title>Αρχική</title>
<%@ include file="include/head.jsp"%>
<%@ include file="include/no_menu.jsp"%>
<h1 style="position: relative; left: 120px">Καλώς ήρθες στην
	εφαρμογή ted2012</h1>
<p>&nbsp;</p>
<div class="centered">
	<div class="error">
		<c:if test="${requestScope.LoginFailed != null}">
			<c:out value="${requestScope.LoginFailed}"></c:out>
		</c:if>
	</div>
	<form id="login_form" name="login_form" method="post"
		action="/ted2012/login">
		<table style="width: 541">
			<tr>
				<td width="88">Username</td>
				<td width="144"><input type="text" name="username"
					id="username"
					<c:if test="${requestScope.username != null}">
							value="${requestScope.username}"</c:if> /></td>
				<td class="error" width="295">
					<div id="errorUsername">
						<c:choose>
							<c:when test="${requestScope.emptyUsername != null}">Συμπληρώστε Όνομα χρήστη</c:when>
						</c:choose>
					</div>
				</td>
			</tr>
			<tr>
				<td>Password</td>
				<td><input type="password" name="password" id="password" /></td>
				<td class="error">
					<div id="errorPassword">
						<c:choose>
							<c:when test="${requestScope.emptyPassword != null}">Συμπληρώστε Password</c:when>
						</c:choose>
					</div>
				</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td><input type="submit" name="login_button" id="login_button"
					value="Είσοδος" /></td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td><a href="/ted2012/register">Κάνε εγγραφή</a></td>
			</tr>
		</table>
		<p>&nbsp;</p>
		<p>&nbsp;</p>
		<p>&nbsp;</p>
		 <p>&nbsp;</p>
	</form>
	<p>&nbsp;</p>
</div>
<%@ include file="include/bottom.jsp"%>
