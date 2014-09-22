<%@ page contentType="text/html;charset=UTF-8" %><%@ taglib
    uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE HTML>
<head>
<meta charset="utf-8">
<title>添加Task</title>
<link rel="shortcut icon" type="image/x-icon" href="http://a.xnimg.cn/favicon-rr.ico?ver=3" />
<link href="http://s.xnimg.cn/a59339/n/core/global2-all-min.css" rel="stylesheet" />
<link href="http://s.xnimg.cn/a66140/apps/openplatform/publicplatform/css/public-platform-all.css" rel="stylesheet" type="text/css" media="all" />
<link href="http://s.xnimg.cn/a65825/apps/openplatform/publicplatform/css/public-platform-console.css" rel="stylesheet" type="text/css" media="all" />
<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
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
					<h1>管理后台---添加任务</h1>
				</div>

				<div class='specialtopic-config-area'>
					<form id='add_form' action='/mobile/task/publish' method='post' enctype="multipart/form-data">
						<ul class='topic-content'>
							<li class='group'>
								<label class='fm-label'>用户 </label>
								<div class='left'>
									<select name='user_id' class='fm-select'>
										<c:forEach items="${users }" var="item">
											<option value="${item.id }">${item.name }</option>
										</c:forEach>
									</select>
								</div>
							</li>
							<li class='group'>
								<label class='fm-label'>title </label>
								<input class='fm-input' name='title' />
							</li>
							<li class='group'>
								<label class='fm-label'>gratuity </label>
								<input class='fm-input' name='gratuity' />
							</li>
							<li class='group'>
								<label class='fm-label'>deadline </label>
								<input class='fm-input' name='deadline' />
							</li>
							<li class='group'>
								<label class='fm-label'>message </label>
								<input class='fm-input' name='message' />
							</li>
							<li class='group'>
								<label class='fm-label'>remark #</label>
								<input class='fm-input' name='remark' />
							</li>
							<li class='group'>
								<label class='fm-label'>good_country #</label>
								<input class='fm-input' name='good_country' />
							</li>
							<li class='group'>
								<label class='fm-label'>money #</label>
								<input class='fm-input' name='money' />
							</li>
							<li class='group'>
								<label class='fm-label'>good_number #</label>
								<input class='fm-input' name='good_number' />
							</li>
							<li class='group'>
								<label class='fm-label'>live_city #</label>
								<input class='fm-input' name='live_city' />
							</li>
							<li class='group'>
								<label class='fm-label'>good_photo </label>
								<input type="file" name="good_photos">
							</li>
							<li class='group'>
								<label class='fm-label'>good_photo </label>
								<input type="file" name="good_photos">
							</li>
						</ul>
						<div class='submit-area'>
							<a href='#' id='submit' class='fm-submit'>保存</a>
							<a href='/country/show' id='cancel' class='fm-submit gray'>返回</a>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>

	
</body>
</html>