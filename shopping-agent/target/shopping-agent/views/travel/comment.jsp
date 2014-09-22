<%@ page contentType="text/html;charset=UTF-8" %><%@ taglib
    uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib tagdir="/WEB-INF/tags/time" prefix="time"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>行程${travel.id }-评论</title>
	<link rel="shortcut icon" type="image/x-icon" href="http://a.xnimg.cn/favicon-rr.ico?ver=3" />
	<link rel="stylesheet" href="/static/css/country/country.css" />
	<link rel="stylesheet"
	href="http://code.jquery.com/ui/1.10.2/themes/smoothness/jquery-ui.css" />
	<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
	<script src="http://code.jquery.com/ui/1.10.2/jquery-ui.js"></script>
</head>
<body>
	<ul>
		<c:forEach items="${comments }" var="item">
			<c:set var="user" value="${item.user_info }"></c:set>
			<li>
				<img src="${item.user_info.head_url }"  width="50px" height="50px"/> ${user.user_name }<br/>
				<!-- 没有回复 -->
				<c:if test="${item.comment_to_id == 0 }">
					${item.comment_content }
				</c:if>
				<c:if test="${item.comment_to_id != 0 and item.comment_to_uid !=0 }">
					回复:${item.comment_to_uname }  ${item.comment_content }
				</c:if>
				<font color="red">${item.comment_score }</font>
				<br/>
				<time:format formatter="yyyy.mm dd hh:mm:ss" time="${item.comment_time }" />
			</li>
		</c:forEach>
	</ul>
</body>
</html>
