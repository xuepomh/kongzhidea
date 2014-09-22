package com.renren.shopping.services;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.renren.shopping.conf.CommentConf;
import com.renren.shopping.conf.ResultConf;
import com.renren.shopping.conf.UserConf;
import com.renren.shopping.dao.UserDao;
import com.renren.shopping.dao.UserFollowsDao;
import com.renren.shopping.model.Comment;
import com.renren.shopping.model.UploadModel;
import com.renren.shopping.model.User;
import com.renren.shopping.model.UserFollows;
import com.renren.shopping.model.UserFollowsPK;

@Service
public class UserService {
	public static final Log logger = LogFactory.getLog(UserService.class);
	@Autowired
	UserDao userDao;

	@Autowired
	UserFollowsDao userFollowsDao;
	
	@Autowired
	CommentService commentService;

	public void addUser(User user) {
		userDao.saveObject(user);
	}

	public void updateUser(User user) {
		userDao.updateObject(user);
	}

	public void deleteUser(User user) {
		userDao.deleteObject(user);
	}

	public User getUser(int id) {
		return userDao.getObject(User.class, id);
	}

	/**
	 * 根据账号得到用户
	 * 
	 * @return
	 */
	public User getUserByAccount(String account) {
		String hql = "from User where account=?";
		List<User> list = userDao.find(hql, account);
		if (list.size() == 0) {
			return null;
		}
		return list.get(0);
	}

	/**
	 * 根据名字得到用户
	 * 
	 * @return
	 */
	public User getUserByName(String name) {
		String hql = "from User where name=?";
		List<User> list = userDao.find(hql, name);
		if (list.size() == 0) {
			return null;
		}
		return list.get(0);
	}

	/**
	 * 得到用户列表
	 * 
	 * @param id
	 * @return
	 */
	public List<User> getUserList(int start, int limit) {
		String hql = "from User";
		return userDao.getListForPage(hql, start, limit);
	}

	/**
	 * 得到加V用户列表
	 * 
	 * @param id
	 * @return
	 */
	public List<User> getUserVList(int start, int limit) {
		String hql = "from User where isV = 1 ";
		return userDao.getListForPage(hql, start, limit);
	}

//	/**
//	 * 计算用户星级 TODO 测试方案
//	 * 
//	 * @param user
//	 * @return
//	 */
//	public int getUserStar(int userId) {
//		List<UserFollows> list = getUserFollowedFansId(userId);
//		//粉丝数量
//		int fans;
//		if(list == null){
//			fans = 0;
//		}else{
//			fans = list.size(); 
//		}
//		List<Comment> comments = commentService.getCommentsList(userId);
//		double commentScore = 0;
//		if(comments == null){
//			commentScore = 0;
//		}else{
//			for(Comment comment : comments){
//				commentScore += comment.getScore();
//			}
//			commentScore = commentScore/comments.size() + commentScore%comments.size();
//		}
//		double fanScore = (100000 - 100000/(Math.pow(2, fans/10 + fans%10)))*(CommentConf.FOLLOW_PERCENT);
//		double score = commentScore*(CommentConf.SCORE_PERCENT) + fanScore/10000 + fanScore%10000;
//		return Integer.parseInt(new java.text.DecimalFormat("0").format(score));
//	}
	
	/**
	 * 计算用户星级 TODO 测试方案
	 * 
	 * @param user
	 * @return
	 */
	public int getUserStar(User user) {
		int star = user.getId() % 5 + 1;
		return star ;
	}

	/********************* 关注 ***************************/

	/**
	 * 关注
	 * 
	 * @param userFollows
	 */
	public void attention(UserFollows userFollows) {
		userFollowsDao.saveObject(userFollows);
	}

	/**
	 * 得到粉丝关注的user信息
	 * 
	 * @param fanId
	 * @return
	 */
	public List<UserFollows> getFanAttentionUserIds(int fanId) {
		// 1
		String sql = "from UserFollows where userFollowsPK.fanId = ?";
		return userFollowsDao.find(sql, fanId);

		// 2
		// return userFollowsDao.findByNamedQuery(
		// "com.renren.shopping.model.UserFollows.getFollowedIds", fanId);

		// 3
		// String sql = "from UserFollows where userFollowsPK.fanId = :fanId";
		// return userFollowsDao.findByNamedParam(sql, "fanId", fanId);

		// 4
		// String sql = "from UserFollows where userFollowsPK.fanId = :fanId ";
		// return userFollowsDao.findByNamedParam(sql,
		// userFollowsDao.getStringArray("fanId"),
		// userFollowsDao.getObjectArray(fanId));

	}

