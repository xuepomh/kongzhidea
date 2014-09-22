package com.renren.shopping.conf;

public class KVConf {
	/*****************************************************/
	/** 任务 热搜词,list */
	public static final String HOT_SEARCH_WORD_KEY = "HOT:SEARCH:WORD:KEY";
	/** 任务跑腿费排序,zset */
	public static final String TASK_GRATUITY_SORT = "TASK:GRATUITY:SORT";

	/*****************************************************/
	/** 注册短信验证码 KEY+phone,存储发送验证码,用逗号隔开 */
	private static final String PHONE_REGISTER_VERIFY_CODE = "AGENT:REGISTER:";
	public static final int REGISTER_CODE_TIME = 60 * 30;// 30min

	/** 注册短信验证码 KEY+phone,存储发送验证码 ,用逗号隔开 */
	public static String getPhoneRegisterKey(String phone) {
		return PHONE_REGISTER_VERIFY_CODE + phone;
	}

	/** 注册短信验证码 KEY+phone,控制发送频率 */
	private static final String PHONE_REGISTER_VERIFY_TIME = "AGENT:REGISTER:TIME:";
	public static final int REGISTER_TIME = 60;// 60s
	public static final String PHONE_REGISTER_TIME_VALUE = "1";

	/** 注册短信验证码 KEY+phone,控制发送频率 */
	public static String getPhoneRegisterTimeKey(String phone) {
		return PHONE_REGISTER_VERIFY_TIME + phone;
	}

	/*****************************************************/
	/** 找回密码短信验证码 KEY+phone,存储发送验证码,用逗号隔开 */
	private static final String PHONE_PASSWORD_VERIFY_CODE = "AGENT:PASSWORD:";
	public static final int PASSWORD_CODE_TIME = 60 * 30;// 30min

	/** 找回密码短信验证码 KEY+phone,存储发送验证码 ,用逗号隔开 */
	public static String getPhonePasswordKey(String phone) {
		return PHONE_PASSWORD_VERIFY_CODE + phone;
	}

	/** 找回密码短信验证码 KEY+phone,控制发送频率 */
	private static final String PHONE_PASSWORD_VERIFY_TIME = "AGENT:PASSWORD:TIME:";
	public static final int PASSWORD_TIME = 60;// 60s
	public static final String PHONE_PASSWORD_TIME_VALUE = "1";

	/** 找回密码短信验证码 KEY+phone,控制发送频率 */
	public static String getPhonePasswordTimeKey(String phone) {
		return PHONE_PASSWORD_VERIFY_TIME + phone;
	}
	/*****************************************************/
}
