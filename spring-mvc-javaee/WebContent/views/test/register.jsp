<%@ page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>简单控制器</title>
    <!--
	<link rel="stylesheet" type="text/css" href="styles.css">
    -->
    <style type="text/css">
<!--
.STYLE3 {font-size: 9pt}
-->
    </style>
</head>
  
  <body>
  <center>
    <span class="STYLE3">用户注册</span>
  </center>
  	<form action="/spring-mvc/test" method="post">
    <table align="center">
    	<tr>
    		<td height="23"><span class="STYLE3">输入用户名：</span></td>
   		  <td height="23"><input name="name" type="text"></td>
    	</tr>
    	<tr>
    		<td height="23"><span class="STYLE3">输入密码：</span></td>
   		  <td height="23"><input name="pwd" type="password"></td>
    	</tr>
    	<tr>
    		<td height="23" colspan="2" align="center">
    			<span class="STYLE3">
    			<input type="submit" value="注册">
    			<input type="reset" value="重置">
   		        </span>
   		    </td>
    	</tr>
    </table>
    </form>
  </body>
</html>
