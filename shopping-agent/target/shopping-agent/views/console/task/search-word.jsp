<%@ page contentType="text/html;charset=UTF-8" %><%@ taglib
    uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>热词列表</title>
	<link rel="shortcut icon" type="image/x-icon" href="http://a.xnimg.cn/favicon-rr.ico?ver=3" />
	<link rel="stylesheet" href="/static/css/country/country.css" />
	<link rel="stylesheet" href="/static/css/jquery/jquery-ui.css" />
	<script src="/static/js/jquery/jquery-1.11.0.js"></script>
	<script src="/static/js/jquery/jquery-ui.js"></script>
	<script type="text/javascript" src="/static/js/tablesorter/jquery-latest.js"></script>
	<script type="text/javascript" src="/static/js/tablesorter/jquery.tablesorter.js"></script>
<script type="text/javascript">
$(function() {
		$("#words").tablesorter()
		
		$(".search_num").click(function(){
			  var id = $(this).attr("data-id")
			  var num = window.prompt("修改搜索数:",$(this).text())
			  $(this).text(num)
			  $.post("/console/task/setSearchHotWordSearchNum", {
	                'id': id,
	                'num': num,
	            	},
		            function(data, status) {
	            		if (status != 'success') {
	                        alert("数据：" + data + "\n状态：" + status);
	                    }
	            	}
	            );
		  })
	});
	
	function deleteHotWord(id,name){
		if(confirm("确定要删除" + name + "吗?"))
		{
			$.post("/console/task/deleteSearchHotWord", {
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
	<div>
		<table id="words" class="content1 tablesorter">
			<caption>
					<h2>热词列表</h2> 
					<h4> 
						<form action="/console/task/search" method="post" target="_blank">
							<input type="text" name="word" />
							<button>搜索</button>
						</form>
					</h4>
			</caption>
			<thead>
				<tr>
					<th>ID</th>
					<th>热词</th>
					<th>搜索数</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${list }" var="item">
				<tr class="data-${item.id }">
					<td>
						${item.id }
						<a href="#" onclick="deleteHotWord(${item.id},'${item.word }')"><img class="delete" src="/static/image/common/delete.gif"></img></a>
					</td>
					<td>${item.word }</td>
					<td><span class="search_num" data-id=${item.id }>${item.searchNum }</span></td>
				</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	
</body>
</html>
