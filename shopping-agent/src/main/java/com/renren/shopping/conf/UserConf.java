package com.renren.shopping.conf;

import com.renren.shopping.model.User;

import net.sf.json.JSONObject;

public class UserConf {
	public static final String USERKEY = "user_info";
	public static final String USER_ID = "user_id";
	public static final String USER_NAME = "user_name";
	public static final String ORIGINAL_URL = "original_url";
	public static final String MAIN_URL = "main_url";
	public static final String HEAD_URL = "head_url";
	public static final String TINY_URL = "tiny_url";
	public static final String USER_DESCRIPTION = "user_desc";
	public static final String USER_SEX = "user_sex";
	public static final String USER_ACCOUNT = "account";

	/** 加v认证 */
	public static final String USER_ISV = "user_v";
	/** 星级 */
	public static final String USER_STAR = "user_star";

	/** 关注,关注则是1，未关注则是0 */
	public static final String USER_ISFOLLOW = "user_is_follow";
	/** 当前用户 */
	public static final String CURRENT_USER_ID = "user_current_id";

	/** 手机号 */
	public static final String USER_PHONE = "user_phone";
	/** 邮箱 */
	public static final String USER_EMAIL = "user_email";

	/** 手机访问 带的登录信息 */
	public static final String LOGIN_USER_ID = "user_id";
	public static final String LOGIN_USER_SESSION_KEY = "session_key";

	public static JSONObject getLoginResult(int id, String session_key ) {
		JSONObject obj = new JSONObject();
		obj.put(ResultConf.ID, id);
		obj.put(LOGIN_USER_SESSION_KEY, session_key);
		return obj;
	}
}
