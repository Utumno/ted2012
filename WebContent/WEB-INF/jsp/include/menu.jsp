<%@page import="com.ted.domain.User"%>
<%@page import="com.ted.domain.User.*"%>
<% if(((User)request.getSession(false).getAttribute("signedUser")).getRole()==RolesENUM.ADMIN){ %>
	<%@ include file="admin_menu.jsp" %>
<%} else { %>
	<%@ include file="user_menu.jsp" %>
<% } %>