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
	<form action="${ctx}/test/upload" method="post" enctype="multipart/form-data">
		<input type="text" name="name"><br/>
		<input type="file" name="file"><br/>
		<input type="file" name="file2"><br/>
		<input type="submit">
	</form>
	
	<form action="${ctx}/test/upload2" method="post" enctype="multipart/form-data">
		<input type="text" name="name"><br/>
		<input type="file" name="file"><br/>
		<input type="file" name="file"><br/>
		<input type="submit">
	</form>
</body>
</html>