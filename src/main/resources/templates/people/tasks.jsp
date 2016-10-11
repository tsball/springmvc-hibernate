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
<h1>Person Tasks</h1>
	<a href="javascript:void(0);" data-href="${pageContext.request.contextPath}/people/${person.id}/tasks" data-method="POST">Start A Task</a>
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
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.11.0.min.js" ></script>
<script type="text/javascript">
$(function(){
	// Restful Delete
	$('a[data-method]').click(function(event){
		event.preventDefault();
		
		var url = $(this).attr('data-href');
		var http_verb = $(this).attr('data-method');
		$('<form action="'+ url +'" method="POST"><input type="hidden" name="_method" value="'+ http_verb +'" /></form>').submit();
	});
});
</script>
</html>