	/**
	 * 得到粉丝关注的user信息
	 * 
	 * @param fanId
	 * @return
	 */
	public List<User> getFanAttentionUsers(int fanId) {
		List<User> ret = new ArrayList<User>();
		List<UserFollows> fansList = getFanAttentionUserIds(fanId);
		for (UserFollows userFollows : fansList) {
			User u = getUser(userFollows.getUserFollowsPK().getUserId());
			ret.add(u);
		}
		return ret;
	}

	/**
	 * 得到用户的粉丝信息
	 * 
	 * @param userId
	 * @return
	 */
	public List<UserFollows> getUserFollowedFansId(int userId) {
		// 1
		String sql = "from UserFollows where userFollowsPK.userId = ?";
		return userFollowsDao.find(sql, userId);

		// 2
		// String nameQuery = "com.renren.shopping.model.UserFollows.getFanIds";
		// return userFollowsDao.findByNamedQueryAndNamedParam(nameQuery,
		// "userId", userId);

		// 3
		// String nameQuery = "com.renren.shopping.model.UserFollows.getFanIds";
		// return userFollowsDao.findByNamedQueryAndNamedParam(nameQuery,
		// userFollowsDao.getStringArray("userId"),
		// userFollowsDao.getObjectArray(userId));
	}

	/**
	 * 得到用户的粉丝信息
	 * 
	 * @param userId
	 * @return
	 */
	public List<User> getFollowsFans(int userId) {
		List<User> ret = new ArrayList<User>();
		List<UserFollows> fansList = getUserFollowedFansId(userId);
		for (UserFollows userFollows : fansList) {
			User u = getUser(userFollows.getUserFollowsPK().getFanId());
			ret.add(u);
		}
		return ret;
	}

	/**
	 * 判断是否关注
	 * 
	 * @param fanId
	 * @param userId
	 * @return
	 */
	public UserFollows getUserFollows(int fanId, int userId) {
		UserFollows userFollows = new UserFollows();
		UserFollowsPK userFollowsPK = new UserFollowsPK(fanId, userId);
		userFollows.setUserFollowsPK(userFollowsPK);
		return userFollowsDao.getObject(UserFollows.class, userFollowsPK);
	}

	public void deleteUserFollows(int fanId, int userId) {
		UserFollows userFollows = new UserFollows();
		UserFollowsPK userFollowsPK = new UserFollowsPK(fanId, userId);
		userFollows.setUserFollowsPK(userFollowsPK);
		userFollowsDao.deleteObject(userFollows);
	}

	/*********************************************/
	/**
	 * 将user信息添加到JsonObject中
	 * 
	 * @param obj
	 * @param currentUserId
	 *            当前登陆用户,0 表示未登录
	 * @param user
	 */
	public void supplyJsonObject(JSONObject obj, int currentUserId, User user) {
		JSONObject u = new JSONObject();
		if (user == null) {
			return;
		}
		try {
			fillUserInfo(currentUserId, user, u);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			u = new JSONObject();
		}

		obj.put(UserConf.USERKEY, u);
	}

	/**
	 * 将user 基本 信息添加到JsonObject中
	 * 
	 * @param obj
	 * @param currentUserId
	 *            当前登陆用户,0 表示未登录
	 * @param user
	 */
	public void supplyMiniJsonObject(JSONObject obj, int currentUserId,
			User user) {
		JSONObject u = new JSONObject();
		if (user == null) {
			return;
		}
		try {
			fillUserBasicInfo(user, u);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			u = new JSONObject();
		}

		obj.put(UserConf.USERKEY, u);
	}
	
