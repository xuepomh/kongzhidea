<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>设置搜索热词-搜索词为空时候展示</title>
<link rel="stylesheet" href="/static/css/jquery/jquery-ui.css" />
<script src="/static/js/jquery/jquery-1.11.0.js"></script>
<script src="/static/js/jquery/jquery-ui.js"></script>
<link rel="stylesheet" href="/static/css/country/country.css" />
<style>
body {
	font-family: "Trebuchet MS", "Helvetica", "Arial", "Verdana",
		"sans-serif";
	font-size: 62.5%;
}

#sortable {
	list-style-type: none;
	margin: 0;
	padding: 0;
	width: 60%;
}

#sortable li {
	margin: 0 3px 3px 3px;
	padding: 0.4em;
	padding-left: 1.5em;
	font-size: 1.4em;
	height: 18px;
}

#sortable li span {
	position: absolute;
	margin-left: -1.3em;
}
#sortable li span.word-item {
	margin-left: 10px;
}
</style>
<script>
  $(function() {
    $( "#sortable" ).sortable();
    $( "#sortable" ).disableSelection();
    
    $(".del").click(function (){
    	$(this).parent().remove()
    })
    $(".add").click(function (){
    	var word=window.prompt("搜索词:","");
    	if(word == ""){
    		alert("不能填空")
    		return 
    	}
    	var str="<li class=\"ui-state-default\"> <span class=\"ui-icon ui-icon-arrowthick-2-n-s\"></span> <span class=\"word-item\">"+word+"</span> <a href=\"javascript:void(0);\" class=\"del\" style=\"float: right\"><img class=\"delete\" src=\"/static/image/common/delete.gif\"></img></a> </li>"
    	$("#sortable").append(str)
    	flesh()
    })
    $(".save").click(function (){
    	var ar = new Array();
    	var items = $("span.word-item")
    	for(var i=0;i<items.size();i++){
    		var item = items[i]
    		ar.push($(item).text());
    	}
    	$.post("/console/task/setSearchHotWordDefault", {
            'words': ar,
        	},
            function(data, status) {
        		if (status != 'success') {
                    alert("数据：" + data + "\n状态：" + status);
                }
        		location.reload()
        	}
        );
    })
    function flesh(){
    	$(".del").click(function (){
    		$(this).parent().remove()
    	})
    }
  });
  </script>
</head>
<body>
	<h1 style="left: 200px; position: relative;">设置搜索热词-输入词为空时显示</h1>
	<a
		style="left: 300px; position: relative; cursor: pointer; font-size: 14px;"
		class="add">添加</a>
	<a
		style="left: 340px; position: relative; cursor: pointer; font-size: 14px;"
		class="save">保存</a>
	<b>操作完后一定要点保存，否组不生效</b>
	<ul id="sortable" style="left: 30px; position: relative;">
		<c:forEach items="${ hots}" var="item">
			<li class="ui-state-default">
				<span class="ui-icon ui-icon-arrowthick-2-n-s"></span> 
				<span class="word-item">${item}</span>
 				<a href="javascript:void(0);" class="del" style="float: right">
 				  	<img class="delete" src="/static/image/common/delete.gif"></img>
 				</a>
 			</li>
		</c:forEach>

	</ul>

</body>
</html>