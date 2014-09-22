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
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.renren.shopping.annotion.MobileLoginRequired;
import com.renren.shopping.conf.ResultConf;
import com.renren.shopping.conf.TaskConf;
import com.renren.shopping.conf.TravelConf;
import com.renren.shopping.model.Good;
import com.renren.shopping.model.Task;
import com.renren.shopping.services.GoodService;
import com.renren.shopping.services.SearchService;
import com.renren.shopping.services.TaskService;
import com.renren.shopping.services.UploadService;
import com.renren.shopping.services.UserService;

/**
 * 任务相关
 * 
 * @author kk
 * 
 */
@Controller
@RequestMapping("/mobile/task")
public class MobileTaskController {

	@Autowired
	TaskService taskService;

	@Autowired
	GoodService goodService;

	@Autowired
	UserService userService;

	@Autowired
	SearchService searchService;

	public static Log logger = LogFactory.getLog(MobileTaskController.class);

	/**
	 * 发布任务
	 * 
	 * @return
	 */
	@MobileLoginRequired
	@RequestMapping(value = "/publish", method = { RequestMethod.POST })
	@ResponseBody
	public Object publish(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam("user_id") int userId,
			@RequestParam("title") String title,
			@RequestParam("gratuity") double gratuity,
			@RequestParam("deadline") long deadline,
			@RequestParam("message") String message,
			@RequestParam(value = "remark", required = false) String remark,
			@RequestParam(value = "good_country", required = false) String goodCountry,
			@RequestParam(value = "money", required = false) Double money,
			@RequestParam(value = "good_number", required = false) Integer goodNumber,
			@RequestParam(value = "live_city", required = false) String liveCity) {
		if (money == null) {
			money = 0.0;
		}
		if (goodNumber == null) {
			goodNumber = 1;
		}
		// 添加商品
		// 转型为MultipartHttpRequest：
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		// 获得文件：
		List<MultipartFile> good_photos = multipartRequest
				.getFiles("good_photos");
		// 验证
		JSONObject validate = UploadService.getInstance().validate(good_photos);
		if (!ResultConf.isSuccess(validate)) {
			return validate;
		}
		Good good = goodService.saveGoodAndPhoto(title, good_photos);
		/*********************************/
		// 保存任务
		Task task = new Task();
		task.setUserId(userId);
		task.setTitle(title);
		task.setGratuity(gratuity);
		task.setDeadline(deadline);
		task.setMessage(message);
		task.setRemark(remark);
		task.setGoodCountry(goodCountry);
		task.setMoney(money);
		task.setGoodNumber(goodNumber);
		task.setLiveCity(liveCity);
		if (good != null) {
			task.setGoodId(good.getId());
		}
		taskService.addTask(task);
		taskService.afterAddTask(task);
		return ResultConf.getSuccessResult(task.getId());
	}

	/**
	 * 任务流
	 * 
	 * @param user_id
	 *            当前登陆用户 TODO 验证登陆信息 当前登陆用户,未登录为0
	 * @param endId
	 *            加载下一页时候得到上一页的最后一个id
	 * @param limit
	 *            加载条数
	 */
	@RequestMapping(value = "/index", method = { RequestMethod.POST })
	@ResponseBody
	public Object index(HttpServletRequest request,
			HttpServletResponse response, @RequestParam("user_id") int user_id,
			@RequestParam(value = "end_id", required = false) Long endId,
			@RequestParam(value = "limit", required = false) Integer limit) {
		if (endId == null || endId == 0) {
			endId = Long.MAX_VALUE;
		}
		if (limit == null || limit == 0) {
			limit = TaskConf.TASK_LOAD_DEFAULTNUM;
		}
		List<Task> tasks = taskService.getTaskListByIndex(endId, limit);
		JSONArray jar = taskService.getTaskIndex(user_id, tasks);
		return ResultConf.getSuccessResult(jar, limit);
	}

	/**
	 * 根据目的国家筛选
	 * 
	 * @param user_id
	 * @param countryName
	 * @return
	 */
	@RequestMapping(value = "/screenByCountry", method = { RequestMethod.POST })
	@ResponseBody
	public Object screenByCountry(HttpServletRequest request,
			HttpServletResponse response, @RequestParam("user_id") int userId,
			@RequestParam("country") String countryName,
			@RequestParam(value = "end_id", required = false) Long endId,
			@RequestParam(value = "limit", required = false) Integer limit) {
		if (endId == null || endId == 0) {
			endId = Long.MAX_VALUE;
		}
		if (limit == null || limit == 0) {
			limit = TaskConf.TASK_LOAD_DEFAULTNUM;
		}
		List<Task> tasks = taskService.getTasksByCountyName(countryName, endId,
				limit);
		JSONArray jar = taskService.getTaskIndex(userId, tasks);
		return ResultConf.getSuccessResult(jar, limit);
	}

