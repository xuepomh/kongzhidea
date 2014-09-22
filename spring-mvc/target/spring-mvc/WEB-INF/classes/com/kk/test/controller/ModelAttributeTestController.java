package com.kk.test.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.kk.test.model.User;

@Controller
@RequestMapping("/test2")
public class ModelAttributeTestController {

	public static final Log logger = LogFactory.getLog(ModelAttributeTestController.class);

	/**
	 * 被@ModelAttribute注释的方法会在此controller每个方法执行前被执行，
	 * 
	 * 因此对于一个controller映射多个URL的用法来说，要谨慎使用
	 * 
	 * 在页面中可以使用 ${attributeName } ${user.id } ${user.name }等
	 * 
	 * @param abc
	 * @param model
	 */
	@ModelAttribute
	public void populateModel(@RequestParam String abc, Model model) {
		model.addAttribute("attributeName", abc);
	}

	/**
	 * model属性的名称没有指定，它由返回类型隐含表示，如这个方法返回Account类型，那么这个model属性的名称是account。
	 * 这个例子中model属性名称有返回对象类型隐含表示，model属性对象就是方法的返回值。它无须要特定的参数
	 * 
	 * @param id
	 * @param name
	 * @return
	 */
	@ModelAttribute
	public User addAccount(@RequestParam int id, @RequestParam String name) {
		User user = new User(id, name);
		return user;
	}

	/**
	 * 这个例子中使用@ModelAttribute注释的value属性，来指定model属性的名称。model属性对象就是方法的返回值。
	 * 它无须要特定的参数。
	 * 
	 * @param define
	 * @return
	 */
	@ModelAttribute("define")
	public String define(@RequestParam String define) {
		return define;
	}

	/***
	 * abc=1266&id=1&name=kk&define=def
	 */

	/**
	 * { attributeName: "1266", define: "def", void: null, user: { name: "kk",
	 * id: 1 } }
	 * 
	 * @return
	 */
	@RequestMapping(value = "test2", method = RequestMethod.GET)
	public ModelAndView test2() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("jsonView");
		return mav;
	}

	/**
	 * ModelAttribute("user") User
	 * user注释方法参数，参数user的值来源于addAccount()方法中的model属性。 此时如果方法体没有标注
	 * SessionAttributes("user")，那么scope为request ，如果标注了，那么scope为session
	 * 
	 * 在默认情况下，ModelMap 中的属性作用域是 request 级别
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "test3", method = RequestMethod.GET)
	public ModelAndView test3(@ModelAttribute("user") User user) {
		System.out.println(user);
		ModelAndView mav = new ModelAndView();
		mav.setViewName("jsonView");
		return mav;
	}

	/**
	 * 如果没有配置通用的@ModelAttribute,还可以从Form表单或URL参数中获取（实际上，不做此注释也能拿到user对象）
	 */

}
