<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Person List</title>

</head>
<body>
<h1>Person Management</h1>
	<a href="${pageContext.request.contextPath}/people/add">New Person</a>
	<div>${notice}</div>
	<table border="1">
		<thead>
			<tr>
				<th width="200">Username</th>
				<th width="200">First Nmae</th>
				<th width="200">Last Name</th>
				<th></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${people}" var="obj">
			<tr>
				<td><a href="${pageContext.request.contextPath}/people/${obj.id}">${obj.username}</a></td>
				<td>${obj.firstName}</td>
				<td>${obj.lastName}</td>
				<td>
					<a href="${pageContext.request.contextPath}/leaves">View Leave</a>
					<a href="${pageContext.request.contextPath}/people/${obj.id}/tasks">View Tasks</a>
					<a href="${pageContext.request.contextPath}/people/${obj.id}/edit">Edit</a>
					<a href="javascript:void(0);" data-href="${pageContext.request.contextPath}/people/${obj.id}" data-method="DELETE">Delete</a>
				</td>
			</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.11.0.min.js" ></script>
<script type="text/javascript">
$(function(){
	// Restful Delete
	$('a[data-method]').click(function(event){
		event.preventDefault();
		
		var url = $(this).attr('data-href');
		var http_verb = $(this).attr('data-method');
		var csrf = '<input type="hidden" name="_csrf" value="${_csrf.token}"/>';
		$('<form action="'+ url +'" method="POST"><input type="hidden" name="_method" value="'+ http_verb +'" />'+ csrf +'</form>').submit();
	});
});
</script>
</html>
