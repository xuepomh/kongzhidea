<%@ page contentType="text/html;charset=UTF-8" %><%@ taglib
    uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>编辑城市</title>
<link rel="shortcut icon" type="image/x-icon" href="http://a.xnimg.cn/favicon-rr.ico?ver=3" />
<link href="http://s.xnimg.cn/a59339/n/core/global2-all-min.css" rel="stylesheet" />
<link href="http://s.xnimg.cn/a66140/apps/openplatform/publicplatform/css/public-platform-all.css" rel="stylesheet" type="text/css" media="all" />
<link href="http://s.xnimg.cn/a65825/apps/openplatform/publicplatform/css/public-platform-console.css" rel="stylesheet" type="text/css" media="all" />
<script src="/static/js/jquery/jquery-1.11.0.js"></script>
<script type="text/javascript">
$(function() {
	$("#submit").click(function() {
        $("#edit_form").submit()
	})
});
</script>
</head>
<body>

	<div class='main-wrap'>
		<div class='main'>
			<div class="content-area">
				<div class="title">
					<h1>管理后台---编辑城市</h1>
					<h4><a href="/console/province/show">城市列表</a></h4>
				</div>

				<div class='specialtopic-config-area'>
					<form id='edit_form' action='/console/province/editCity' method='post'>
						<input type="hidden" name="id" value="${city.id }"/>
						<ul class='topic-content'>
							<li class='group'>
								<label class='fm-label'>城市名字</label>
								<input class='fm-input' name='name' value="${city.name }"/>
							</li>
							<li class='group'>
								<label class='fm-label'>所属城市 </label>
								<div class='left'>
									<select name='province' class='fm-select'>
										<c:forEach items="${clist }" var="item">
											<option value="${item.id }"
												<c:if test="${city.provinceId == item.id }">selected="selected"</c:if>
											>${item.name }</option>
										</c:forEach>
									</select>
								</div>
							</li>
							<li class='group'>
								<label class='fm-label' title="默认为0，表示不热门，>0表示热门，热门城市中按照该值来排序">是否热门! </label>
								<input class='fm-input' name='hot' value="${city.hot }" />
							</li>
							
							<li class='group'>
								<label class='fm-label' title="默认是0，按照该值来排序">权重! </label>
								<input class='fm-input' name='weight' value="${city.weight }"/>
							</li>
							
						</ul>
						<div class='submit-area'>
							<a href='#' id='submit' class='fm-submit'>保存</a>
							<a href='/console/province/show' id='cancel' class='fm-submit gray'>返回</a>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>

	
</body>
</html>