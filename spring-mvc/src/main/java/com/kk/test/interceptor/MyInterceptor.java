package com.kk.test.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class MyInterceptor implements HandlerInterceptor {
	public static final Log logger = LogFactory.getLog(MyInterceptor.class);

	/**
	 * 所有请求处理完成后,如视图呈现之后
	 */
	@Override
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
//		logger.info("MyInterceptor:afterCompletion");
	}

	/**
	 * 请求处理之后
	 */
	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {
//		logger.info("MyInterceptor:postHandle");
	}

	/**
	 * 请求处理之前
	 */
	@Override
	public boolean preHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2) throws Exception {
//		logger.info("MyInterceptor:preHandle");
		return true;
	}

}
