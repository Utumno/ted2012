<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="include/tag_libs.jsp"%>
<%@ include file="include/top.jsp"%>
<%@page import="java.util.ArrayList"%>
<title>Σελίδα απλού Χρήστη</title>
<%@ include file="include/head.jsp"%>
<%@ include file="include/user_menu.jsp"%>
<div style="float: left; width: 300px; border-right: 1px solid;">
	<h1>Τα έργα μου</h1>
	<h3>Έργα</h3>
	<c:choose>
		<c:when test="${empty userProjects}">Δεν υπάρχουν Διαθέσιμα Έργα</c:when>
		<c:otherwise>
			<table style="width: 200">
				<tr>
					<th class="tables" scope="col">A/A</th>
					<th class="tables" scope="col">Όνομα</th>
				</tr>
				<c:forEach var="project" items="${userProjects}" varStatus="i">
					<tr>
						<td class="tables"><div align="center">${i.count}</div></td>
						<td class="tables"><a href="${u:encodeURI('project?name=',project)}"><c:out value="${project}"/></a></td>
					</tr>
				</c:forEach>
			</table>
		</c:otherwise>
	</c:choose>
</div>
<div style="float: left; width: 300px; margin: 0 50px;">
	<h1>Δημόσια Έργα</h1>
	<h3>Έργα</h3>
	<c:choose>
		<c:when test="${empty publicProjects}">Δεν υπάρχουν Δημόσια Έργα</c:when>
		<c:otherwise>
			<table style="width: 200">
				<tr>
					<th class="tables" scope="col">A/A</th>
					<th class="tables" scope="col">Όνομα</th>
				</tr>
				<c:forEach var="project" items="${publicProjects}" varStatus="i">
					<tr>
						<td class="tables"><div align="center">${i.count}</div></td>
						<td class="tables"><a href="${u:encodeURI('project?name=',project)}"><c:out value="${project}"/></a></td>
					</tr>
				</c:forEach>
			</table>
		</c:otherwise>
	</c:choose>
</div>
<p style="clear: both;">&nbsp;</p>

<c:if test="${staff}">
	<hr />
	<h3>Μη ολοκληρωμένες εργασίες</h3>
	<c:choose>
		<c:when test="${empty userJobs}">Δεν έχετε μη ολοκληρωμένες εργασίες</c:when>
		<c:otherwise>
			<c:forEach var="projectName" items="${userJobs.keySet()}">
				<c:out value="Έργο : ${projectName}"></c:out>
				<table style="width: 200">
					<tr>
						<th class="tables" scope="col">A/A</th>
						<th class="tables" scope="col">Όνομα</th>
						<th class="tables" scope="col">Περιγραφή</th>
						<th class="tables" scope="col">Κατάσταση</th>
					</tr>
					<c:forEach var="job" items="${userJobs[projectName]}" varStatus="i">
						<tr
							<c:if test="${job.getState().toString() eq 'NEW'}">class="newjob"</c:if>>
							<td class="tables"><div align="center">${i.count}</div></td>
							<td class="tables"><a href="job?id=${job.id}"><c:out
										value="${job.name}"></c:out></a></td>
							<td class="tables"><textarea name="description" cols="30"
									rows="${(fn:length(job.description) / 46) + 1}"
									readonly="readonly" id="description" style="border: none"><c:out value="${job.description}"></c:out></textarea></td>
							<td class="tables"><c:out value="${job.state.toString()}"></c:out></td>
						</tr>
					</c:forEach>
				</table>
				<br/>
			</c:forEach>
		</c:otherwise>
	</c:choose>
</c:if>
<%@ include file="include/bottom.jsp"%>
