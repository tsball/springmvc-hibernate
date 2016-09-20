<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Person Management</title>

</head>
<body>
	<h1>Edit Person</h1>
	<div>${alert}</div>
	<form:form modelAttribute="user" action="${pageContext.request.contextPath}/person/${user.id}" method="PUT">
		<div>
			<form:label path="username">Username</form:label>
			<form:input path="username" />
		</div>
		<div>
			<form:label path="firstName">First Name</form:label>
			<form:input path="firstName" />
		</div>
		<div>
			<form:label path="lastName">Last Name</form:label>
			<form:input path="lastName" />
		</div>
		<div>
			<button type="submit">提交</button>
		</div>
	</form:form>
</body>
</html>
