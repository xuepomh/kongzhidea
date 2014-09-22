<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ tag language="java" pageEncoding="UTF-8" body-content="empty"
	description="时间戳格式化"%>
	
<%@ attribute name="time" required="true" type="java.lang.Long"%>
<%@ attribute name="formatter" required="true" type="java.lang.String"%>

<%@ tag import="com.renren.shopping.util.TimeFormatUtil"%>

<c:set value="${time}" var="time" scope="request" />
<c:set value="${formatter}" var="formatter" scope="request" />
<%
	// 注意：标签文件不要有空格输出
	Long time = (Long) request.getAttribute("time");
	String formatter = (String) request.getAttribute("formatter");
	out.print(TimeFormatUtil.format(time, formatter));
%>