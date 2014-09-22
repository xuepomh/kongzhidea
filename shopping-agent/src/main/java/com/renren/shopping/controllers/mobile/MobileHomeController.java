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

import com.renren.shopping.conf.GoodConf;
import com.renren.shopping.conf.KVConf;
import com.renren.shopping.conf.ResultConf;
import com.renren.shopping.conf.TaskConf;
import com.renren.shopping.conf.TravelConf;
import com.renren.shopping.conf.UserConf;
import com.renren.shopping.model.AddressUser;
import com.renren.shopping.model.CollectionPhoto;
import com.renren.shopping.model.Task;
import com.renren.shopping.model.Travel;
import com.renren.shopping.model.User;
import com.renren.shopping.model.UserDetail;
import com.renren.shopping.model.UserFollows;
import com.renren.shopping.model.UserFollowsPK;
import com.renren.shopping.model.UserTelephone;
import com.renren.shopping.services.AddressService;
import com.renren.shopping.services.GoodService;
import com.renren.shopping.services.PasswordMessageService;
import com.renren.shopping.services.RegisterMessageService;
import com.renren.shopping.services.TaskService;
import com.renren.shopping.services.TravelService;
import com.renren.shopping.services.UserLoginService;
import com.renren.shopping.services.UserService;
import com.renren.shopping.util.CommonUtil;
import com.renren.shopping.util.MD5Util;
import com.renren.shopping.util.RedisAPI;
import com.renren.shopping.util.RedisUtil;

/**
 * 登录相关
 * 
 * @author kk
 * 
 */
@Controller
@RequestMapping("/mobile/home")
public class MobileHomeController {

	@Autowired
	UserService userService;

	@Autowired
	UserLoginService userLoginService;

	@Autowired
	PasswordMessageService passwordMessageService;

	public Log logger = LogFactory.getLog(MobileHomeController.class);

	/**
	 * 手机端登录
	 * 
	 * @param account
	 * @param password_md5
	 * @return
	 */
	@RequestMapping(value = "/login", method = { RequestMethod.POST })
	@ResponseBody
	public Object login(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam("account") String account,
			@RequestParam("password_md5") String password_md5) {
		User user = userLoginService.getUserByAccount(account);
		if (user == null) {
			return ResultConf.USER_NOTEXIST_EXCEPTION;
		}
		if (!user.getPassword_md5().equals(password_md5)) {
			return ResultConf.PASSWORD_ERROR_EXCEPTION;
		}
		String session_key = userLoginService.getLoginCookie(user.getId());
		JSONObject data = UserConf.getLoginResult(user.getId(), session_key);
		JSONObject json = new JSONObject();
		userService.parsedUserInfo(json , user.getId(), user);
		JSONObject userInfo = json.optJSONObject(UserConf.USERKEY);
		data.put(UserConf.USERKEY, userInfo);
		return ResultConf.getSuccessResult(data);
	}

	/**
	 * 手机端找回密码
	 * 
	 * @param phone
	 * @return
	 */
	@RequestMapping(value = "/findPasswordVerifiCode", method = { RequestMethod.POST })
	@ResponseBody
	public Object findPasswordVerifiCode(HttpServletRequest request,
			HttpServletResponse response, @RequestParam("phone") String phone,
			@RequestParam("time") long time, @RequestParam("sign") String sign) {
		// 验证 TODO 暂时不验证
		// long now = CommonUtil.now();
		// if (now - time >= 1000 * 10 || StringUtils.isBlank(phone)) {
		// return ResultConf.PARAMEXCEPTION;
		// }
		// String signature = MD5Util.md5(phone + ":" + time);
		// if (!signature.equals(sign)) {
		// return ResultConf.PARAMEXCEPTION;
		// }

		// 判断1分钟内是否发过短信
		String val = RedisAPI.get(
				KVConf.getPhonePasswordTimeKey(phone));

		if (KVConf.PHONE_PASSWORD_TIME_VALUE.equals(val)) {
			return ResultConf.getResultType(2, "1分钟内已经发送过短信了");
		}

		// 判断该用户是否存在
		if (userLoginService.getUserByTelephone(phone) == null) {
			return ResultConf.getResultType(3, "改号码还没有注册过");
		}

		boolean ret = passwordMessageService.sendMessageVerify(phone);
		if (ret) {
			return ResultConf.SUCCESSRET;
		} else {
			return ResultConf.SYSTEM_EXCEPTION;
		}
	}

	/**
	 * 手机号 找回密码
	 * 
	 * @param phone
	 * @param code
	 * @return
	 */
	@RequestMapping(value = "/setTelePassword", method = { RequestMethod.POST })
	@ResponseBody
	public Object setTelePassword(HttpServletRequest request,
			HttpServletResponse response, @RequestParam("phone") String phone,
			@RequestParam("code") String code,
			@RequestParam("password_md5") String password_md5) {
		if (StringUtils.isBlank(code)
				|| code.length() != RegisterMessageService.verifyCodeLength) {
			return ResultConf.VERIFY_FAIL_EXCEPTION;
		}

		// 判断改手机号是否已经注册过
		User user = userLoginService.getUserByTelephone(phone);
		if (user == null) {
			return ResultConf.USER_NOTEXIST_EXCEPTION;
		}
		String cacheCode = RedisAPI.get(
				KVConf.getPhonePasswordKey(phone));
		if (StringUtils.isBlank(cacheCode) || cacheCode.indexOf(code) == -1) {
			return ResultConf.VERIFY_FAIL_EXCEPTION;
		}
		user.setPassword_md5(password_md5);
		userService.updateUser(user);
		// 清空缓存的验证码
		RedisAPI.delete(KVConf.getPhonePasswordKey(phone));
		return ResultConf.getSuccessResult(user.getId());
	}
}
