<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="include/tag_libs.jsp"%>
<%@ include file="include/top.jsp"%>
<title>Σελίδα Εργασίας</title>
<%@ include file="include/head.jsp"%>
<%@ include file="include/menu.jsp"%>
<h1>Εργασία</h1>
<%@ include file="include/error_begin.jsp"%>
<form id="jobForm" name="jobForm" method="post" action="job?id=${id}">
	<table style="width: 379">
		<tr>
			<td width="215">Όνομα</td>
			<td width="178">${name}</td><td></td>
		</tr>
		<tr>
			<td>Περιγραφή</td>
			<td class="tables" width="241"><textarea name="description"
					cols="30" rows="${description.length()/46 + 1}"
					<c:if test="${!projectManager}">readonly="readonly"</c:if> id="description" style="border: none"><c:if test="${requestScope.description != null}">${requestScope.description}</c:if></textarea>
			</td>
			<td class="error"><c:if test="${requestScope.emptyDescription != null}">Το πεδίο είναι υποχρεωτικό</c:if></td>			
		</tr>
		<tr>
			<td>Ημερομηνία έναρξης</td>
			<td><input type="text" name="startDate"
				<c:if test="${!projectManager}">readonly="readonly"</c:if>
				id="startDate"
				<c:if test="${requestScope.startDate != null}">	value="${requestScope.startDate}"</c:if> /></td>
			<td class="error"><c:choose>
					<c:when test="${requestScope.emptyStartDate != null}">Το πεδίο είναι υποχρεωτικό</c:when>
					<c:when test="${requestScope.malformedStartDate != null}">Η ημερομηνία πρέπει να έχει την μορφή yyyy/mm/dd</c:when>
				</c:choose></td>
		</tr>
		<tr>
			<td>Ημερομηνία λήξης</td>
			<td><input type="text" name="endDate"
				<c:if test="${!projectManager}">readonly="readonly"</c:if>
				id="endDate"
				<c:if test="${requestScope.endDate != null}">	value="${requestScope.endDate}"</c:if> /></td>
			<td class="error"><c:choose>
					<c:when test="${requestScope.emptyEndDate!= null}">Το πεδίο είναι υποχρεωτικό</c:when>
					<c:when test="${requestScope.malformedEndDate != null}">Η ημερομηνία πρέπει να έχει την μορφή yyyy/mm/dd</c:when>
					<c:when test="${requestScope.endDateBeforeStartDate != null}">Η ημερομηνία λήξης πρέπει να είναι μετά την ημερομηνία έναρξης</c:when>
				</c:choose></td>
		</tr>
		<%@ include file="include/staff.jsp"%>
		<c:if test="${projectManager}">
			<%@ include file="include/add_remove_staff.jsp"%>
		</c:if>
		<tr>
			<td>Κατάσταση</td>
			<td><c:choose>
					<c:when test="${!relativeStaff and !projectManager}">
						<c:if test="${state eq 'NEW'}">Νέα </c:if>
						<c:if test="${state eq 'STARTED'}">Εκτελείται</c:if>
						<c:if test="${state eq 'DONE'}">Ολοκληρωμένη</c:if>
					</c:when>
					<c:otherwise>
						<select name="state" size="1">
							<option
								<c:if test="${state eq 'NEW'}"> selected="selected"</c:if>
								value="new">Νέα</option>
							<option
								<c:if test="${state eq 'STARTED'}"> selected="selected"</c:if>
								value="started">Εκτελείται</option>
							<option
								<c:if test="${state eq 'DONE'}"> selected="selected"</c:if>
								value="done">Ολοκληρωμένη</option>
						</select>
					</c:otherwise>
				</c:choose></td>
		</tr>
	</table>
	<p>&nbsp;</p>
	<div>
		<c:if test="${relativeStaff}">
			<div align="center">
				<input type="submit" name="updateJobState" id="updateJobState"
					value="Ενημέρωση" />
			</div>
		</c:if>
		<c:if test="${projectManager}">
			<div align="center">
				<input type="submit" name="updateJob" id="updateJob"
					value="Ενημέρωση" />
			</div>
		</c:if>
		<p>&nbsp;</p>
		<c:if test="${projectManager}">
			<div align="left">
				<a href="deletejob?id=${id}">Διαγραφή Εργασίας</a>
			</div>
		</c:if>
	</div>
</form>
<p>&nbsp;</p>
<hr />
<h3>Σχόλια Εργασίας</h3>
<c:choose>
	<c:when test="${empty jobComments}">Δεν υπάρχουν σχόλια γι αυτή την εργασία</c:when>
	<c:otherwise>
		<table style="width: 506">
			<tr>
				<th width="50" class="tables" scope="col">Α/Α</th>
				<th width="127" class="tables" scope="col">Χρήστης</th>
				<th width="241" class="tables" scope="col">Σχόλιο</th>
			</tr>
			<c:forEach var="comment" items="${jobComments}" varStatus="i">
				<tr>
					<td class="comments"><div align="center">${i.count}</div></td>
					<td class="comments"><a
						href="${u:encodeURI('profile?user=',comment.commenter.username)}"><c:if
								test="${guest or staff}"></a> </c:if>${comment.getCommenter().getUsername()}<c:if
							test="${!guest and !staff}">
							</a>
						</c:if></td>
					<td class="tables" width="241"><textarea name="comment"
							cols="30" rows="${comment.getComment().length()/46 + 1}"
							readonly="readonly" id="comment" style="border: none">${comment.getComment()}</textarea>
					</td>
				</tr>
			</c:forEach>
		</table>
	</c:otherwise>
</c:choose>
<c:if test="${projectManager or relativeStaff}">
	<p>
	<h4>Προσθήκη Σχολίου</h4>
	</p>
	<form action="job?id=${id}" method="post" name="addcomment">
		<p>
			<textarea name="comment" cols="50" rows="8"></textarea>
		</p>
		<table style="width: 416">
			<tr>
				<td width="281">&nbsp;</td>
				<td width="125"><input type="submit" name="submitcomment"
					id="submitcomment" value="Προσθήκη" /></td>
			</tr>
		</table>
		<p>&nbsp;</p>
	</form>
</c:if>
<%@ include file="include/error_end.jsp"%>
<%@ include file="include/bottom.jsp"%>
