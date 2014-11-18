package com.ebay.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ebay.model.User;

@Controller
@RequestMapping("/json")
public class JsonController {
	public static final Log logger = LogFactory.getLog(JsonController.class);

	/**
	 * {"userList":[{"name":"孔智慧","id":1}],"JSONObject":{"a":"aa"},"hashMap":{
	 * "aa":1},"user":{"name":"孔智慧","id":1}}
	 * 
	 * @return
	 */
	@RequestMapping("jackson")
	public ModelAndView jackson() {
		ModelAndView mav = new ModelAndView("jsonView");
		User user = new User(1, "孔智慧");
		mav.addObject(user);// bean 如果多个bean则使用最下面的那个
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("aa", 1);
		mav.addObject(map);// map
		List<User> list = new ArrayList<User>();
		list.add(user);
		mav.addObject(list);// list
		JSONObject obj = new JSONObject();
		obj.put("a", "aa");
		mav.addObject(obj);// JsonObject
		return mav;
	}

	@RequestMapping("object")
	@ResponseBody
	public Object object() {
		JSONObject obj = new JSONObject();
		obj.put("id", 1);
		obj.put("name", "孔智慧");
		return obj;
	}
}
