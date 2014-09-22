package com.renren.shopping.services;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.renren.shopping.conf.KVConf;
import com.renren.shopping.conf.ResultConf;
import com.renren.shopping.conf.GoodConf;
import com.renren.shopping.conf.TaskConf;
import com.renren.shopping.conf.UserConf;
import com.renren.shopping.dao.TaskDao;
import com.renren.shopping.model.Good;
import com.renren.shopping.model.Task;
import com.renren.shopping.model.User;
import com.renren.shopping.util.CommonUtil;
import com.renren.shopping.util.RedisAPI;
import com.renren.shopping.util.RedisUtil;

@Service
public class TaskService {

	@Autowired
	TaskDao taskDao;

	/****************************/
	@Autowired
	UserService userService;

	@Autowired
	GoodService goodService;

	@Autowired
	SearchService searchService;

	private static final Log logger = LogFactory.getLog(TaskService.class);

	/*************** 任务 ****************/
	public void addTask(Task task) {
		taskDao.saveObject(task);
	}

	public void updateTask(Task task) {
		taskDao.updateObject(task);
	}

	public void deleteTask(Task task) {
		taskDao.deleteObject(task);
	}

	public Task getTask(long id) {
		return taskDao.getObject(Task.class, id);
	}

	/**************************************/

	/**
	 * 得到所有任务列表 慎用!
	 * 
	 * @return
	 */
	public List<Task> getTaskList() {
		String hql = "from Task";
		return taskDao.find(hql);
	}

	/**
	 * 通过in查询得到task列表
	 * 
	 * @param ids
	 * @return
	 */
	public List<Task> getTaskListDesc(List<Long> ids) {
		return taskDao.getTaskListDesc(ids);
	}

	/**
	 * 任务流首页
	 * 
	 * @return
	 */
	public List<Task> getTaskListByIndex(long endId, int limit) {
		String hql = "from Task where id < ? and delete_flag = 0 order by id desc";
		return taskDao.getListForPage2(hql, limit, endId);
	}

	/**
	 * 根据目的国家筛选
	 * 
	 * @param countryName
	 * @return
	 */
	public List<Task> getTasksByCountyName(String countryName, long endId,
			int limit) {
		String hql = "from Task where goodCountry = ? and id < ? and delete_flag = 0 order by id desc";
		return taskDao.getListForPage2(hql, limit, countryName, endId);
	}

	/**
	 * 根据跑腿费筛选 ，使用redis缓存列表
	 * 
	 * @return
	 */
	public List<Task> getTasksByGratuity(long endId, int limit) {
		long pos = RedisAPI.zrevrank(KVConf.TASK_GRATUITY_SORT,
				String.valueOf(endId));
		pos = pos < 0 ? -1 : pos;
		Set<String> ids = RedisAPI.zrevrange(
				KVConf.TASK_GRATUITY_SORT, pos + 1, pos + limit);
		List<Task> list = taskDao.getTaskListDesc(CommonUtil.getLongList(ids));
		Collections.sort(list, new Comparator<Task>() {

			@Override
			public int compare(Task o1, Task o2) {
				return CommonUtil.getIntegerCompare0(o2.getGratuity()
						- o1.getGratuity());
			}

		});
		return list;

	}

	/**
	 * 根据居住城市筛选
	 * 
	 * @param city
	 * @return
	 */
	public List<Task> getTasksByLiveCity(String city, long endId, int limit) {
		String hql = "from Task where liveCity like ? and id < ? and delete_flag = 0 order by id desc";
		return taskDao.getListForPage2(hql, limit, city + "%", endId);
	}

	/**
	 * 得到某用户的任务列表
	 * 
	 * @return
	 */
	public List<Task> getTasksByUser(int userId, long endId, int limit) {
		String hql = "from Task where userId = ? and id < ? and delete_flag = 0 order by id desc";
		return taskDao.getListForPage2(hql, limit, userId, endId);
	}

