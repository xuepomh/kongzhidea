package com.renren.shopping.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CommonUtil {

	private static final Log logger = LogFactory.getLog(CommonUtil.class);

	/**
	 * 将list转成string，用逗号隔开
	 * 
	 * @param ids
	 * @return
	 */
	public static String longListToString(List<Long> ids) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < ids.size(); i++) {
			if (i > 0) {
				sb.append(",");
			}
			sb.append(ids.get(i));
		}
		return sb.toString();
	}

	/**
	 * 将list转成string，用分号隔开
	 * 
	 * @param strs
	 * @return
	 */
	public static String stringListToString(List<String> strs) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < strs.size(); i++) {
			if (i > 0) {
				sb.append(";");
			}
			sb.append(strs.get(i));
		}
		return sb.toString();
	}

	/**
	 * 将string类型的list转换为integer类型的list
	 * 
	 * @param list
	 * @return
	 */
	public static List<Integer> getIntegerList(List<String> list) {
		List<Integer> ret = new ArrayList<Integer>();
		for (String str : list) {
			try {
				int num = Integer.valueOf(str);
				ret.add(num);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		return ret;
	}

	/**
	 * 将string类型的set转换为integer类型的list
	 * 
	 * @param set
	 * @return
	 */
	public static List<Integer> getIntegerList(Set<String> set) {
		List<Integer> ret = new ArrayList<Integer>();
		for (String str : set) {
			try {
				int num = Integer.valueOf(str);
				ret.add(num);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		return ret;
	}

	/**
	 * 将string类型的list转换为long类型的list
	 * 
	 * @param list
	 * @return
	 */
	public static List<Long> getLongList(List<String> list) {
		List<Long> ret = new ArrayList<Long>();
		for (String str : list) {
			try {
				long num = Long.valueOf(str);
				ret.add(num);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		return ret;
	}

	/**
	 * 将string类型的set转换为long类型的list
	 * 
	 * @param set
	 * @return
	 */
	public static List<Long> getLongList(Set<String> set) {
		List<Long> ret = new ArrayList<Long>();
		for (String str : set) {
			try {
				long num = Long.valueOf(str);
				ret.add(num);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		return ret;
	}

	public static int getIntegerCompare0(double x) {
		if (x > 0) {
			return 1;
		} else if (x < 0) {
			return -1;
		}
		return 0;
	}

	public static int getIntegerCompare0(long x) {
		if (x > 0) {
			return 1;
		} else if (x < 0) {
			return -1;
		}
		return 0;
	}

	/**
	 * 将数组转成list
	 * 
	 * @param objs
	 * @return
	 */
	public static List<String> toListString(Object... objs) {
		List<String> list = new ArrayList<String>();
		for (Object s : objs) {
			list.add(s.toString());
		}
		return list;
	}

	/**
	 * 当前时间unix时间戳,精确到ms
	 * 
	 * @return
	 */
	public static long now() {
		Date date = new Date();
		return date.getTime();
	}

	/**
	 * 判断是否为手机号
	 * 
	 * @param telephone
	 * @return
	 */
	public static boolean isTelePhone(String telephone) {
		if (StringUtils.isBlank(telephone)) {
			return false;
		}
		if (telephone.length() != 11) {
			return false;
		}
		Pattern pattern = Pattern.compile("^[1]{1}[0-9]{10}");
		if (!pattern.matcher(telephone).matches()) {
			return false;
		}
		return true;
	}

	/**
	 * 判断是否是邮箱
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		if (StringUtils.isBlank(email)) {
			return false;
		}

		if (email.length() > 32) {
			return false;
		}

		boolean isValid = true;
		email = email.trim();
		// Check at-sign and white-space usage
		int atSign = email.indexOf('@');
		if (atSign == -1 || atSign == 0 || atSign == email.length() - 1
				|| email.indexOf('@', atSign + 1) != -1
				|| email.indexOf(' ') != -1 || email.indexOf('\t') != -1
				|| email.indexOf('\n') != -1 || email.indexOf('\r') != -1) {
			isValid = false;
		}
		// Check dot usage
		if (isValid) {
			email = email.substring(atSign + 1);
			int dot = email.indexOf('.');
			if (dot == -1 || dot == 0 || dot == email.length() - 1) {
				isValid = false;
			}
		}
		return isValid;
	}
}
