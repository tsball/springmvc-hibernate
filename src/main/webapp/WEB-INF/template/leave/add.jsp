<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Leave Management</title>

</head>
<body>
	<h1>Leave Apply</h1>
	<form:form method="POST" commandName="leave" action="${pageContext.request.contextPath}/leave/">
		<form:errors path="*" cssClass="errorblock" element="div" />
		<div>
			<label>请假类型：</label>
			<select name="leaveType">
				<option value="0">公休</option>
				<option value="1">病假</option>
				<option value="2">调休</option>
				<option value="3">事假</option>
				<option value="4">婚假</option>
			</select>
		</div>
		<div>
			<label>开始时间：</label>
			<input type="text" name="beginTime" />
		</div>
		<div>
			<label>结束时间：</label>
			<input type="text" name="endTime" />
		</div>
		<div>
			<label>请假原因：</label>
			<textarea name="reason"></textarea>
		</div>
		<div>
			<button type="submit">提交</button>
		</div>
	</form:form>
</body>
</html>
