<%@ include file="tag_libs.jsp"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<tr>
	<td valign="top">Προσωπικό</td>
	<td><c:choose>
			<c:when test="${empty requestScope.addedStaff}">Δεν έχει προστεθεί προσωπικό</c:when>
			<c:otherwise>
				<table style="width: 100%" border="1">
					<c:forEach var="user" items="${addedStaff}" varStatus="i">
						<tr>
							<td><input type="hidden" name="added" value="${user}" /><a
								href="${u:encodeURI('profile?user=',user)}"><c:if test="${guest or staff}"></a>
								</c:if>
								<c:out value="${user}"></c:out> <c:if
									test="${!guest and !staff}">
									</a>
								</c:if></td>
						</tr>
					</c:forEach>
				</table>
			</c:otherwise>
		</c:choose></td>
	<td class="error"><c:if test="${requestScope.emptyStaff != null}">Το πεδίο είναι υποχρεωτικό</c:if>
	</td>
</tr>