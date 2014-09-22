<%@ page contentType="text/html;charset=UTF-8" %><%@ taglib
    uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>用户登录</title>
<link rel="shortcut icon" type="image/x-icon" href="http://a.xnimg.cn/favicon-rr.ico?ver=3" />
<link href="http://s.xnimg.cn/a59339/n/core/global2-all-min.css" rel="stylesheet" />
<link href="http://s.xnimg.cn/a66140/apps/openplatform/publicplatform/css/public-platform-all.css" rel="stylesheet" type="text/css" media="all" />
<link href="http://s.xnimg.cn/a65825/apps/openplatform/publicplatform/css/public-platform-console.css" rel="stylesheet" type="text/css" media="all" />
<script src="/static/js/jquery/jquery-1.11.0.js"></script>
<script type="text/javascript">
$(function() {
	$("#submit").click(function() {
        $("#login_form").submit()
	})
	
	$("#reset").click(function() {
        $("#login_form")[0].reset()
	})
});
</script>
</head>
<body>

	<div class='main-wrap'>
		<div class='main'>
			<div class="content-area">
				<div class="title">
					<h1>用户注册</h1>
				</div>

				<div class='specialtopic-config-area'>
					<form id='login_form' action='/login/do' method='post'>
						<input type="hidden" name="back_url" value="${backUrl }"/>
						<ul class='topic-content'>
							<li class='group'>
								<label class='fm-label'>账号</label>
								<input class='fm-input' name='account' type="text" />
							</li>
							<li class='group'>
								<label class='fm-label'>密码</label>
								<input class='fm-input' name='password' type="password"/>
							</li>
							
						</ul>
						<div class='submit-area'>
							<a href='#' id='submit' class='fm-submit'>保存</a>
							<a href='#' id='reset' class='fm-submit gray'>重置</a>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>

	
</body>
</html>