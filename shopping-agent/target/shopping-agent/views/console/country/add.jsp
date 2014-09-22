<%@ page contentType="text/html;charset=UTF-8" %><%@ taglib
    uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>添加国家</title>
<link rel="shortcut icon" type="image/x-icon" href="http://a.xnimg.cn/favicon-rr.ico?ver=3" />
<link href="http://s.xnimg.cn/a59339/n/core/global2-all-min.css" rel="stylesheet" />
<link href="http://s.xnimg.cn/a66140/apps/openplatform/publicplatform/css/public-platform-all.css" rel="stylesheet" type="text/css" media="all" />
<link href="http://s.xnimg.cn/a65825/apps/openplatform/publicplatform/css/public-platform-console.css" rel="stylesheet" type="text/css" media="all" />
<script src="/static/js/jquery/jquery-1.11.0.js"></script>
<script type="text/javascript">
$(function() {
	$("#submit").click(function() {
        $("#add_form").submit()
	})
});
</script>
</head>
<body>

	<div class='main-wrap'>
		<div class='main'>
			<div class="content-area">
				<div class="title">
					<h1>管理后台---添加国家</h1>
					<h4><a href="/console/country/show">国家列表</a></h4>
				</div>

				<div class='specialtopic-config-area'>
					<form id='add_form' action='/console/country/addCountry' method='post'>
						<ul class='topic-content'>
							<li class='group'>
								<label class='fm-label'>国家名字</label>
								<input class='fm-input' name='name' />
							</li>
							<li class='group'>
								<label class='fm-label'>国旗 </label>
								<input class='fm-input' name='flag' />
							</li>
							<li class='group'>
								<label class='fm-label'>国旗-缩略图 </label>
								<input class='fm-input' name='flag_thumb' />
							</li>
							<li class='group'>
								<label class='fm-label'>所属洲 </label>
								<div class='left'>
									<select name='continent' class='fm-select'>
										<c:forEach items="${clist }" var="item">
											<option value="${item.id }"
												<c:if test="${source == item.id }">selected="selected"</c:if>
											>${item.name }</option>
										</c:forEach>
									</select>
								</div>
							</li>
							<li class='group'>
								<label class='fm-label' title="默认为0，表示不热门，>0表示热门，热门国家中按照该值来排序">是否热门! </label>
								<input class='fm-input' name='hot' />
							</li>
							
							<li class='group'>
								<label class='fm-label' title="默认是0，按照该值来排序">权重! </label>
								<input class='fm-input' name='weight' />
							</li>
							
						</ul>
						<div class='submit-area'>
							<a href='#' id='submit' class='fm-submit'>保存</a>
							<a href='/console/country/show' id='cancel' class='fm-submit gray'>返回</a>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>

	
</body>
</html>