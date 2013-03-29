<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="include/top.jsp"%>
<title>Εγγραφή Χρήστη</title>
<%@ include file="include/head.jsp"%>
<%@ include file="include/no_menu.jsp"%>
<div class="centered">
	<h1>Εγγραφή Χρήστη</h1>
	<form id="registration_form" name="registration_form" method="post"
		action="register">
		<table style="width: 596">
			<tr>
				<td width="185">Username</td>
				<td width="178"><input type="text" name="username"
					id="username"
					<c:if test="${requestScope.username != null}">
							value="${requestScope.username}"</c:if> /></td>
				<td class="error" width="332">
					<div id="errorUsername">
						<c:choose>
							<c:when test="${requestScope.duplicateUsername != null}">Το επιλεγμένο username υπάρχει ήδη</c:when>
							<c:when test="${requestScope.emptyUsername != null}">Συμπληρώστε το Όνομα Χρήστη</c:when>
							<c:when test="${requestScope.smallUsername != null}">Ελάχιστος αριθμός χαρακτήρων: 5</c:when>
						</c:choose>
					</div>
				</td>
			</tr>
			<tr>
				<td>Password</td>
				<td><input type="password" name="password1" id="password1"
					<c:if test="${requestScope.password1 != null}">
							value="${requestScope.password1}"</c:if> /></td>
				<td class="error">
					<div id="errorPassword1">
						<c:choose>
							<c:when test="${requestScope.emptyPassword1 != null}">Συμπληρώστε Password</c:when>
							<c:when test="${requestScope.smallPassword1 != null}">Ελάχιστος αριθμός χαρακτήρων: 6</c:when>
							<c:when test="${requestScope.diffPasswords != null}">Τα 2 passwords πρέπει να είναι ίδια</c:when>
						</c:choose>
					</div>
				</td>
			</tr>
			<tr>
				<td>Confirm Password</td>
				<td><input type="password" name="password2" id="password2"
					<c:if test="${requestScope.password2 != null}">
							value="${requestScope.password2}"</c:if> /></td>
				<td class="error">
					<div id="errorPassword2">
						<c:choose>
							<c:when test="${requestScope.emptyPassword2 != null}">Επανάλαβετε το Password</c:when>
							<c:when test="${requestScope.smallPassword2 != null}">Ελάχιστος αριθμός χαρακτήρων: 6</c:when>
							<c:when test="${requestScope.diffPasswords != null}">Τα 2 passwords πρέπει να είναι ίδια</c:when>
						</c:choose>
					</div>
				</td>
			</tr>
			<tr>
				<td>Όνομα</td>
				<td><input type="text" name="name" id="name"
					<c:if test="${requestScope.name != null}">
							value="${requestScope.name}"</c:if> /></td>
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
					<c:if test="${requestScope.surname != null}">
							value="${requestScope.surname}"</c:if> /></td>
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
					<c:if test="${requestScope.email != null}">
							value="${requestScope.email}"</c:if> /></td>
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
				<td>Επισκέπτης</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td><input type="submit" name="register_button"
					id="register_button" value="Εγγραφή" /></td>
			</tr>
		</table>
		<p>&nbsp;</p>
	</form>
</div>
<%@ include file="include/bottom.jsp"%>
