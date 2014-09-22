package com.kk.test.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CookieValue;
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

import com.kk.test.annotion.LoginRequired;
import com.kk.test.model.User;
import com.kk.test.model.UserTest;
import com.kk.test.service.HibernateUtil;
import com.kk.test.service.JDBCUtil;
import com.kk.test.service.UserServic;
import com.kk.test.service.UserService;
import com.kk.test.model.TestId;

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
public class UserTestCon {

	public static final Log logger = LogFactory.getLog(UserTestCon.class);

	@Autowired
	UserServic userServic;

	@Autowired
	UserService userService;

	@Autowired
	JDBCUtil jdbcUtil;

	@Autowired
	HibernateUtil hibUtil;

	@RequestMapping("/register")
	public String register() {
		logger.info("register");
		return "test/register";
	}

	@RequestMapping("/test_model")
	public String test_model(HttpServletRequest request, Model model) {
		model.addAttribute("attributeName", "model");
		User user = new User();
		user.setId(1);
		user.setName("孔智慧");
		model.addAttribute("user", user);
		return "test2/test";
	}

	/**
	 * 接受 /test 的post请求,传递参数为一个pojo，用作提交表单使用
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView createUser(UserTest user) {
		userServic.createUser(user);
		ModelAndView mav = new ModelAndView();
		mav.setViewName("test/createSuccess");
		mav.addObject("user", user);
		return mav;
	}

	/**
	 * http://agent.renren.com/test/testpojo?name=a&pwd=b,使用pojo作为传递参数,
	 * 如果参数不全则补null
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "testpojo", method = RequestMethod.GET)
	public ModelAndView getUser(UserTest user) {
		userServic.createUser(user);
		ModelAndView mav = new ModelAndView();
		mav.setViewName("test/createSuccess");
		mav.addObject("user", user);
		return mav;
	}

	@RequestMapping("/listTestIds")
	public ModelAndView listTestIds() {
		List<TestId> list = userService.getTestIdList();
		logger.info(list);
		ModelAndView mav = new ModelAndView();
		mav.setViewName("test/testids");
		mav.addObject("list", list);
		return mav;
	}

	@RequestMapping(value = { "/jdbc", "/jdbc2" }, method = RequestMethod.GET)
	public ModelAndView jdbc() {
		List<TestId> list = jdbcUtil.getTestIdList();
		ModelAndView mav = new ModelAndView();
		mav.setViewName("test/jdbc");
		mav.addObject("list", list);
		logger.info(mav.toString());
		logger.info("findTestId:  " + jdbcUtil.findTestId(1));
		logger.info("findTestId2:  " + jdbcUtil.findTestId2(2));
		// jdbcUtil.testExecute();
		logger.info("getQueryInt:  " + jdbcUtil.getTestCount() + ".."
				+ jdbcUtil.getTestId());
		logger.info("getQueryList: " + jdbcUtil.getTestList());
		// jdbcUtil.testInsert(6);
		logger.info("findTestIdNew:  " + jdbcUtil.findTestIdNew(11));
		logger.info("findTestIdListNew:  " + jdbcUtil.findTestIdListNew());
		// jdbcUtil.testInsert2(7);
		// logger.info("insert user generateKey:  "
		// + jdbcUtil.testInsertUser("孔智慧2"));
		// List<User> actors = new ArrayList<User>();
		// actors.add(new User("k1"));
		// actors.add(new User("k2"));
		// logger.info("update Batch:  " + jdbcUtil.updateBatchActors(actors));
		logger.info("get user:  " + jdbcUtil.getTestUser(1));
		logger.info("get userlist:  " + jdbcUtil.getTestUserList(1));
		// hibUtil.addUser(new User("孔智慧3"));
		// User u = hibUtil.getUser(10);
		// logger.info(u);
		// u.setName("test2");
		// hibUtil.updateUser(u);
		// logger.info(u);
		// hibUtil.deleteUser(u);
		logger.info(hibUtil.findUsersByName("k"));
		logger.info(hibUtil.findUsersByName2("孔"));
		// hibUtil.addUser2(new User("孔智慧4"));
		logger.info("get user:  " + hibUtil.getUser_get(1));
		logger.info("get user:  " + hibUtil.getUser_get(2));
		logger.info("get user:  " + hibUtil.getUser_load(1));
		// hibUtil.update_auto(11, "kong");
		// User u2 = hibUtil.getUser_get(11);
		// u2.setName("kong2");
		// hibUtil.update_manual(u2);
		// hibUtil.deleteUser2(hibUtil.getUser_get(17));
		// hibUtil.deleteUser2(new User(16, "sdfd"));
		hibUtil.testHQL();
		// User u3 = new User("孔智慧5");
		// hibUtil.addUser(u3);
		// logger.info(u3);
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
			"a=2", "b" })
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
		// return list 406,找不到对应的converter
		return list.toString();
	}

	/**
	 * 将请求报文体转换为字符串绑定到reqBody入参中
	 * 
	 * @param reqBody
	 * @return
	 */
	@RequestMapping(value = "testrequest1")
	public ModelAndView testRequest1(@RequestBody String reqBody) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("test/test");
		logger.info(reqBody);
		return mav;
	}

	/**
	 * RestTemplate 使用该类调用web服务
	 * 
	 * @return
	 */
	@RequestMapping(value = "testtestrequest1")
	public ModelAndView testtestRequest1() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("test/test");
		RestTemplate rest = new RestTemplate();
		MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>();
		form.add("name", "kk");
		form.add("pwd", "pass");
		// 第一个参数是url，第二个是通过MultiValueMap准备报文体的参数数据
		rest.postForLocation("http://agent.renren.com/test/testrequest1", form);
		return mav;
	}
}
