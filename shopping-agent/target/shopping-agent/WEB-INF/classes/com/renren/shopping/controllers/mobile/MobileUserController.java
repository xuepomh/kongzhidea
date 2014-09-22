package com.renren.shopping.controllers.mobile;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.renren.shopping.annotion.MobileLoginRequired;
import com.renren.shopping.conf.GoodConf;
import com.renren.shopping.conf.ResultConf;
import com.renren.shopping.conf.TaskConf;
import com.renren.shopping.conf.TravelConf;
import com.renren.shopping.model.AddressUser;
import com.renren.shopping.model.CollectionPhoto;
import com.renren.shopping.model.Task;
import com.renren.shopping.model.Travel;
import com.renren.shopping.model.User;
import com.renren.shopping.model.UserDetail;
import com.renren.shopping.model.UserFollows;
import com.renren.shopping.model.UserFollowsPK;
import com.renren.shopping.services.AddressService;
import com.renren.shopping.services.GoodService;
import com.renren.shopping.services.TaskService;
import com.renren.shopping.services.TravelService;
import com.renren.shopping.services.UserService;

/**
 * 用户相关
 * 
 * @author kk
 * 
 */
@Controller
@RequestMapping("/mobile/user")
public class MobileUserController {

	@Autowired
	UserService userService;

	@Autowired
	AddressService addressService;

	@Autowired
	GoodService goodService;

	@Autowired
	TaskService taskService;

	@Autowired
	TravelService travelService;

	public static Log logger = LogFactory.getLog(MobileUserController.class);

	/**
	 * 关注
	 * 
	 * @return
	 */
	@MobileLoginRequired
	@RequestMapping(value = "/follow", method = { RequestMethod.POST })
	@ResponseBody
	public Object collectionGood(HttpServletRequest request,
			HttpServletResponse response, @RequestParam("user_id") int userId,
			@RequestParam("follow_id") int folowId) {
		if (userId == 0 || folowId == 0 || userId == folowId) {
			return ResultConf.PARAM_EXCEPTION;
		}
		UserFollows uf = new UserFollows();
		uf.setUserFollowsPK(new UserFollowsPK(userId, folowId));
		try {
			userService.attention(uf);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			if (userService.getUserFollows(userId, folowId) != null) {
				return ResultConf.getResult(1, "已关注");
			}
			return ResultConf.SYSTEM_EXCEPTION;
		}
		return ResultConf.SUCCESSRET;
	}

	/**
	 * 设置头像
	 * 
	 * @return
	 */
	@MobileLoginRequired
	@RequestMapping(value = "/setHeadurl", method = { RequestMethod.POST })
	@ResponseBody
	public Object setHeadUrl(HttpServletRequest request,
			HttpServletResponse response, @RequestParam("user_id") int userId,
			@RequestParam("head_url") MultipartFile file) {
		JSONObject ret = userService.setHeadUrl(userId, file);
		if (!ResultConf.isSuccess(ret)) {
			return ret;
		}
		return ResultConf.SUCCESSRET;
	}

	/**
	 * 设置姓名
	 * 
	 * @return
	 */
	@MobileLoginRequired
	@RequestMapping(value = "/setName", method = { RequestMethod.POST })
	@ResponseBody
	public Object setHeadUrl(HttpServletRequest request,
			HttpServletResponse response, @RequestParam("user_id") int userId,
			@RequestParam("name") String name) {
		User user = userService.getUser(userId);
		if (user == null) {
			return ResultConf.USER_NOTEXIST_EXCEPTION;
		}
		user.setName(name);
		userService.updateUser(user);
		return ResultConf.SUCCESSRET;
	}

	/**
	 * 设置性别
	 * 
	 * @return
	 */
	@MobileLoginRequired
	@RequestMapping(value = "/setSex", method = { RequestMethod.POST })
	@ResponseBody
	public Object setSex(HttpServletRequest request,
			HttpServletResponse response, @RequestParam("user_id") int userId,
			@RequestParam("sex") String sex) {
		User user = userService.getUser(userId);
		if (user == null) {
			return ResultConf.USER_NOTEXIST_EXCEPTION;
		}
		sex = StringUtils.upperCase(sex);
		if (!UserDetail.isSexValidate(sex)) {
			return ResultConf.USER_SEX_EXCEPTION;
		}
		UserDetail detail = user.getUserDetail();
		detail.setSex(sex);
		user.setUserDetail(detail);
		userService.updateUser(user);
		return ResultConf.SUCCESSRET;
	}

