package com.renren.shopping.interceptor.mobile;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.renren.shopping.annotion.MobileLoginRequired;
import com.renren.shopping.conf.ResultConf;
import com.renren.shopping.conf.UserConf;
import com.renren.shopping.model.UserLoginCookie;
import com.renren.shopping.services.UserLoginService;
import com.renren.shopping.util.CommonUtil;

/**
 * 验证登陆 拦截器
 * 
 * @required
 * 
 *           增加@MobileLoginRequired源注释
 * 
 *           在spring的配置文件中配置aop切面
 * 
 *           参数里面添加request参数
 * 
 *           使用方法 在controller中的方法前面添加@MobileLoginRequired,
 *           并且在controller中的方法的返回值是Object
 *           如果方法的返回值是ModelAndView，那么在拦截器中的返回值也要是ModelAndView，否则出错
 * 
 *           动态判断方法的返回值，如果是ModelAndView则返回jsonView，如果是Object则返回JsonObject
 * 
 * @author kk
 */

@Component
public class LoginRequiredInterceptor implements MethodInterceptor {

	@Autowired
	UserLoginService userLoginService;

	@Override
	public Object invoke(MethodInvocation inv) throws Throwable {
		MobileLoginRequired loginRequired = AnnotationUtils.findAnnotation(
				inv.getMethod(), MobileLoginRequired.class);
		HttpServletRequest request = getRequest(inv);
		Object ret = ResultConf.MOBILE_NOLOGIN_EXCEPTION;
		Class<?> returnType = inv.getMethod().getReturnType();
		if (returnType == ModelAndView.class) {
			ret = ResultConf.MOBILE_NOLOGIN_MAV;
		}
		if (loginRequired != null && request != null) {
			// 得到userId和session_key
			int userId = NumberUtils.toInt(request
					.getParameter(UserConf.LOGIN_USER_ID));
			String session_key = request
					.getParameter(UserConf.LOGIN_USER_SESSION_KEY);
			if (userId == 0 || StringUtils.isBlank(session_key)) {
				return ret;
			}
			// 验证 如果不匹配或者失效则认为是未登录
			UserLoginCookie loginCookie = userLoginService
					.getUserLoginCookie(userId);
			if (!loginCookie.getLoginCookie().equals(session_key)
					|| loginCookie.getDeadline() < CommonUtil.now()) {
				return ret;
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
}