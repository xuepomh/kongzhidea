<%@ page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>testIds</title>
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
    -->
<style type="text/css">
<!--
.STYLE3 {
	font-size: 9pt
}
-->
</style>
</head>

<body>
	<c:forEach items="${list }" var="item">
		${item.id }<br>
	</c:forEach>
</body>
</html>
