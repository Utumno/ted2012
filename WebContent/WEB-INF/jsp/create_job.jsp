<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="include/tag_libs.jsp"%>
<%@ include file="include/top.jsp"%>
<title>Νέα Εργασία</title>
<%@ include file="include/head.jsp"%>
<%@ include file="include/user_menu.jsp"%>
<h1>Δημιουργία Νέας Εργασίας</h1>

<%@ include file="include/error_begin.jsp"%>
<form id="createJobForm" name="createJobForm" method="post"
	action="createjob">
	<table style="width: 590">
		<tr>
			<td width="170"><input type="hidden" name="project"
				value="${project}" /> Όνομα</td>
			<td width="170"><input type="text" name="name" id="name"
				<c:if test="${requestScope.name != null}">
							value="${requestScope.name}"</c:if> /></td>
			<td class="error" width="250"><c:choose>
					<c:when test="${requestScope.emptyName != null}">Το πεδίο είναι υποχρεωτικό</c:when>
					<c:when test="${requestScope.jobNameExists != null}">Υπάρχει ήδη εργασία με αυτό το όνομα</c:when>
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
			<td>Ημερομηνία έναρξης</td>
			<td><input type="text" name="startDate" id="startDate"
				<c:if test="${requestScope.startDate != null}">	value="${requestScope.startDate}"</c:if> /></td>
			<td class="error"><c:choose>
					<c:when test="${requestScope.emptyStartDate != null}">Το πεδίο είναι υποχρεωτικό</c:when>
					<c:when test="${requestScope.malformedStartDate != null}">Η ημερομηνία πρέπει να έχει την μορφή yyyy/mm/dd</c:when>
				</c:choose></td>
		</tr>
		<tr>
			<td>Ημερομηνία λήξης</td>
			<td><input type="text" name="endDate" id="endDate"
				<c:if test="${requestScope.endDate != null}">	value="${requestScope.endDate}"</c:if> /></td>
			<td class="error"><c:choose>
					<c:when test="${requestScope.emptyEndDate!= null}">Το πεδίο είναι υποχρεωτικό</c:when>
					<c:when test="${requestScope.malformedEndDate != null}">Η ημερομηνία πρέπει να έχει την μορφή yyyy/mm/dd</c:when>
					<c:when test="${requestScope.endDateBeforeStartDate != null}">Η ημερομηνία λήξης πρέπει να είναι μετά την ημερομηνία έναρξης</c:when>
				</c:choose></td>
		</tr>
		<%@ include file="include/staff.jsp"%>
		<%@ include file="include/add_remove_staff.jsp"%>
		<tr>
			<td>&nbsp;</td>
			<td><input type="submit" name="createJob" id="createJob"
				value="Δημιουργία Έργασίας" /></td>
			<td>&nbsp;</td>
		</tr>
	</table>
</form>
<%@ include file="include/error_end.jsp"%>
<%@ include file="include/bottom.jsp"%>
