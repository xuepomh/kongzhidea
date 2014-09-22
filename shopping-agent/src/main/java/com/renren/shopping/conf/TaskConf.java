package com.renren.shopping.conf;

public class TaskConf {
	public static final String TASKKEY = "task_info";
	public static final String TASK_ID = "task_id";
	public static final String TASK_TITLE = "task_title";
	/** 跑腿费 */
	public static final String TASK_GRATUITY = "task_gratuity";
	/** 截止时间 */
	public static final String TASK_DEADLINE = "task_deadline";
	public static final String TASK_MESSAGE = "task_message";
	/** 任务希望实现的国家 */
	public static final String TASK_GOOD_COUNTRY = "task_good_country";
	/** 金额上限 */
	public static final String TASK_MONEY = "task_money";
	/** 商品个数 */
	public static final String TASK_GOOD_NUMBER = "task_good_number";
	/** 居住城市 */
	public static final String TASK_LIVE_CITY = "task_live_city";
	/** 备注 */
	public static final String TASK_REMARK = "task_remark";
	/** 任务状态 */
	public static final String TASK_STATUS = "task_status" ;
	public static final int TASK_WAITING = 0 ;
	public static final int TASK_TAKE_AWAY = 1;
	public static final int TASK_EXPIRED = 2;
	/** 任务列表每次加载默认条数 */
	public static final int TASK_LOAD_DEFAULTNUM = 20;

	/** 搜索推荐词默认条数 */
	public static final int TASK_SUGGEST_DEFAULTNUM = 10;

}
