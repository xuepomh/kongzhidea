package com.kk.test.controller;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.kk.test.model.User;

@Controller
@RequestMapping("/test")
public class TestController {

	/**
	 * 中文乱码问题
	 * 
	 * @param sid
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping("/test_param2")
	public ModelAndView test_param2(@RequestParam("sid") String sid)
			throws UnsupportedEncodingException {
		System.out.println(new String(sid.getBytes("ISO-8859-1"), "utf-8"));
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("sid", sid);
		return mav;
	}

}
