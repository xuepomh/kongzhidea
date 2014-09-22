package com.renren.shopping.conf;

import org.springframework.web.servlet.ModelAndView;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ResultConf {
	public static final String CODE = "code";
	public static final String MSG = "msg";
	public static final String HASMORE = "hasmore";
	public static final String SIZE = "size";
	public static final String DATA = "data";
	public static final String ID = "id";

	/** 空json */
	public static final JSONObject NULL = new JSONObject();
	/** 成功 */
	public static final int SUCCESSCODE = 0;
	private static final String SUCCESSMSG = "success";
	public static final JSONObject SUCCESSRET = new JSONObject();

	/** 参数有误code */
	public static final int PARAM_EXCEPTIONCODE = 104;
	public static final JSONObject PARAM_EXCEPTION = new JSONObject();
	/** 系统异常code */
	public static final int SYSTEM_EXCEPTIONCODE = 105;
	public static final JSONObject SYSTEM_EXCEPTION = new JSONObject();
	/** 用户不存在code */
	public static final int USER_NOTEXIST_EXCEPTIONCODE = 100;
	public static final JSONObject USER_NOTEXIST_EXCEPTION = new JSONObject();
	/** 密码错误code */
	public static final int PASSWORD_ERROR_EXCEPTIONCODE = 101;
	public static final JSONObject PASSWORD_ERROR_EXCEPTION = new JSONObject();
	/** 性别错误code */
	public static final int USER_SEX_EXCEPTIONCODE = 110;
	public static final JSONObject USER_SEX_EXCEPTION = new JSONObject();
	/** 注册用户已存在code */
	public static final int USER_EXIST_EXCEPTIONCODE = 120;
	public static final JSONObject USER_EXIST_EXCEPTION = new JSONObject();
	/** 输入字数过多code */
	public static final int DESC_OVERFLOW_EXCEPTIONCODE = 155;
	public static final JSONObject DESC_OVERFLOW_EXCEPTION = new JSONObject();
	/** 输入为空  */
	public static final int DESC_EMPTY_EXCEPTIONCODE = 156;
	public static final JSONObject DESC_EMPTY_EXCEPTION = new JSONObject();
	
	/** 资源不存在code */
	public static final int SOURCE_NOTEXIST_EXCEPTIONCODE = 130;
	public static final JSONObject SOURCE_NOTEXIST_EXCEPTION = new JSONObject();

	/** 验证失败code */
	public static final int VERIFY_FAIL_EXCEPTIONCODE = 133;
	public static final JSONObject VERIFY_FAIL_EXCEPTION = new JSONObject();
	/** 手机端 用户未登录code */
	public static final int MOBILE_NOLOGIN_EXCEPTIONCODE = 140;
	public static final JSONObject MOBILE_NOLOGIN_EXCEPTION = new JSONObject();
	public static final ModelAndView MOBILE_NOLOGIN_MAV = new ModelAndView(
			"jsonView");

	static {

		PARAM_EXCEPTION.put(CODE, PARAM_EXCEPTIONCODE);
		PARAM_EXCEPTION.put(MSG, "参数有误");

		SYSTEM_EXCEPTION.put(CODE, SYSTEM_EXCEPTIONCODE);
		SYSTEM_EXCEPTION.put(MSG, "系统异常");

		SUCCESSRET.put(CODE, SUCCESSCODE);
		SUCCESSRET.put(MSG, SUCCESSMSG);

		USER_NOTEXIST_EXCEPTION.put(CODE, USER_NOTEXIST_EXCEPTIONCODE);
		USER_NOTEXIST_EXCEPTION.put(MSG, "用户不存在");

		PASSWORD_ERROR_EXCEPTION.put(CODE, PASSWORD_ERROR_EXCEPTIONCODE);
		PASSWORD_ERROR_EXCEPTION.put(MSG, "密码错误");

		USER_SEX_EXCEPTION.put(CODE, USER_SEX_EXCEPTIONCODE);
		USER_SEX_EXCEPTION.put(MSG, "性别错误");

		USER_EXIST_EXCEPTION.put(CODE, USER_EXIST_EXCEPTIONCODE);
		USER_EXIST_EXCEPTION.put(MSG, "用户已存在");

		DESC_OVERFLOW_EXCEPTION.put(CODE, DESC_OVERFLOW_EXCEPTIONCODE);
		DESC_OVERFLOW_EXCEPTION.put(MSG, "不能超过160字");
		
		DESC_EMPTY_EXCEPTION.put(CODE, DESC_EMPTY_EXCEPTIONCODE);
		DESC_EMPTY_EXCEPTION.put(MSG, "输入为空");
		
		SOURCE_NOTEXIST_EXCEPTION.put(CODE, SOURCE_NOTEXIST_EXCEPTIONCODE);
		SOURCE_NOTEXIST_EXCEPTION.put(MSG, "资源不存在");

		VERIFY_FAIL_EXCEPTION.put(CODE, VERIFY_FAIL_EXCEPTIONCODE);
		VERIFY_FAIL_EXCEPTION.put(MSG, "验证码错误");

		MOBILE_NOLOGIN_EXCEPTION.put(CODE, MOBILE_NOLOGIN_EXCEPTIONCODE);
		MOBILE_NOLOGIN_EXCEPTION.put(MSG, "用户未登录");

		MOBILE_NOLOGIN_MAV.addObject(CODE, MOBILE_NOLOGIN_EXCEPTIONCODE);
		MOBILE_NOLOGIN_MAV.addObject(MSG, "用户未登录");
		
		
	}

	/**
	 * 标准返回格式
	 * 
	 * @param code
	 * @param msg
	 * @return
	 */
	public static JSONObject getResult(int code, String msg) {
		JSONObject obj = new JSONObject();
		obj.put(CODE, code);
		obj.put(MSG, msg);
		return obj;
	}

	/**
	 * 判断标准返回中 code是否为0:成功
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isSuccess(JSONObject obj) {
		return obj.optInt(ResultConf.CODE) == ResultConf.SUCCESSCODE;
	}

	/**
	 * 得到一个json对象
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public static JSONObject getJsonObject(Object key, Object value) {
		JSONObject ret = new JSONObject();
		ret.put(key, value);
		return ret;
	}

	/**
	 * 添加信息成功后 返回带的id
	 * 
	 * @param id
	 * @return
	 */
	public static JSONObject getSuccessResult(long id) {
		JSONObject obj = JSONObject.fromObject(SUCCESSRET);
		JSONObject ret = getJsonObject(ID, id);
		obj.put(DATA, ret);
		return obj;
	}

	/**
	 * 取具体信息后 返回客户端格式
	 * 
	 * @param obj
	 * @return
	 */
	public static JSONObject getSuccessResult(JSONObject obj) {
		if (obj == null || obj == NULL || obj.keySet().size() == 0) {
			return SOURCE_NOTEXIST_EXCEPTION;
		}
		JSONObject ret = JSONObject.fromObject(SUCCESSRET);
		ret.put(DATA, obj);
		return ret;
	}

	/**
	 * 取具体信息后 返回客户端格式
	 * 
	 * @param obj
	 * @return
	 */
	public static JSONObject getSuccessResult(JSONArray jar) {
		if (jar == null || jar.size() == 0) {
			return SOURCE_NOTEXIST_EXCEPTION;
		}
		JSONObject ret = JSONObject.fromObject(SUCCESSRET);
		ret.put(DATA, jar);
		return ret;
	}
	
	/**
	 * 取具体信息后 返回客户端格式
	 * 
	 * @param jar
	 *            如果是null则返回不存在，如果size是0则正常返回
	 * @param limit
	 *            用户得到hasmore参数，如果当前数据长度小于limit,则说明是最后一页
	 * @return
	 */
	public static JSONObject getSuccessResult(JSONArray jar, int limit) {
		if (jar == null) {
			return SOURCE_NOTEXIST_EXCEPTION;
		}
		JSONObject ret = JSONObject.fromObject(SUCCESSRET);
		int hasmore = 1;
		if (limit > jar.size()) {
			hasmore = 0;
		}
		ret.put(HASMORE, hasmore);
		ret.put(SIZE, jar.size());
		ret.put(DATA, jar);
		return ret;
	}

	/**
	 * 取具体信息后 返回客户端格式
	 * 
	 * @param jar
	 *            如果是null则返回不存在，如果size是0则正常返回
	 * @return
	 */
	public static JSONObject getSuccessResult2(JSONArray jar) {
		if (jar == null) {
			return SOURCE_NOTEXIST_EXCEPTION;
		}
		JSONObject ret = JSONObject.fromObject(SUCCESSRET);
		ret.put(SIZE, jar.size());
		ret.put(DATA, jar);
		return ret;
	}

	/**
	 * 得到一个自定义返回值类型
	 * 
	 * @param code
	 * @param msg
	 * @return
	 */
	public static JSONObject getResultType(int code, String msg) {
		JSONObject ret = new JSONObject();
		ret.put(CODE, code);
		ret.put(MSG, msg);
		return ret;
	}

}
