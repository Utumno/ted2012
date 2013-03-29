<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="include/top.jsp"%>
<%@ include file="include/tag_libs.jsp"%>
<title>Είστε σίγουρος ;</title>
<%@ include file="include/head.jsp"%>
<%@ include file="include/menu.jsp"%>
<h1>Είστε σίγουρος ;</h1>
<%@ include file="include/error_begin.jsp"%>
<c:out value="${confirmationMessage}"></c:out>
<form id="confirmDeletionForm" name="confirmDeletionForm" method="post"
	action="${redirectUrlIfUserDecidesTODelete}">
	<c:forEach var="object" items="${arrayOfObjectsToDelete}">
		<input type="hidden" name="arrayOfKeysOfObjectsToBeDeleted"
			value="${object.id}" />
		<c:out value="${object.name} "></c:out>
	</c:forEach>
	 ;
	<p>
		<input type="submit" name="yesDelete" id="yesDelete" value="Διαγραφή" />
	</p>
</form>
<form id="dontDeleteForm" name="dontDeleteForm" method="GET"
	action='<c:out value="${servlet}"></c:out>'>
	<p>
	<input type="hidden" name="name" id="dontDelete" value='<c:out value="${redirectUrlIfUserDecidesNOTToDelete}"></c:out>' />
		<input type="submit" name="dontDeleteSubmit" id="dontDelete" value='<c:out value="Άκυρο"></c:out>' />
	</p>
</form>
<%@ include file="include/error_end.jsp"%>
<%@ include file="include/bottom.jsp"%>