	/**
	 * 设置电话
	 * 
	 * @return
	 */
	@MobileLoginRequired
	@RequestMapping(value = "/setPhone", method = { RequestMethod.POST })
	@ResponseBody
	public Object setPhone(HttpServletRequest request,
			HttpServletResponse response, @RequestParam("user_id") int userId,
			@RequestParam("phone") String tel) {
		User user = userService.getUser(userId);
		if (user == null) {
			return ResultConf.USER_NOTEXIST_EXCEPTION;
		}
		UserDetail detail = user.getUserDetail();
		detail.setTel(tel);
		user.setUserDetail(detail);
		userService.updateUser(user);
		return ResultConf.SUCCESSRET;
	}

	/**
	 * 设置邮箱
	 * 
	 * @return
	 */
	@MobileLoginRequired
	@RequestMapping(value = "/setEmail", method = { RequestMethod.POST })
	@ResponseBody
	public Object setEmail(HttpServletRequest request,
			HttpServletResponse response, @RequestParam("user_id") int userId,
			@RequestParam("email") String email) {
		User user = userService.getUser(userId);
		if (user == null) {
			return ResultConf.USER_NOTEXIST_EXCEPTION;
		}
		UserDetail detail = user.getUserDetail();
		detail.setEmail(email);
		user.setUserDetail(detail);
		userService.updateUser(user);
		return ResultConf.SUCCESSRET;
	}

	/**
	 * 得到用户信息
	 * 
	 * @param user_id
	 *            当前登陆用户
	 * @param host_id
	 *            查看的用户id
	 * @return
	 */
	@RequestMapping(value = "/getUser2", method = { RequestMethod.POST })
	@ResponseBody
	public Object getUser2(HttpServletRequest request,
			HttpServletResponse response, @RequestParam("user_id") int userId,
			@RequestParam("host_id") int hostId) {
		User user = userService.getUser(hostId);
		JSONObject obj = new JSONObject();
		userService.parsedUserInfo(obj, userId, user);
		return ResultConf.getSuccessResult(obj);
	}

	/**
	 * 得到用户信息
	 * 
	 * @param user_id
	 *            用户id
	 * @return
	 */
	@MobileLoginRequired
	@RequestMapping(value = "/getUser", method = { RequestMethod.POST })
	@ResponseBody
	public Object getUser(HttpServletRequest request,
			HttpServletResponse response, @RequestParam("user_id") int userId) {
		User user = userService.getUser(userId);
		JSONObject obj = new JSONObject();
		userService.parsedUserInfo(obj, userId, user);
		return ResultConf.getSuccessResult(obj);
	}

	/**
	 * 用户的收藏列表
	 * 
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/getUserCollections", method = RequestMethod.POST)
	@ResponseBody
	public Object getUserCollectionList(HttpServletRequest request,
			HttpServletResponse response, @RequestParam("user_id") int userId,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "limit", required = false) Integer limit) {
		if (page == null || page == 0) {
			page = 1;
		}
		if (limit == null || limit == 0) {
			limit = GoodConf.USER_COLLECTION_DEFAULT_NUM;
		}
		List<CollectionPhoto> collections = goodService
				.getCollectionPhotoListByUserId(userId, page, limit);
		JSONArray jar = goodService.getParsedUserCollectionList(userId,
				collections);
		return ResultConf.getSuccessResult(jar, limit);
	}

	/**
	 * 用户任务列表
	 * 
	 * @param user_id
	 */
	@RequestMapping(value = "/getUserTasks", method = { RequestMethod.POST })
	@ResponseBody
	public Object userTaskList(HttpServletRequest request,
			HttpServletResponse response, @RequestParam("user_id") int userId,
			@RequestParam(value = "end_id", required = false) Long endId,
			@RequestParam(value = "limit", required = false) Integer limit) {
		if (endId == null || endId == 0) {
			endId = Long.MAX_VALUE;
		}
		if (limit == null || limit == 0) {
			limit = TaskConf.TASK_LOAD_DEFAULTNUM;
		}
		List<Task> tasks = taskService.getTasksByUser(userId, endId, limit);
		JSONArray jar = taskService.getParsedUserTaskList(userId, tasks);
		return ResultConf.getSuccessResult(jar, limit);
	}

	/**
	 * 用户的行程列表
	 * 
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/getUserTravels", method = { RequestMethod.POST })
	@ResponseBody
	public Object userTravelList(HttpServletRequest request,
			HttpServletResponse response, @RequestParam("user_id") int userId,
			@RequestParam(value = "end_id", required = false) Long endId,
			@RequestParam(value = "limit", required = false) Integer limit) {
		if (endId == null || endId == 0) {
			endId = Long.MAX_VALUE;
		}
		if (limit == null || limit == 0) {
			limit = TravelConf.TRAVEL_LOAD_DEFAULTNUM;
		}
		List<Travel> travels = travelService.getTravelsByUser(userId, endId,
				limit);
		JSONArray jar = travelService.getParsedUserTravelList(userId, travels);
		return ResultConf.getSuccessResult(jar, limit);
	}
}
