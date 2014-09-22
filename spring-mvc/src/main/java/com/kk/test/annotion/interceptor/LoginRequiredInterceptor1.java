package com.kk.test.annotion.interceptor;

import javax.servlet.http.HttpServletRequest;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.servlet.ModelAndView;

import com.kk.test.annotion.LoginRequired;

/**
 * 验证登陆 拦截器
 * 
 * @required
 * 
 *           增加@LoginRequired源注释
 * 
 *           在spring的配置文件中配置aop切面
 * 
 *           实现jsonView视图(因为返回值是一个json格式，如果是直接跳转则使用RedirectView,不需jsonView)
 * 
 *           参数里面添加request参数
 * 
 *           使用方法 在controller中的方法前面添加@LoginRequired
 *           并且返回值是ModelAndView,否则如果在拦截器中返回json则出错，
 *           如果方法的返回值是Object，那么在拦截器中的返回值最好不要是ModelAndView，可以是json和string
 * @author kk
 */
public class LoginRequiredInterceptor1 implements MethodInterceptor {

	@Override
	public Object invoke(MethodInvocation inv) throws Throwable {
		LoginRequired loginRequired = AnnotationUtils.findAnnotation(
				inv.getMethod(), LoginRequired.class);
		HttpServletRequest request = getRequest(inv);
		if (loginRequired != null && request != null) {
			int userId = NumberUtils.toInt(request.getParameter("user_id"));
			String session_key = request.getParameter("session_key");
			if (userId == 0 || StringUtils.isBlank(session_key)) {
				ModelAndView mav = new ModelAndView("jsonView");
				mav.addObject("nologin", 1);
				return mav;
			}
		}
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