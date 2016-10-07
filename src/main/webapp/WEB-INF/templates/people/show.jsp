<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>User List</title>

</head>
<body>
	<h1>Person</h1>
	<div>
		<label>Username</label>
		${person.username}
	</div>
	<div>
		<label>First Name</label>
		${person.firstName}
	</div>
	<div>
		<label>Last Name</label>
		${person.lastName}
	</div>
</body>
</html>