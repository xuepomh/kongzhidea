<%@ page contentType="text/html;charset=UTF-8" %><%@ taglib
    uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE HTML>
<head>
<meta charset="utf-8">
<title>发现</title>
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
					<h1>运营添加发现内容</h1>
				</div>

				<div class='specialtopic-config-area'>
					<form id='add_form' action='/shopping-agent/mobile/test/pic' method='post' enctype="multipart/form-data">
						<ul class='topic-content'>
							<li class='group'>
								<label class='fm-label'>主界面展示图 * </label>
								<input type="file" name="main_pic">
							</li>
							<li class='group'>
								<label class='fm-label'>背景图 * </label>
								<input type="file" name="background_pic">
							</li>
														<li class='group'>
								<label class='fm-label'>背景图 * </label>
								<input type="file" name="test_pic">
							</li>
														<li class='group'>
								<label class='fm-label'>背景图 * </label>
								<input type="file" name="test_pic1">
							</li>
						</ul>
						<div class='submit-area'>
							<a href='#' id='submit' class='fm-submit'>保存</a>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>

	
</body>
</html>