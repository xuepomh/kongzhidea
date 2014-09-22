<%@ page contentType="text/html;charset=UTF-8" %><%@ taglib
    uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>加v用户</title>
	<link rel="shortcut icon" type="image/x-icon" href="http://a.xnimg.cn/favicon-rr.ico?ver=3" />
	<link rel="stylesheet" href="/static/css/country/country.css" />
	<link rel="stylesheet" href="/static/css/jquery/jquery-ui.css" />
	<script src="/static/js/jquery/jquery-1.11.0.js"></script>
	<script src="/static/js/jquery/jquery-ui.js"></script>
	<script type="text/javascript" src="/static/js/tablesorter/jquery-latest.js"></script>
	<script type="text/javascript" src="/static/js/tablesorter/jquery.tablesorter.js"></script>
<script type="text/javascript">
$(function() {
		$("#users").tablesorter()
	});
	
	function deleteUser(id,name){
		if(confirm("确定要取消" + name + "加V认证吗?"))
		{
			$.post("/console/user/cancelUserV", {
                'id': id,
            	},
	            function(data, status) {
            		if (status != 'success') {
                        alert("数据：" + data + "\n状态：" + status);
                    }
            		$(".data-" + id).hide(100)
            	}
            );
		}
	}
	
	function setV(){
    	var id=window.prompt("用户ID:","");
    	$.post("/console/user/setUserV", {
            'id': id,
        	},
            function(data, status) {
        		if (status != 'success') {
                    alert("数据：" + data + "\n状态：" + status);
                }
        		if(data.code != 0){
        			alert(data.code + ".." + data.msg)
        		}
        		location.reload()
        	}
        );
    }
</script>
</head>
<body>
	<div>
		<table id="users" class="content1 tablesorter">
			<caption>
					<h2>加V用户</h2> 
					<h4><a href="javascript:void(0)" onclick="setV()">添加加V用户</a></h4>
			</caption>
			<thead>
				<tr>
					<th>ID</th>
					<th>账号</th>
					<th>名字</th>
					<th>加V</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${userVs }" var="item">
				<tr class="data-${item.id }">
					<td>
						<a href="/console/user/${item.id }">${item.id }</a> 
						<a href="#" onclick="deleteUser(${item.id},'${item.name }')"><img class="delete" src="/static/image/common/delete.gif"></img></a>
					</td>
					<td>${item.account }</td>
					<td>${item.name }</td>
					<td>${item.isV }</td>
				</tr>
				</c:forEach>
			</tbody>
		</table>
		<a href="/console/user/showv?p=${p+1 }&limit=${limit}" style="float: right;right: 380px;position: relative;"> 下一页</a>
		<a href="/console/user/showv?p=${p-1 }&limit=${limit}" style="float: right;right: 420px;position: relative;"> 上一页</a>
	</div>
</body>
</html>
