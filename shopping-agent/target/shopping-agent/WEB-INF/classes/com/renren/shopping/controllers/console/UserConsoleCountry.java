package com.renren.shopping.controllers.console;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.renren.shopping.conf.ResultConf;
import com.renren.shopping.model.User;
import com.renren.shopping.services.UserService;

/**
 * 用户信息
 * 
 * @author kk
 * 
 */
@Controller
@RequestMapping("/console/user")
public class UserConsoleCountry {

	@Autowired
	UserService userService;

	/**
	 * 展示用户信息
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("{id}")
	public ModelAndView user(@PathVariable("id") int id) {
		ModelAndView mav = new ModelAndView("jsonView");
		User user = userService.getUser(id);
		mav.addObject(user);
		return mav;
	}

	/**
	 * 显示加v用户
	 * 
	 * @param request
	 * @param response
	 * @param p
	 * @return
	 */
	@RequestMapping("showv")
	public ModelAndView setUserIsV(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "p", required = false) Integer p,
			@RequestParam(value = "limit", required = false) Integer limit) {
		if (p == null || p == 0) {
			p = 1;
		}
		if (limit == null || limit == 0) {
			limit = 20;
		}
		int start = (p - 1) * limit;
		ModelAndView mav = new ModelAndView("console/user/v");

		List<User> userVs = userService.getUserVList(start, limit);
		mav.addObject("userVs", userVs);
		mav.addObject("p", p);
		mav.addObject("limit", limit);
		return mav;
	}

	@RequestMapping("setUserV")
	@ResponseBody
	public Object setUserV(@RequestParam("id") int id) {
		User user = userService.getUser(id);
		if (user == null) {
			return ResultConf.SOURCE_NOTEXIST_EXCEPTION;
		}
		user.setIsV(1);
		userService.updateUser(user);
		return ResultConf.SUCCESSRET;
	}

	@RequestMapping("cancelUserV")
	@ResponseBody
	public Object cancelUserV(@RequestParam("id") int id) {
		User user = userService.getUser(id);
		if (user == null) {
			return ResultConf.SOURCE_NOTEXIST_EXCEPTION;
		}
		user.setIsV(0);
		userService.updateUser(user);
		return ResultConf.SUCCESSRET;
	}

}
