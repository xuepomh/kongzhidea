package com.renren.captcha.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.renren.captcha.manager.CaptchaManager;
import com.renren.captcha.pojo.AbstractToken;
import com.renren.captcha.pojo.RRToken;

/**
 * 校验验证码
 * 
 * @author zhihui.kong@renren-inc.com
 * 
 */
public class TestCaptchaServlet extends HttpServlet {

	private static final Log logger = LogFactory
			.getLog(TestCaptchaServlet.class);
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		ApplicationContext context = WebApplicationContextUtils
				.getRequiredWebApplicationContext(getServletContext());
		CaptchaManager captchaManager = (CaptchaManager) context.getBean(
				"captchaManager", CaptchaManager.class);

		// 解析token。 目前触屏的有一个post参数，模式为 "_PASSWORD_7895642122"
		String captcha = request.getParameter("captcha");
		AbstractToken rrToken = new RRToken(request);
		boolean ret = captchaManager.checkValicode(rrToken.getToken(), captcha);
		PrintWriter pw = response.getWriter();
		pw.write("check captcha result is :" + ret);
		pw.flush();
		pw.close();

	}
}
