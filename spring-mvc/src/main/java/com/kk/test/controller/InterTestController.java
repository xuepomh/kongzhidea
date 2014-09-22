package com.kk.test.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.kk.test.annotion.LoginRequired;
import com.kk.test.annotion.TestRequired;
import com.kk.test.model.User;

@Controller
@RequestMapping("/inter")
public class InterTestController {

	public final Log logger = LogFactory.getLog(InterTestController.class);

	@TestRequired
	@LoginRequired
	@RequestMapping(value = "login", method = RequestMethod.GET)
	public ModelAndView login(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("jsonView");
		return mav;
	}
	
	@LoginRequired
	@RequestMapping(value = "login2", method = RequestMethod.POST)
	public ModelAndView login2(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("jsonView");
		return mav;
	}

	@RequestMapping(value = "nologin", method = RequestMethod.GET)
	public ModelAndView nologin() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("jsonView");
		return mav;
	}

}
