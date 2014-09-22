package com.kk.test.define.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

/**
 * 
 * @author Jingweiyu
 */
public class OperationController extends MultiActionController {
	/**
	 * url :/operation?method=plus&num1=1&num2=2
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView plus(HttpServletRequest request,
			HttpServletResponse response) {
		int num1 = Integer.valueOf(request.getParameter("num1"));
		int num2 = Integer.valueOf(request.getParameter("num2"));
		String exper = "运算结果：<br><br>&nbsp&nbsp&nbsp&nbsp" + num1 + "+" + num2
				+ "=" + (num1 + num2);
		Map<String, String> map = new HashMap<String, String>();
		map.put("num1", num1 + "");
		map.put("num2", num2 + "");
		map.put("exper", exper);
		return new ModelAndView("jsonView", "map", map);
	}

	public ModelAndView minus(HttpServletRequest request,
			HttpServletResponse response) {
		int num1 = Integer.valueOf(request.getParameter("num1"));
		int num2 = Integer.valueOf(request.getParameter("num2"));
		String exper = "运算结果：<br><br>&nbsp&nbsp&nbsp&nbsp" + num1 + "-" + num2
				+ "=" + (num1 - num2);
		Map<String, String> map = new HashMap<String, String>();
		map.put("num1", num1 + "");
		map.put("num2", num2 + "");
		map.put("exper", exper);
		return new ModelAndView("jsonView", "map", map);
	}

	public ModelAndView multiply(HttpServletRequest request,
			HttpServletResponse response) {
		int num1 = Integer.valueOf(request.getParameter("num1"));
		int num2 = Integer.valueOf(request.getParameter("num2"));
		String exper = "运算结果：<br><br>&nbsp&nbsp&nbsp&nbsp" + num1 + "*" + num2
				+ "=" + (num1 * num2);
		Map<String, String> map = new HashMap<String, String>();
		map.put("num1", num1 + "");
		map.put("num2", num2 + "");
		map.put("exper", exper);
		return new ModelAndView("jsonView", "map", map);
	}

	public ModelAndView division(HttpServletRequest request,
			HttpServletResponse response) {
		int num1 = Integer.valueOf(request.getParameter("num1"));
		int num2 = Integer.valueOf(request.getParameter("num2"));
		Map<String, String> map = new HashMap<String, String>();
		map.put("num1", num1 + "");
		map.put("num2", num2 + "");
		if (num2 == 0) {
			map.put("exper", "零不能做除数！");
		} else {
			String exper = "运算结果：<br><br>&nbsp&nbsp&nbsp&nbsp" + num1 + "/"
					+ num2 + "=" + (num1 / num2);
			map.put("exper", exper);
		}
		return new ModelAndView("jsonView", "map", map);
	}
}
