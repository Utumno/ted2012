<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="include/tag_libs.jsp"%>
<%@ include file="include/top.jsp"%>
<title>Στοιχεία Έργου</title>
<%@ include file="include/head.jsp"%>
<%@ include file="include/menu.jsp"%>
<h1>Στοιχεία Έργου</h1>
<%@ include file="include/error_begin.jsp"%>
<form id="adminProjectForm" name="adminProjectForm" method="post"
	action="${u:encodeURI('project?name=',name)}">
	<table style="width: 379">
		<tr>
			<td width="185">Όνομα</td>
			<td width="178"><c:out value="${name}"></c:out></td>
		</tr>
		<tr>
			<td>Περιγραφή</td>
			<c:choose>
				<c:when test="${admin}">
					<td><input type="text" name="description" id="description"
						value="<c:out value="${description}"></c:out>" /></td>
					<td class="error"><c:choose>
							<c:when test="${requestScope.emptyDescription != null}">Το πεδίο είναι υποχρεωτικό</c:when>
						</c:choose></td>
				</c:when>
				<c:otherwise>
					<td width="178"><c:out value="${description}"></c:out></td>
				</c:otherwise>
			</c:choose>
		</tr>
		<tr>
			<td>Δημόσιο/Ιδιωτικό</td>
			<c:choose>
				<c:when test="${admin }">
					<td><label for="publik"></label> <select name="publik"
						id="publik">
							<%
								String publik = (String) request.getAttribute("publik");
							%>
							<option <%if (publik.equals("private")) {%> selected="selected"
								<%}%> value="private">Ιδιωτικό</option>
							<option <%if (publik.equals("publik")) {%> selected="selected"
								<%}%> value="publik">Δημόσιο</option>
					</select></td>
					<td class="error"><c:choose>
							<c:when test="${requestScope.emptyPublik != null}">Το πεδίο είναι υποχρεωτικό</c:when>
						</c:choose></td>
				</c:when>
				<c:otherwise>
					<td width="178"><c:if
							test="${requestScope.publik == 'private'}">Ιδιωτικό</c:if> <c:if
							test="${requestScope.publik == 'publik'}">Δημόσιο</c:if></td>
				</c:otherwise>
			</c:choose>
		</tr>
		<tr>
			<td>Υπεύθυνος Έργου</td>
			<td><c:choose>
					<c:when test="${admin}">
						<c:choose>
							<c:when test="${empty allManagers}">Δεν υπάρχουν διαθέσιμοι managers</c:when>
							<c:otherwise>
								<select name="manager" id="manager">
									<c:forEach var="user" items="${allManagers}" varStatus="i">
										<c:choose>
											<c:when
												test="${(empty requestScope.selectedManager) && (i.count == 0)}">
												<option selected="selected" value="${user}">
													<c:out value="${user}"></c:out>
												</option>
											</c:when>
											<c:when
												test="${!(empty requestScope.selectedManager) && (requestScope.selectedManager eq user)}">
												<option selected="selected" value="${user}">
													<c:out value="${user}"></c:out>
												</option>
											</c:when>
											<c:otherwise>
												<option>
													<c:out value="${user}"></c:out>
												</option>
											</c:otherwise>
										</c:choose>
									</c:forEach>
								</select>
							</c:otherwise>
						</c:choose>
					</c:when>
					<c:otherwise>
						<c:out value="${selectedManager}"></c:out>
					</c:otherwise>
				</c:choose></td>
			<td class="error"><c:if
					test="${requestScope.emptyManager != null}">Το πεδίο είναι υποχρεωτικό</c:if>
			</td>
		</tr>
		<%@ include file="include/staff.jsp"%>
		<c:if test="${admin}">
			<%@ include file="include/add_remove_staff.jsp"%>
			<tr>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td><input type="submit" name="updateProject"
					id="updateProject" value="Ενημέρωση Έργου" /></td>
				<td>&nbsp;</td>					
			</tr>
		</c:if>
	</table>
	<c:if test="${admin}">
		<p align="center">
			<a href="${u:encodeURI('deleteproject?name=',name)}">Διαγραφή
				Έργου</a>
		</p>
	</c:if>
</form>
<h3>Εργασίες</h3>

<c:choose>
	<c:when test="${empty jobs}">Δεν υπάρχουν εργασίες για το έργο αυτό</c:when>
	<c:otherwise>
		<form action="deletejob"
			method="post" name="deleteJob">
			<table style="width: 200">
				<tr>
					<th class="tables" scope="col">A/A</th>
					<th class="tables" scope="col">Όνομα</th>
					<th class="tables" scope="col">Περιγραφή</th>
					<th class="tables" scope="col">Κατάσταση</th>
					<c:if test="${projectManager}">
						<td class="hidden" scope="col"><input name="deleteJobsButton"
							type="submit" value="Διαγραφή" /><input type="hidden" value="${name}" name="project" /></td>
					</c:if>
				</tr>
				<c:forEach var="job" items="${jobs}" varStatus="i">
					<tr>
						<td class="tables"><div align="center">${i.count}</div></td>
						<td class="tables"><a href="job?id=${job.id}"><c:out
									value="${job.name}"></c:out></a></td>
						<%-- <c:set var="jobDescr" value="${job.description}" /> --%>
						<td class="tables"><textarea name="description" cols="30"
								rows="${(fn:length(job.description) / 46) + 1}"
								readonly="readonly" id="description" style="border: none"><c:out value="${job.description}"></c:out></textarea></td>
						<td class="tables"><c:out value="${job.state.toString()}"></c:out></td>
						<c:if test="${projectManager}">
							<td class="hidden"><input name="deleteJobId" type="checkbox"
								value="${job.id}" /></td>
						</c:if>
					</tr>
				</c:forEach>
			</table>
		</form>
	</c:otherwise>
</c:choose>
<c:if test="${projectManager}">
	<div align="center">
	<form id="createJobForm" name="createJobForm" method="post"
		action="createjob">
		<input type="hidden" value="${name}" name="project" /> <input
			type="submit" name="gotoCreateJob" id="gotoCreateJob"
			value="Δημιουργία Εργασίας" />
	</form>
	</div>
</c:if>
<%@ include file="include/error_end.jsp"%>
<%@ include file="include/bottom.jsp"%>
