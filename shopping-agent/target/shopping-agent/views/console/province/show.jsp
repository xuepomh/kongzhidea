<%@ page contentType="text/html;charset=UTF-8" %><%@ taglib
    uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>${province.name }</title>
	<link rel="shortcut icon" type="image/x-icon" href="http://a.xnimg.cn/favicon-rr.ico?ver=3" />
	<link rel="stylesheet" href="/static/css/country/country.css" />
	<link rel="stylesheet" href="/static/css/jquery/jquery-ui.css" />
	<script src="/static/js/jquery/jquery-1.11.0.js"></script>
	<script src="/static/js/jquery/jquery-ui.js"></script>
	<script type="text/javascript" src="/static/js/tablesorter/jquery-latest.js"></script>
	<script type="text/javascript" src="/static/js/tablesorter/jquery.tablesorter.js"></script>
<script type="text/javascript">
$(function() {
		$("#citys").tablesorter()
	});
	
	function deleteCity(id,name){
		if(confirm("确定要删除" + name + "吗?"))
		{
			$.post("/console/province/deleteCity", {
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
</script>
</head>
<body>
	<a href="/console/province/hot">热门城市</a>  
	<c:forEach items="${clist }" var="item">
		<a href="/console/province/show/${item.id }">${item.name }</a>  
	</c:forEach>
	<br/>
	
	<div>
		<table id="citys" class="content1 tablesorter">
			<caption>
					<h2>${province.name }的城市列表</h2> 
					<h4><a href="/console/province/add?source=${province.id }">添加城市</a></h4>
			</caption>
			<thead>
				<tr>
					<th>ID</th>
					<th>城市</th>
					<th>热门</th>
					<th>权重</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${province.cities }" var="item">
				<tr class="data-${item.id }">
					<td>
						<a href="/console/province/edit/${item.id }">${item.id }</a> 
						<a href="#" onclick="deleteCity(${item.id},'${item.name }')"><img class="delete" src="/static/image/common/delete.gif"></img></a>
					</td>
					<td>${item.name }</td>
					<td>${item.hot }</td>
					<td>${item.weight }</td>
				</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</body>
</html>
