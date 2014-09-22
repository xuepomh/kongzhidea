package com.renren.shopping.controllers.test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.renren.shopping.model.User;
import com.renren.shopping.services.UserLoginService;
import com.renren.shopping.services.UserService;

/**
 * 
 * 
 * @author kk
 * 
 */
@Controller
@RequestMapping("")
public class HomeController {

	@Autowired
	UserService userService;

	@Autowired
	UserLoginService userLoginService;

	public static final Log logger = LogFactory.getLog(HomeController.class);

	/**
	 * 用户登录
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("login")
	public ModelAndView login(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "back_url", required = false) String backUrl) {
		ModelAndView mav = new ModelAndView("home/login");
		mav.addObject("backUrl", backUrl);
		return mav;
	}

	/**
	 * 用户登录
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/login/do", method = RequestMethod.POST)
	public ModelAndView doLogin(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "back_url", required = false) String backUrl,
			@RequestParam("account") String account,
			@RequestParam("password") String password) {
		// 登录时候识别邮箱，手机号和普通账号
		User user = userLoginService.getUserByAccount(account);
		if (user == null || !user.getPassword_md5().equals(password)) {
			return new ModelAndView(new RedirectView("/login"));
		}
		userLoginService.loginWeb(request, response, user.getId());

		if (StringUtils.isBlank(backUrl)) {
			backUrl = "/u";
		}
		return new ModelAndView(new RedirectView(backUrl));
	}

	/**
	 * 用户登录
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("logout")
	public ModelAndView logout(HttpServletRequest request,
			HttpServletResponse response) {
		userLoginService.clearLoginCookieToWeb(response);
		return new ModelAndView(new RedirectView("/login"));
	}
}
