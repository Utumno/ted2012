<%@ page language="java" pageEncoding="UTF-8"%>
<tr>
	<td></td>
	<c:choose>
		<c:when test="${empty allStaff}">
			<td>Δεν υπάρχει προσωπικό για προσθήκη</td>
		</c:when>
		<c:otherwise>
			<td><select name="staffMember" id="staffMember">
					<c:forEach var="user" items="${allStaff}">
						<option value="${user}">
							<c:out value="${user}"></c:out>
						</option>
					</c:forEach>
			</select></td>
			<td><input type="submit" name="addStaff" id="addStaff"
				value="Προσθήκη Προσωπικού" /></td>
		</c:otherwise>
	</c:choose>
	<td>
		<!-- NEEDED ? -->
	</td>
</tr>
<tr>
	<td></td>
	<c:choose>
		<c:when test="${empty addedStaff}">
			<td>Δεν υπάρχει προσωπικό για διαγραφή</td>
		</c:when>
		<c:otherwise>
			<td><select name="deletedStaffMember" id="deletedStaffMember">
					<c:forEach var="user" items="${addedStaff}">
						<option value="${user}">${user}</option>
					</c:forEach>
			</select></td>
			<td><input type="submit" name="deleteStaff" id="deleteStaff"
				value="Διαγραφή Προσωπικού" /></td>
		</c:otherwise>
	</c:choose>
	<td>
		<!-- NEEDED ? -->
	</td>
</tr>