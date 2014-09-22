package com.renren.shopping.controllers.test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.renren.shopping.model.User;
import com.renren.shopping.services.UserLoginService;
import com.renren.shopping.services.UserService;

/**
 * 用户设置页
 * 
 * @author kk
 * 
 */
@Controller
@RequestMapping("u")
public class UserController {

	@Autowired
	UserService userService;

	@Autowired
	UserLoginService userLoginService;

	/**
	 * 用户信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("")
	public ModelAndView home(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mav = new ModelAndView("jsonView");
		User user = userLoginService.identifyUser(request, response);
		mav.addObject(user);
		return mav;
	}
}
