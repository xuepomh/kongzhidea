package com.renren.captcha.servlet;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.renren.captcha.manager.CaptchaManager;
import com.renren.captcha.pojo.AbstractCaptcha;
import com.renren.captcha.pojo.AbstractToken;
import com.renren.captcha.pojo.RRToken;

/**
 * 生成验证码
 * @author zhihui.kong@renren-inc.com
 *
 */
public class CaptchaServlet extends HttpServlet {

	private static final Log logger = LogFactory.getLog(CaptchaServlet.class);
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		ApplicationContext context = WebApplicationContextUtils
				.getRequiredWebApplicationContext(getServletContext());
		CaptchaManager captchaManager = (CaptchaManager) context.getBean(
				"captchaManager", CaptchaManager.class);

		response.setContentType("image/png");
		response.setHeader("cache", "no-cache");
		OutputStream outputStream = null;
		try {
			outputStream = response.getOutputStream();
			AbstractToken rrToken = new RRToken(request);
			if (rrToken.isValid()) {
				AbstractCaptcha rrCaptcha = captchaManager
						.generateAndCacheCaptcha(rrToken.getToken());
				ImageIO.write((BufferedImage) rrCaptcha.getImage(), "png",
						outputStream);
			}
			outputStream.flush();
		} catch (Exception e) {
			logger.error("error when get captcha image.", e);
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					logger.error("error when close outputStream.", e);
				}
			}
		}

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doGet(req, resp);
	}

}