	/**
	 * 将任务流首页数据解析为json格式
	 * 
	 * @param currentUserId
	 * @param tasks
	 * @return
	 */
	public JSONArray getTaskIndex(int currentUserId, List<Task> tasks) {
		JSONArray jar = new JSONArray();
		for (Task task : tasks) {
			try {
				JSONObject obj = new JSONObject();
				// user信息
				User user = userService.getUser(task.getUserId());
				userService.supplyMiniJsonObject(obj, currentUserId, user);
				// task信息
				supplyJsonObject(obj, task);
				// 商品信息
				Good good = goodService.getGood(task.getGoodId());
				goodService.supplyMiniGoodJsonObject(obj, currentUserId, good);

				jar.add(obj);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		return jar;
	}

	/**
	 * 将task信息添加到JsonObject中
	 * 
	 * @param obj
	 * @param task
	 */
	public void supplyJsonObject(JSONObject obj, Task task) {
		JSONObject t = new JSONObject();
		if (task == null) {
			return;
		}
		try {
			t.put(TaskConf.TASK_ID, task.getId());
			t.put(TaskConf.TASK_TITLE, task.getTitle());
			t.put(TaskConf.TASK_GRATUITY, task.getGratuity());
			t.put(TaskConf.TASK_DEADLINE, task.getDeadline());
			t.put(TaskConf.TASK_MESSAGE, task.getMessage());
			t.put(TaskConf.TASK_GOOD_COUNTRY, task.getGoodCountry());
			t.put(TaskConf.TASK_MONEY, task.getMoney());
			t.put(TaskConf.TASK_GOOD_NUMBER, task.getGoodNumber());
			t.put(TaskConf.TASK_LIVE_CITY, task.getLiveCity());
			t.put(GoodConf.GOOD_ID, task.getGoodId());
			t.put(UserConf.USER_ID, task.getUserId());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			t = new JSONObject();
		}
		obj.put(TaskConf.TASKKEY, t);
	}

	/**
	 * 将用户的任务列表 数据解析为json格式
	 * 
	 * @param currentUserId
	 * @param tasks
	 * @return
	 */
	public JSONArray getParsedUserTaskList(int currentUserId, List<Task> tasks) {
		JSONArray jar = new JSONArray();
		for (Task task : tasks) {
			try {
				JSONObject obj = new JSONObject();
				// task信息
				supplyJsonObject(obj, task);
				// 商品信息
				Good good = goodService.getGood(task.getGoodId());
				goodService.supplyMiniGoodJsonObject(obj, currentUserId, good);

				jar.add(obj);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		return jar;
	}

	/**
	 * 新任务主页，具体一个任务
	 * 
	 * @param currentUserId
	 *            当前登陆用户
	 * @param id
	 *            任务id
	 * @return
	 */
	public JSONObject getParsedUserTask(int currentUserId, long id) {
		JSONObject obj = new JSONObject();
		try {
			Task task = getTask(id);
			if (!taskExists(task)) {
				return ResultConf.NULL;
			}
			// 用户信息
			User user = userService.getUser(task.getUserId());
			userService.supplyMiniJsonObject(obj, currentUserId, user);
			// 任务信息
			supplyJsonObject(obj, task);
			// 商品信息
			Good good = goodService.getGood(task.getGoodId());
			goodService.supplyJsonObject(obj, currentUserId, good);

			return obj;

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResultConf.NULL;
		}
	}

	/**
	 * 判断task是否存在
	 * 
	 * @param task
	 * @return true存在，false不存在
	 */
	public boolean taskExists(Task task) {
		if (task == null || task.getDelete_flag() != 0) {
			return false;
		}
		return true;
	}

	/**
	 * 判断task是否存在
	 * 
	 * @param task
	 * @return true存在，false不存在
	 */
	public boolean taskExists(long taskId) {
		return taskExists(getTask(taskId));
	}

	/**
	 * 添加任务完成后的一些操作
	 * 
	 * @param task
	 */
	public void afterAddTask(Task task) {
		// 搜索使用
		searchService.insertTaskIndex(task.getId(), task.getTitle(),
				task.getMessage());
		// 跑腿费
		RedisAPI.zadd(KVConf.TASK_GRATUITY_SORT,
				task.getGratuity(), String.valueOf(task.getId()));

	}

	/**
	 * 删除任务完成后的一些操作
	 * 
	 * @param task
	 */
	public void afterDeleteTask(Task task) {
		// 搜索使用
		searchService.deleteTaskIndex(task.getId());
		// 跑腿费
		RedisAPI.zrem(KVConf.TASK_GRATUITY_SORT,
				String.valueOf(task.getId()));
	}
}
