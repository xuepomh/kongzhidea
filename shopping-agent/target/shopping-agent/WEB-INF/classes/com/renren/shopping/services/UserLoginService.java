package com.renren.shopping.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.renren.shopping.conf.KVConf;
import com.renren.shopping.dao.UserEmailDao;
import com.renren.shopping.dao.UserLoginCookieDao;
import com.renren.shopping.dao.UserTelephoneDao;
import com.renren.shopping.model.User;
import com.renren.shopping.model.UserDetail;
import com.renren.shopping.model.UserEmail;
import com.renren.shopping.model.UserLoginCookie;
import com.renren.shopping.model.UserTelephone;
import com.renren.shopping.util.CommonUtil;
import com.renren.shopping.util.CookieManager;
import com.renren.shopping.util.MD5Util;
import com.renren.shopping.util.RedisAPI;
import com.renren.shopping.util.RedisUtil;

@Service
public class UserLoginService {

	@Autowired
	UserLoginCookieDao userLoginCookieDao;

	@Autowired
	UserTelephoneDao userTelephoneDao;

	@Autowired
	UserEmailDao userEmailDao;

	@Autowired
	UserService userService;

	public static final Log logger = LogFactory.getLog(UserLoginService.class);

	/****************** 用户登录cookie信息 ***************/
	public void addUserLoginCookie(UserLoginCookie userLoginCookie) {
		userLoginCookieDao.saveObject(userLoginCookie);
	}

	public void updateUserLoginCookie(UserLoginCookie userLoginCookie) {
		userLoginCookieDao.updateObject(userLoginCookie);
	}

	public void deleteUserLoginCookie(UserLoginCookie userLoginCookie) {
		userLoginCookieDao.deleteObject(userLoginCookie);
	}

	public UserLoginCookie getUserLoginCookie(int userId) {
		return userLoginCookieDao.getObject(UserLoginCookie.class, userId);
	}

	/**
	 * 根据login_cookie得到登录cookie信息
	 * 
	 * @param cookie
	 * @return
	 */
	public UserLoginCookie getUserLoginCookieByCookie(String cookie) {
		String hql = "from UserLoginCookie where loginCookie = ?";
		List<UserLoginCookie> list = userLoginCookieDao.find(hql, cookie);
		if (list.size() == 0) {
			return null;
		}
		return list.get(0);
	}

	/********************* 手机登录 *************************/
	public void addUserTelephone(UserTelephone userTelephone) {
		userTelephoneDao.saveObject(userTelephone);
	}

	public void updateUserTelephone(UserTelephone userTelephone) {
		userTelephoneDao.updateObject(userTelephone);
	}

	public void deleteUserTelephone(UserTelephone userTelephone) {
		userTelephoneDao.deleteObject(userTelephone);
	}

	public UserTelephone getUserTelephone(int userId) {
		return userTelephoneDao.getObject(UserTelephone.class, userId);
	}

	public UserTelephone getUserTelephone(String telephone) {
		String hql = "from UserTelephone where telephone = ?";
		List<UserTelephone> list = userTelephoneDao.find(hql, telephone);
		if (list.size() == 0) {
			return null;
		}
		return list.get(0);
	}

	/******************** 邮箱登录 ******************/
	public void addUserEmail(UserEmail userEmail) {
		userEmailDao.saveObject(userEmail);
	}

	public void updateUserEmail(UserEmail userEmail) {
		userEmailDao.updateObject(userEmail);
	}

	public void deleteUserEmail(UserEmail userEmail) {
		userEmailDao.deleteObject(userEmail);
	}

	public UserEmail getUserEmail(int userId) {
		return userEmailDao.getObject(UserEmail.class, userId);
	}

	public UserEmail getUserEmail(String email) {
		String hql = "from UserEmail where email = ?";
		List<UserEmail> list = userEmailDao.find(hql, email);
		if (list.size() == 0) {
			return null;
		}
		return list.get(0);
	}

	/************************** login cookie相关 **********/
	/** 根据LOGIN_COOKIE来判断登录用户 */
	public static final String LOGIN_COOKIE = "__login";
	public static final String LOGIN_COOKIE_REQUEST = "__login_request";
	/** LOGIN_COOKIE在服务器端保存的时间,30天 */
	public static final long LOGIN_COOKIE_SUSTAINED = 1000l * 60 * 60 * 24 * 30;
	/** LOGIN_COOKIE在web端保存的时间,在关闭浏览器前一直生效 */
	public static final long LOGIN_COOKIE_WEB = -1;

	/**
	 * 保存_l票到库中，更新deadline
	 * 
	 * @param userId
	 * @param loginCookie
	 */
	private void saveAndUpdateUserLoginCookie(int userId, String loginCookie) {
		UserLoginCookie userLoginCookie = getUserLoginCookie(userId);
		// 不存在则添加
		if (userLoginCookie == null) {
			userLoginCookie = new UserLoginCookie();
			userLoginCookie.setUserId(userId);
			userLoginCookie.setLoginCookie(loginCookie);
			userLoginCookie.setDeadline(CommonUtil.now()
					+ LOGIN_COOKIE_SUSTAINED);
			addUserLoginCookie(userLoginCookie);
		} else {// 存在则更新
			userLoginCookie.setLoginCookie(loginCookie);
			userLoginCookie.setDeadline(CommonUtil.now()
					+ LOGIN_COOKIE_SUSTAINED);
			updateUserLoginCookie(userLoginCookie);
		}
	}