	/**
	 * 将List<User> 基本 信息添加到JsonObject中
	 * 
	 * @param obj
	 * @param currentUserId
	 *            当前登陆用户,0 表示未登录
	 * @param user
	 */
	public void supplyMiniJsonObject(JSONObject obj, int currentUserId,
			List<User> users) {
		JSONArray jar = new JSONArray();
		if (users == null || users.size() == 0) {
			obj.put(UserConf.USERKEY, jar);    //users为空时，依然要往obj中添加一个字段，不然客户端会提示“自愿不存在”
			return;
		}
		try {
			for(User user : users){
				JSONObject u = new JSONObject();
				if(user == null){
					continue;
				}
				fillUserBasicInfo(user, u);
				jar.add(u);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		obj.put(UserConf.USERKEY, jar);
	}

	/**
	 * 得到个人中心的用户的详细信息
	 * 
	 * @param obj
	 * @param currentUserId
	 *            当前登陆用户,0 表示未登录
	 * @param user
	 */
	public void parsedUserInfo(JSONObject obj, int currentUserId, User user) {
		JSONObject u = new JSONObject();
		if (user == null) {
			return;
		}
		try {
			fillUserInfo(currentUserId, user, u);
			u.put(UserConf.USER_PHONE, StringUtils.trimToEmpty(user.getUserDetail().getTel()));
			u.put(UserConf.USER_EMAIL, StringUtils.trimToEmpty(user.getUserDetail().getEmail()));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			u = new JSONObject();
		}

		obj.put(UserConf.USERKEY, u);
	}

	private void fillUserInfo(int currentUserId, User user, JSONObject u) {
		fillUserBasicInfo(user, u);
		u.put(UserConf.USER_STAR, getUserStar(user));
		u.put(UserConf.USER_ISV, user.getIsV());
		u.put(UserConf.CURRENT_USER_ID, currentUserId);

		// 关注信息
		if (currentUserId == 0 || currentUserId == user.getId()) {
			// u.put(UserConf.USER_ISFOLLOW, 0);
		} else {
			UserFollows uf = getUserFollows(currentUserId, user.getId());
			u.put(UserConf.USER_ISFOLLOW, uf == null ? 0 : 1);
		}
	}

	private void fillUserBasicInfo(User user, JSONObject u) {
		u.put(UserConf.USER_ACCOUNT, user.getAccount());
		u.put(UserConf.USER_ID, user.getId());
		u.put(UserConf.USER_NAME, StringUtils.trimToEmpty(user.getName()));
		u.put(UserConf.USER_DESCRIPTION, StringUtils.trimToEmpty(user.getDescription()));
		u.put(UserConf.ORIGINAL_URL, StringUtils.trimToEmpty(user.getOriginal_url()));
		u.put(UserConf.MAIN_URL, StringUtils.trimToEmpty(user.getMain_url()));
		u.put(UserConf.HEAD_URL, StringUtils.trimToEmpty(user.getHead_url()));
		u.put(UserConf.TINY_URL, StringUtils.trimToEmpty(user.getTiny_url()));
		u.put(UserConf.USER_SEX, StringUtils.trimToEmpty(user.getUserDetail().getSex()));
	}

	/**
	 * 设置用户头像
	 */
	public JSONObject setHeadUrl(int userId, MultipartFile file) {
		User user = getUser(userId);
		if (user == null) {
			return ResultConf.USER_NOTEXIST_EXCEPTION;
		}

		JSONObject vali = UploadService.getInstance().validate(file);
		if (!ResultConf.isSuccess(vali)) {
			return vali;
		}
		UploadModel model = UploadService.getInstance().uploadGj(file);
		if (model == null) {
			return ResultConf.SYSTEM_EXCEPTION;
		}
		user.setOriginal_url(model.getOriginal_url());
		user.setMain_url(model.getMain_url());
		user.setHead_url(model.getHead_url());
		user.setTiny_url(model.getTiny_url());
		updateUser(user);
		JSONObject json = ResultConf.SUCCESSRET;
		JSONObject data = new JSONObject();
		data.put("main_url", model.getMain_url());
		data.put("original_url", model.getOriginal_url());
		data.put("head_url", model.getHead_url());
		data.put("tiny_url", model.getTiny_url());
		json.put(ResultConf.DATA, data);
		return json;
	}

}
