<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Person Management</title>

    <link href="${contextPath}/css/bootstrap.min.css" rel="stylesheet">
    <link href="${contextPath}/css/common.css" rel="stylesheet">

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body>
	<h1>Edit Person</h1>
	<div>${alert}</div>
	<form:form modelAttribute="person" action="${pageContext.request.contextPath}/person/${person.id}" method="PUT">
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
			<button id="btn-submit" type="button">提交</button>
		</div>
	</form:form>
	<!-- /container -->
	<script type="text/javascript" src="${contextPath}/js/jquery-1.11.0.min.js" ></script>
	<script type="text/javascript" src="${contextPath}/js/jquery.form.min.js" ></script>
	<script type="text/javascript" src="${contextPath}/js/bootstrap.min.js" ></script>
</body>
<script type="text/javascript">
$(function(){
	$('#btn-submit').click(function(){
		var btnSubmitText = $(this).text();
		
		$("#person").ajaxSubmit({
          beforeSubmit: function() {
            // disabled the button
            $('#btn-submit').prop('disabled', true);
            $('#btn-submit').append("中...");
          },
          success: function (response) {
        	  location.href = '/person';
          },
          error: function(xhr, status, error) {
        	  //status: error
        	  //error: Not Acceptable
        	  alert(xhr.status);
        	  
              // re-enable the button
              $('#btn-submit').prop('disabled', false);
              $('#btn-submit').html(btnSubmitText);
          }
        });
	});
});
</script>
</html>
