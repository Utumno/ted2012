<%@page import="com.ted.domain.User"%>
<%@page import="com.ted.domain.User.*"%>
<%@ include file="tag_libs.jsp"%>
<c:choose>
	<c:when test="${sessionScope.signedUser.role == 'ADMIN'}">
		<%@ include file="admin_menu.jsp"%></c:when>
	<c:otherwise>
		<%@ include file="user_menu.jsp"%>
	</c:otherwise>
</c:choose>
