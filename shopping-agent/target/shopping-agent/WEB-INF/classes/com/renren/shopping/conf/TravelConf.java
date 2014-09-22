package com.renren.shopping.conf;

import java.util.HashMap;
import java.util.Map;

public class TravelConf {
	public static final String TRAVELKEY = "travel_info";
	public static final String TRAVEL_ID = "travel_id";
	public static final String TRAVEL_DESCRIPTION = "travel_desc";
	public static final String TRAVEL_START_TIME = "travel_start_time";
	public static final String TRAVEL_BACK_TIME = "travel_back_time";
	/** 出发城市 */
	public static final String FROM = "from";
	/** 去往国家 */
	public static final String TO = "to";

	/** 根据回国时间来筛选 */
	public static final long DAY_UNIX_TIME = 1000l * 60 * 60 * 24;// 一天的时间戳
	public static Map<Integer, Integer> back_days = new HashMap<Integer, Integer>();

	/** 买手行程页中展示评论条数 */
	public static final int TRAVEL_SHOW_COMMENTNUM = 5;
	/** 买手列表每次加载默认条数 */
	public static final int TRAVEL_LOAD_DEFAULTNUM = 20;
	static {
		// key:type;value:day number
		back_days.put(1, 0);// 常驻
		back_days.put(2, 3);// 3天内
		back_days.put(3, 7);// 一周内
		back_days.put(4, 14);// 两周内
		back_days.put(5, 30);// 一个月内
	}

}