	/**
	 * 根据id生成登录后生成的_l票
	 * 
	 * @param userId
	 * @return
	 */
	private String generateLoginCookie(int userId) {
		User user = userService.getUser(userId);
		String sign = user.getAccount() + ";" + user.getPassword_md5() + ";"
				+ CommonUtil.now();
		return MD5Util.md5(sign);
	}

	/**
	 * 得到用户id得到_l票，如果票未失效，则返回，并且重置票失效时间；如果票失效，则重新生成一个，并添加到库中
	 * 
	 * @param userId
	 * @return
	 */
	public String getLoginCookie(int userId) {
		UserLoginCookie userLoginCookie = getUserLoginCookie(userId);
		String loginCookie = "";
		// 如果票未失效,直接返回,并且将失效时间延后
		if (userLoginCookie != null
				&& CommonUtil.now() <= userLoginCookie.getDeadline()) {
			loginCookie = userLoginCookie.getLoginCookie();
		} else {// 票失效,重新生成一份
			loginCookie = generateLoginCookie(userId);
		}

		saveAndUpdateUserLoginCookie(userId, loginCookie);
		return loginCookie;
	}

	/**
	 * 写登录cookie到浏览器中，声明周期为会话期
	 * 
	 * @param response
	 * @param cookie
	 */
	private void saveLoginCookieToWeb(HttpServletResponse response,
			String cookie) {
		CookieManager.getInstance().saveCookie(response, LOGIN_COOKIE, cookie);
	}

	/**
	 * 清除浏览器 登录cookie
	 * 
	 * @param response
	 * @param cookie
	 */
	public void clearLoginCookieToWeb(HttpServletResponse response) {
		CookieManager.getInstance().clearCookie(response, LOGIN_COOKIE, 0, "/");
	}

	/**
	 * 用户登录,保存票到web和服务端
	 * 
	 * @param request
	 * @param response
	 * @param userId
	 * @return
	 */
	public boolean loginWeb(HttpServletRequest request,
			HttpServletResponse response, int userId) {
		String t = getLoginCookie(userId);
		saveLoginCookieToWeb(response, t);
		return true;
	}

	/**
	 * 通过_l票得到user对象
	 * 
	 * @param request
	 * @return
	 */
	public User identifyUser(HttpServletRequest request,
			HttpServletResponse response) {
		// 先看get参数,如果有get参数则写到cookie中
		String loginCookie = request.getParameter(LOGIN_COOKIE_REQUEST);
		if (!StringUtils.isBlank(loginCookie)) {
			saveLoginCookieToWeb(response, loginCookie);
		}
		// 从cookie中读取
		if (StringUtils.isBlank(loginCookie)) {
			loginCookie = CookieManager.getInstance().getCookie(request,
					LOGIN_COOKIE);
		}
		if (StringUtils.isBlank(loginCookie)) {
			return null;
		}
		// 得到userId
		UserLoginCookie userLoginCookie = getUserLoginCookieByCookie(loginCookie);
		if (userLoginCookie == null) {
			return null;
		}
		// 判断是否过期
		if (CommonUtil.now() > userLoginCookie.getDeadline()) {
			return null;
		}
		User user = userService.getUser(userLoginCookie.getUserId());
		return user;
	}

	/**************** 注册相关 ******************/
	/**
	 * 验证账号注册信息
	 * 
	 * @param account
	 * @return
	 */
	public List<String> validate(String account) {
		List<String> errors = new ArrayList<String>();
		// 账号长度
		if (account.length() > 40) {
			errors.add("账号不超过40字符");
		}
		if (CommonUtil.isTelePhone(account)) {
			UserTelephone userTelephone = getUserTelephone(account);
			if (userTelephone != null) {
				errors.add("手机号已经被注册");
			}
		} else if (CommonUtil.isEmail(account)) {
			UserEmail userEmail = getUserEmail(account);
			if (userEmail != null) {
				errors.add("邮箱已经被注册");
			}
		} else {
			User user = userService.getUserByAccount(account);
			if (user != null) {
				errors.add("账号已经被注册");
			}
		}
		return errors;
	}

