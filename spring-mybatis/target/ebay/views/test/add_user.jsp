<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>upload file</title>
</head>
<body>
	<form action="${ctx}/user/save_post" method="post" >
		<input type="text" name="name"><br/>
		<input type="submit">
	</form>
	
</body>
</html>