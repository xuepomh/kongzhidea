package com.renren.shopping.controllers.test;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
 * 注册页面
 * 
 * @author kk
 * 
 */
@Controller
@RequestMapping("register")
public class RegisterController {

	@Autowired
	UserService userService;

	@Autowired
	UserLoginService userLoginService;

	public static Log logger = LogFactory.getLog(RegisterController.class);

	/**
	 * 账号注册
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("")
	public ModelAndView register(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mav = new ModelAndView("register/register");
		return mav;
	}

	/**
	 * 账号注册,识别账号，邮箱，手机号
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "register", method = RequestMethod.POST)
	public ModelAndView register(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam("account") String account,
			@RequestParam("password") String password,
			@RequestParam(value = "icode", required = false) String icode) {
		// 判断用户是否已经注册
		List<String> errors = userLoginService.validate(account);
		if (errors.size() > 0) {
			logger.error(errors.toString());
			return new ModelAndView(new RedirectView("/register"));
		}
		// TODO 手机注册验证验证码
		// 添加用户 默认name为account
		User user = userLoginService.createUser(account, password, account);
		// 用户登录
		if (user != null) {
			userLoginService.loginWeb(request, response, user.getId());
			return new ModelAndView(new RedirectView("/u"));
		}
		return new ModelAndView(new RedirectView("/register"));
	}
}