	/**
	 * 根据跑腿费筛选
	 * 
	 * @param user_id
	 * @return
	 */
	@RequestMapping(value = "/screenByGratuity", method = { RequestMethod.POST })
	@ResponseBody
	public Object screenByGratuity(HttpServletRequest request,
			HttpServletResponse response, @RequestParam("user_id") int userId,
			@RequestParam(value = "end_id", required = false) Long endId,
			@RequestParam(value = "limit", required = false) Integer limit) {
		if (endId == null || endId == 0) {
			endId = Long.MAX_VALUE;
		}
		if (limit == null || limit == 0) {
			limit = TaskConf.TASK_LOAD_DEFAULTNUM;
		}
		List<Task> tasks = taskService.getTasksByGratuity(endId, limit);
		JSONArray jar = taskService.getTaskIndex(userId, tasks);
		return ResultConf.getSuccessResult(jar, limit);
	}

	/**
	 * 根据居住城市筛选
	 * 
	 * @param user_id
	 * @param city
	 * @return
	 */
	@RequestMapping(value = "/screenByCity", method = { RequestMethod.POST })
	@ResponseBody
	public Object screenByLiveCity(HttpServletRequest request,
			HttpServletResponse response, @RequestParam("user_id") int userId,
			@RequestParam("city") String city,
			@RequestParam(value = "end_id", required = false) Long endId,
			@RequestParam(value = "limit", required = false) Integer limit) {
		if (endId == null || endId == 0) {
			endId = Long.MAX_VALUE;
		}
		if (limit == null || limit == 0) {
			limit = TaskConf.TASK_LOAD_DEFAULTNUM;
		}
		city = StringUtils.trim(city);
		if (city.endsWith("市")) {
			city = city.substring(0, city.length() - 1);
		}
		List<Task> tasks = taskService.getTasksByLiveCity(city, endId, limit);
		JSONArray jar = taskService.getTaskIndex(userId, tasks);
		return ResultConf.getSuccessResult(jar, limit);
	}

	/**
	 * 根据搜索词得到推荐词语
	 * 
	 * @param word
	 * @return
	 */
	@RequestMapping(value = "/searchSuggestion", method = { RequestMethod.POST })
	@ResponseBody
	public Object searchSuggestion(
			@RequestParam(value = "word", required = false) String word,
			@RequestParam(value = "limit", required = false) Integer limit) {
		if (limit == null || limit == 0) {
			limit = TaskConf.TASK_SUGGEST_DEFAULTNUM;
		}
		List<String> ret = searchService.getSuggestion(word, limit);
		return ResultConf.getSuccessResult(JSONArray.fromObject(ret), limit);
	}

	/**
	 * 根据搜索词得到任务列表
	 * 
	 * @param word
	 * @return
	 */
	@RequestMapping(value = "/searchTasks", method = { RequestMethod.POST })
	@ResponseBody
	public Object searchTasks(HttpServletRequest request,
			HttpServletResponse response, @RequestParam("word") String word,
			@RequestParam(value = "end_id", required = false) Long endId,
			@RequestParam(value = "limit", required = false) Integer limit) {
		if (endId == null || endId == 0) {
			endId = Long.MAX_VALUE;
		}
		if (limit == null || limit == 0) {
			limit = TaskConf.TASK_LOAD_DEFAULTNUM;
		}
		List<Task> tasks = searchService.getMatchList(word, endId, limit);
		JSONArray jar = taskService.getTaskIndex(0, tasks);
		return ResultConf.getSuccessResult(jar, limit);
	}

	/**
	 * 用户任务主页
	 * 
	 * @param user_id
	 */
	@RequestMapping(value = "/getUserTask", method = { RequestMethod.POST })
	@ResponseBody
	public Object userTask(HttpServletRequest request,
			HttpServletResponse response, @RequestParam("user_id") int userId,
			@RequestParam("task_id") long taskId) {
		JSONObject obj = taskService.getParsedUserTask(userId, taskId);
		return ResultConf.getSuccessResult(obj);
	}

	/**
	 * 删除任务
	 * 
	 * @param userId
	 * @param taskId
	 * @return
	 */
	@MobileLoginRequired
	@RequestMapping(value = "/delete", method = { RequestMethod.POST })
	@ResponseBody
	public Object delete(HttpServletRequest request,
			HttpServletResponse response, @RequestParam("user_id") int userId,
			@RequestParam("task_id") long taskId) {
		Task task = taskService.getTask(taskId);
		if (task == null || task.getUserId() != userId
				|| task.getDelete_flag() != 0) {
			return ResultConf.SOURCE_NOTEXIST_EXCEPTION;
		}
		task.setDelete_flag(1);
		taskService.updateTask(task);
		taskService.afterDeleteTask(task);
		return ResultConf.getSuccessResult(taskId);
	}

}
