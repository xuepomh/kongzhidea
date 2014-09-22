package com.renren.shopping.interceptor;

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
 * 只有未登录能访问
 * 
 * @author kk
 * 
 */
@Component
public class CheckUnLoginInterceptor implements HandlerInterceptor {

	@Autowired
	UserLoginService userLoginService;
	public static final Log logger = LogFactory
			.getLog(CheckUnLoginInterceptor.class);

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
	 * 请求处理之前 handler.getClass().getName()是当前拦截的controller的类名
	 */
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		User user = userLoginService.identifyUser(request, response);
		if (user != null) {
			response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
			response.sendRedirect("/u");
			return false;
		}
		return true;
	}

}
