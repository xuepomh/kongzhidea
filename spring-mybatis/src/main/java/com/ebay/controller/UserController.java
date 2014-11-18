package com.ebay.controller;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ebay.model.User;
import com.ebay.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

	public static final Log logger = LogFactory.getLog(UserController.class);

	@Autowired
	UserService userService;

	// jdbc
	@RequestMapping("/save")
	@ResponseBody
	public Object save(@RequestParam("name") String name) {
		try {
			name = new String(name.getBytes("ISO-8859-1"), "utf-8");
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(), e);
		}
		userService.saveJUser(name);
		return "ok";
	}

	@RequestMapping("/add")
	public String add() {
		return "test/add_user";
	}

	@RequestMapping(value = "save_post", method = RequestMethod.POST)
	@ResponseBody
	public Object save_post(@RequestParam("name") String name) {
		userService.saveJUser(name);
		return "ok";
	}

	@RequestMapping("/get")
	@ResponseBody
	public Object get(@RequestParam("id") int id) {
		User user = userService.getUser(id);
		return user;
	}

	@RequestMapping("/list")
	@ResponseBody
	public Object list(@RequestParam("name") String name) {
		try {
			name = new String(name.getBytes("ISO-8859-1"), "utf-8");
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(), e);
		}
		return userService.getJUserList('%' + name + '%');
	}

	// mybatis
	@RequestMapping("/get2")
	@ResponseBody
	public Object get2(@RequestParam("id") int id) {
		User user = userService.getUser(id);
		return user;
	}

	@RequestMapping("/list2")
	@ResponseBody
	public Object list2(@RequestParam("name") String name) {
		try {
			name = new String(name.getBytes("ISO-8859-1"), "utf-8");
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(), e);
		}
		return userService.getJUserList(name + '%');
	}

	@RequestMapping("/save2")
	@ResponseBody
	public Object save2(@RequestParam("name") String name) {
		try {
			name = new String(name.getBytes("ISO-8859-1"), "utf-8");
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(), e);
		}
		User user = new User(name);
		user.setBirthYear((int) (Math.random() * 40));
		userService.saveUser(user);
		return user;
	}

	@RequestMapping("/update2")
	@ResponseBody
	public Object update2(@RequestParam("id") int id,
			@RequestParam("name") String name) {
		try {
			name = new String(name.getBytes("ISO-8859-1"), "utf-8");
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(), e);
		}
		User user = userService.getUser(id);
		user.setName(name);
		user.setBirthYear((int) (Math.random() * 40));
		userService.updateUser(user);
		return user;
	}

	@RequestMapping("/delete2")
	@ResponseBody
	public Object delete2(@RequestParam("id") int id) {
		userService.deleteUser(id);
		return "ok";
	}

	@RequestMapping("/limit2")
	@ResponseBody
	public Object limit2(@RequestParam("id") int id,
			@RequestParam("st") int st, @RequestParam("ed") int ed) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("id", id);
		param.put("start", st);
		param.put("limit", ed);
		return userService.getUserLimit(param);
	}
}
