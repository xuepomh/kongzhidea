package com.ebay.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import com.ebay.model.User;

/**
 * 简单控制器 包名和类型随意，只要指定Controller即可,RequestMapping 控制路径的
 * 返回jsp地址在applicationContext中配置的视图名称扫描器,默认显示/views/下的jsp路径;
 * 
 * 如果controller类前不加@RequestMapping则默认是web应用的部署目录
 * 
 * @author zhihui.kong
 */
@Controller
@RequestMapping("/test")
public class TestController {

	public static final Log logger = LogFactory.getLog(TestController.class);

	@RequestMapping("/register")
	public String register() {
		logger.info("register");
		return "test/register";
	}

	@RequestMapping("/test_model")
	public String test_model(HttpServletRequest request, Model model) {
		User user = new User();
		user.setId(1);
		user.setName("孔智慧");
		model.addAttribute("user", user);
		return "test/user";
	}

	/**
	 * 接受 /test 的post请求,传递参数为一个pojo，用作提交表单使用
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "testpojoPost", method = RequestMethod.POST)
	public ModelAndView createUser(User user,
			@RequestParam(value = "sex", required = false) String sex) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("test/createSuccess");
		mav.addObject("user", user);
		mav.addObject("sex", sex);
		return mav;
	}

	/**
	 * http://agent.renren.com/test/testpojo?name=a&id=3,使用pojo作为传递参数,
	 * 如果参数不全则补null
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "testpojo", method = RequestMethod.GET)
	public ModelAndView getUser(User user) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("test/createSuccess");
		mav.addObject("user", user);
		return mav;
	}

	/**
	 * url支持Ant风格(即?(匹配任意一个字符) *(匹配任意多个字符) **(匹配多层路径,例如con/**\/test.xml
	 * 表示con类路径下当前目录及其子孙目录) 等字符)
	 */
	@RequestMapping("/testurl1/*/t?{userId}")
	public ModelAndView testUrl1(@PathVariable("userId") int userId) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("test/test");
		logger.info(userId);
		return mav;
	}

	/**
	 * 指定url参数，params = {"a=2", "b" } 表示必须有a,b参数，且a=2 ;!param 不能包含请求参数;
	 * param1!=value1 请求参数不能是value1;
	 * 
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	@RequestMapping(value = "testurl2", method = RequestMethod.GET, params = {
			"a=2", "b", "!c" })
	public ModelAndView testUrl2(@RequestParam("a") int a,
			@RequestParam("b") int b) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("test/test");
		logger.info(a + ".." + b);
		return mav;
	}

	/**
	 * 请求参数按照名称匹配的方式绑定到方法入参中
	 * 
	 * @param a
	 * @return
	 */
	@RequestMapping(value = "testurl3")
	public ModelAndView testUrl3(@RequestParam("a") int a) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("test/test");
		logger.info(a);
		return mav;
	}

	/**
	 * 请求参数按照名称匹配的方式绑定到方法入参中
	 * 
	 * RequestParam(value = "a", required = false)
	 * required默认是true，表示必须必须，否则抛异常，如果是false，则是null，则不能用int
	 * a，否则出现不能将null赋值给基本类型的异常
	 * 
	 * @param a
	 * @return
	 */
	@RequestMapping(value = "testurl4")
	public ModelAndView testUrl4(
			@RequestParam(value = "a", required = false) Integer a) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("test/test");
		logger.info(a == null ? "null" : a);
		return mav;
	}

	/**
	 * 绑定请求参数或者cookie @RequestHeader和@CookieValue
	 * 
	 * @param ua
	 * @return
	 */
	@RequestMapping(value = "testurl5")
	public ModelAndView testUrl5(@RequestHeader("user-agent") String ua) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("test/test");
		logger.info(ua);
		return mav;
	}

	/**
	 * 将servlet API作为参数传入,参数不写则默认为null
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "testurl6")
	public ModelAndView testUrl6(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("test/test");
		String a1 = request.getParameter("a");
		String a2 = WebUtils.findParameterValue(request, "a");
		logger.info(a1 + ".." + a2);
		return mav;
	}

	/**
	 * 将servlet API作为参数传入,url跳转
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "testurl7")
	public void testUrl7(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			response.sendRedirect("http://www.baidu.com");
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * forward跳转
	 * 
	 * @return
	 */
	@RequestMapping(value = "testurl8")
	public String testUrl8() {
		return "forward:/test/register";
	}

	/**
	 * redirect跳转
	 * 
	 * @return
	 */
	@RequestMapping(value = "testurl9")
	public String testUrl9() {
		return "redirect:http://agent.renren.com/test/register";
	}

	/**
	 * 直接将对象返回，不返回一个视图@ResponseBody
	 * 
	 * @return
	 */
	@RequestMapping("/testresponsestring1")
	@ResponseBody
	public Object testResponseString1() {
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 1; i < 10; i++) {
			list.add(i);
		}
		return list;
	}
}
