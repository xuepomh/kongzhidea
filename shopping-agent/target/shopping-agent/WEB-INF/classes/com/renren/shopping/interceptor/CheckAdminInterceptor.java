package com.renren.shopping.interceptor;

import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.renren.shopping.model.User;
import com.renren.shopping.services.UserLoginService;

/**
 * 只有管理员能访问
 * 
 * @author kk
 * 
 */
@Component
public class CheckAdminInterceptor implements HandlerInterceptor {

	@Autowired
	UserLoginService userLoginService;
	public static final Log logger = LogFactory
			.getLog(CheckAdminInterceptor.class);

	/**
	 * 所有请求处理完成后,如视图呈现之后
	 */
	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object arg2, Exception arg3)
			throws Exception {
		// logger.info("MyInterceptor:afterCompletion");
	}

	/**
	 * 请求处理之后
	 */
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object arg2, ModelAndView arg3)
			throws Exception {
		// logger.info("MyInterceptor:postHandle");
	}

	/**
	 * 请求处理之前
	 */
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		String oriURL = request.getRequestURL().toString();
		if (request.getQueryString() != null) {
			oriURL = oriURL + "?" + request.getQueryString();
		}
		// 当前url
		String rewriteUrl = URLEncoder.encode(oriURL, "utf-8");

		User user = userLoginService.identifyUser(request, response);
		if (user == null) {
			response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
			response.sendRedirect("/login?back_url=" + rewriteUrl);
			return false;
		}
		if (user.getId() != 16) {
			response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
			response.sendRedirect("/u?back_url=" + rewriteUrl);
			return false;
		}
		return true;
	}
}
