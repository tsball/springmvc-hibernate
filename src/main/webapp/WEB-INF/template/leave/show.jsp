<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Leave Management</title>

</head>
<body>
	<h1>Leave</h1>
	<div>
		<label>Leave Type</label>
		${leave.leaveType}
	</div>
	<div>
		<label>Created At</label>
		${leave.createdAt}
	</div>
	<div>
		<label>Status</label>
		${processInstance}
	</div>
</body>
</html>
