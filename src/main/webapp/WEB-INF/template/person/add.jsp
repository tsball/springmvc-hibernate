<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Person Management</title>

</head>
<body>
	<h1>Add Person</h1>
	<form:form method="POST" commandName="person" action="${pageContext.request.contextPath}/person/">
		<form:errors path="*" cssClass="errorblock" element="div" />
		<div>
			<label>Username</label>
			<form:input path="username" />
		</div>
		<div>
			<label>First Name</label>
			<input type="text" name="firstName" />
		</div>
		<div>
			<label>Last Name</label>
			<input type="text" name="lastName" />
		</div>
		<div>
			<button type="submit">提交</button>
		</div>
	</form:form>
</body>
</html>
