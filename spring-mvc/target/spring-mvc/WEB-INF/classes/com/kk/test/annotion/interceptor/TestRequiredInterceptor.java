package com.kk.test.annotion.interceptor;

import javax.servlet.http.HttpServletRequest;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.servlet.ModelAndView;

import com.kk.test.annotion.LoginRequired;
import com.kk.test.annotion.TestRequired;

/**
 * 验证登陆 拦截器
 * 
 * @required
 * 
 * @author kk
 */
public class TestRequiredInterceptor implements MethodInterceptor {

	@Override
	public Object invoke(MethodInvocation inv) throws Throwable {
		TestRequired testRequired = AnnotationUtils.findAnnotation(
				inv.getMethod(), TestRequired.class);
		System.out.println("test interceptor!");
		return inv.proceed();
	}

	/**
	 * 必须在方法中包含HttpServletRequest参数,否则无法获取
	 * 
	 * @param inv
	 * @return
	 */
	private HttpServletRequest getRequest(MethodInvocation inv) {
		HttpServletRequest req = null;
		Object[] args = inv.getArguments();
		for (Object o : args) {
			if (o instanceof HttpServletRequest) {
				req = (HttpServletRequest) o;
			}
		}
		return req;
	}

	// @Override
	// public Object invoke(MethodInvocation mi) throws Throwable {
	//
	// Object[] ars = mi.getArguments();
	// HttpServletRequest req = null;
	// for (Object o : ars) {
	// if (o instanceof HttpServletRequest) {
	// System.out
	// .println("------------this is a HttpServletRequest Parameter------------ ");
	// req = (HttpServletRequest) o;
	// System.out.println(req.getRequestURI() + "?"
	// + req.getQueryString());
	// }
	// }
	// // 判断该方法是否加了@LoginRequired 注解
	// if (mi.getMethod().isAnnotationPresent(LoginRequired.class)) {
	// System.out
	// .println("----------this method is added @LoginRequired-------------------------");
	// if (req != null) {
	// String sid = req.getParameter("sid");
	// if (!"1".equals(sid)) {
	// ModelAndView mav = new ModelAndView("jsonView");
	// mav.addObject("code", 1);
	// mav.addObject("msg","not login");
	// return mav;
	// }
	// }
	// }
	//
	// // 执行被拦截的方法，切记，如果此方法不调用，则被拦截的方法不会被执行。
	// Object ret = mi.proceed();
	// System.out.println("ret = " + ret);
	// return ret;
	// }

}