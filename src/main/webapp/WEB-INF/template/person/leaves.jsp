<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Leave List</title>

</head>
<body>
<h1>Person Leaves</h1>
	<a href="${pageContext.request.contextPath}/leave/add">Apply a leave</a>
	<div>${notice}</div>
	<table border="1">
		<thead>
			<tr>
				<th width="200">Task Id</th>
				<th width="200">Task Name</th>
				<th></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${taskRepresentations}" var="obj">
			<tr>
				<td>${obj.id}</td>
				<td>${obj.name}</td>
				<td>
				</td>
			</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>