	/**
	 * 验证账号注册信息
	 * 
	 * @param account
	 * @return
	 */
	public List<String> validatePhoneRegister(String name, String phone,
			String sex, String code, String password_md5) {
		List<String> errors = new ArrayList<String>();
		if (userService.getUserByName(name) != null) {
			errors.add("名字已经被占用");
		}
		if (!CommonUtil.isTelePhone(phone)) {
			errors.add("手机号格式有误");
		}
		if (getUserByTelephone(phone) != null) {
			errors.add("手机号已经被占用");
		}
		if (!UserDetail.isSexValidate(sex)) {
			errors.add("性别参数错误");
		}
		if (StringUtils.isBlank(code)
				|| code.length() != RegisterMessageService.verifyCodeLength) {
			errors.add("验证码长度有误");
		}
		String cacheCode = RedisAPI.get(
				KVConf.getPhoneRegisterKey(phone));
		if (StringUtils.isBlank(cacheCode) || cacheCode.indexOf(code) == -1) {
			errors.add("验证码不正确,请重新输入");
		}
		if (StringUtils.isBlank(password_md5) || password_md5.length() > 40) {
			errors.add("密码格式有误，请重新输入");
		}
		return errors;
	}

	/**
	 * 用户注册，创建user,用于账号，邮箱，手机号注册,自动识别
	 * 
	 * @param account
	 * @param password
	 * @return 返回注册用户
	 */
	public User createUser(String account, String password, String name) {
		if (CommonUtil.isTelePhone(account)) {
			return createTeleteUser(account, password, name);
		} else if (CommonUtil.isEmail(account)) {
			return createEmailteUser(account, password, name);
		}

		return createAccountUser(account, password, name);
	}

	/**
	 * 创建账号用户
	 * 
	 * @param account
	 * @param password
	 * @return 返回注册用户
	 */
	public User createAccountUser(String account, String password, String name) {
		User exist = userService.getUserByAccount(account);
		if (exist != null) {
			return null;
		}
		User user = new User(account, password, name);
		userService.addUser(user);
		return user;
	}

	/**
	 * 用户注册，仅仅限于手机号注册
	 * 
	 * @param telephone
	 * @param password
	 * @return 返回注册用户
	 */
	public User createTeleteUser(String telephone, String password, String name) {
		if (!CommonUtil.isTelePhone(telephone)) {
			return null;
		}
		UserTelephone mapping = getUserTelephone(telephone);
		if (mapping != null) {
			logger.error("tel:" + telephone + "  exists");
			return null;
		}
		// 生成user
		// 默认account是手机号，被占则随机生成
		String userAccount = telephone;
		if (userService.getUserByAccount(telephone) != null) {
			userAccount = "tel_" + MD5Util.md5("" + CommonUtil.now());
		}
		User user = new User(userAccount, password, name);
		UserDetail userDetail = new UserDetail();
		userDetail.setTel(telephone);
		user.setUserDetail(userDetail);
		userService.addUser(user);
		// 映射user-telephone
		UserTelephone userTelephone = new UserTelephone(user.getId(), telephone);
		addUserTelephone(userTelephone);
		return user;
	}

	/**
	 * 用户注册，仅仅限于邮箱注册
	 * 
	 * @param telephone
	 * @param password
	 * @return 返回注册用户
	 */
	public User createEmailteUser(String email, String password, String name) {
		if (!CommonUtil.isEmail(email)) {
			return null;
		}
		UserEmail mapping = getUserEmail(email);
		if (mapping != null) {
			logger.error("email:" + email + "  exists");
			return null;
		}
		// 生成user
		// 默认account是手机号，被占则随机生成
		String userAccount = email;
		if (userService.getUserByAccount(email) != null) {
			userAccount = "email_" + MD5Util.md5("" + CommonUtil.now());
		}
		User user = new User(userAccount, password, name);
		UserDetail userDetail = new UserDetail();
		userDetail.setEmail(email);
		user.setUserDetail(userDetail);
		userService.addUser(user);
		// 映射user-email
		UserEmail userEmail = new UserEmail(user.getId(), email);
		addUserEmail(userEmail);
		return user;
	}

	/**
	 * 通过account得到user，account会自动识别电话，邮箱和普通账号
	 * 
	 * @param account
	 * @return
	 */
	public User getUserByAccount(String account) {
		if (StringUtils.isBlank(account)) {
			return null;
		}
		if (CommonUtil.isTelePhone(account)) {
			return getUserByTelephone(account);
		} else if (CommonUtil.isEmail(account)) {
			return getUserByEmail(account);
		}
		return userService.getUserByAccount(account);
	}

	/**
	 * 通过手机号得到user对象
	 * 
	 * @param telephone
	 * @return
	 */
	public User getUserByTelephone(String telephone) {
		if (StringUtils.isBlank(telephone)) {
			return null;
		}
		UserTelephone userTelephone = getUserTelephone(telephone);
		if (userTelephone == null) {
			return null;
		}
		User user = userService.getUser(userTelephone.getUserId());
		return user;
	}

	/**
	 * 通过邮箱得到user对象
	 * 
	 * @param email
	 * @return
	 */
	public User getUserByEmail(String email) {
		if (StringUtils.isBlank(email)) {
			return null;
		}
		UserEmail userEmail = getUserEmail(email);
		if (userEmail == null) {
			return null;
		}
		User user = userService.getUser(userEmail.getUserId());
		return user;
	}
}
