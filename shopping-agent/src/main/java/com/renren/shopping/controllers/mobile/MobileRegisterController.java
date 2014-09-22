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
 * 注册相关
 * 
 * @author kk
 * 
 */
@Controller
@RequestMapping("/mobile/register")
public class MobileRegisterController {

	@Autowired
	UserService userService;

	@Autowired
	UserLoginService userLoginService;

	@Autowired
	RegisterMessageService messageService;

	public Log logger = LogFactory.getLog(MobileRegisterController.class);

	/**
	 * 发送短信验证码
	 * 
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/verifyCode", method = { RequestMethod.POST })
	@ResponseBody
	public Object verifyCode(HttpServletRequest request,
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
				KVConf.getPhoneRegisterTimeKey(phone));

		if (KVConf.PHONE_REGISTER_TIME_VALUE.equals(val)) {
			return ResultConf.getResultType(2, "1分钟内已经发送过短信了");
		}

		// 判断该手机号是否已经注册过
		if (userLoginService.getUserByTelephone(phone) != null) {
			return ResultConf.getResultType(3, "该号码已经注册过");
		}

		boolean ret = messageService.sendMessageVerify(phone);
		if (ret) {
			return ResultConf.SUCCESSRET;
		} else {
			return ResultConf.SYSTEM_EXCEPTION;
		}
	}

	/**
	 * 检测userName是否被占用
	 * 
	 * @param name
	 * @return 返回code是1表示被占用，返回0表示没有被占用
	 */
	@RequestMapping(value = "/isUserNameExists", method = { RequestMethod.POST })
	@ResponseBody
	public Object isUserNameExists(HttpServletRequest request,
			HttpServletResponse response, @RequestParam("name") String name) {
		User user = userService.getUserByName(name);
		if (user == null) {
			return ResultConf.getResultType(0, "没有被占用");
		}
		return ResultConf.getResultType(1, name + "已被占用");
	}

	/**
	 * 检测手机号是否被占用
	 * 
	 * @param phone
	 * @return 返回code是1表示被占用，返回0表示没有被占用
	 */
	@RequestMapping(value = "/isPhoneExists", method = { RequestMethod.POST })
	@ResponseBody
	public Object isPhoneExists(HttpServletRequest request,
			HttpServletResponse response, @RequestParam("phone") String phone) {
		UserTelephone userTele = userLoginService.getUserTelephone(phone);
		if (userTele == null) {
			return ResultConf.getResultType(0, "没有被占用");
		}
		return ResultConf.getResultType(1, phone + "已被占用");
	}

	/**
	 * 手机号注册
	 * 
	 * @param name
	 * @param sex
	 * @param phone
	 * @param code
	 * @param password_md5
	 * @return
	 */
	@RequestMapping(value = "/register", method = { RequestMethod.POST })
	@ResponseBody
	public Object register(HttpServletRequest request,
			HttpServletResponse response, @RequestParam("name") String name,
			@RequestParam("sex") String sex,
			@RequestParam("phone") String phone,
			@RequestParam("code") String code,
			@RequestParam("password_md5") String password_md5) {
		List<String> errors = userLoginService.validatePhoneRegister(name,
				phone, sex, code, password_md5);
		if (errors.size() > 0) {
			return ResultConf.getResultType(1,
					CommonUtil.stringListToString(errors));
		}
		User user = userLoginService
				.createTeleteUser(phone, password_md5, name);
		if (user == null) {
			return ResultConf.SYSTEM_EXCEPTION;
		}
		UserDetail userDetail = user.getUserDetail();
		userDetail.setSex(sex);
		user.setUserDetail(userDetail);
		userService.updateUser(user);

		String session_key = userLoginService.getLoginCookie(user.getId());

		return ResultConf.getSuccessResult(UserConf.getLoginResult(
				user.getId(), session_key));
	}
}
