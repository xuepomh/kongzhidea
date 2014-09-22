package com.renren.shopping.conf;

public class OrdersConf {
	
	public static final String ORDERS_ID  = "orders_id";
	public static final String ORDERSKEY = "orders_info";
	public static final String ORDER_NUMBER = "order_number";
	public static final String RECEIVER_ID = "receiver_id";
	public static final String BUYER_ID = "buyer_id";
	public static final String OWNER_ID = "owner_id";
	public static final String TASK_ID = "task_id";
	public static final String TOTAL_MOENY = "total_money";
	public static final String UPDATE_TIME = "update_time";
	public static final String SCORE = "score";
	public static final String COMMENTS = "comments";
	public static final String ORDER_STATUS = "order_status";
	public static final String CREATE_TIME = "create_time";
	public static final String LEAVE_MESSAGE = "leave_message";
	public static final String TITLE = "title" ; //任务title
	public static final String ADDRESSID = "addressId";
	public static final String MONEY = "money";//任务金额
	public static final String GRATUITY = "gratuity"; //跑腿费比例
	public static final String MESSAGE = "message"; //任务描述
	
	/** 卖家待确认 */
	public static final int BUYERACK_STATUS = 0;
	/** 买家待付款*/
	public static final int RECEIVERPAY_STATUS = 1;
	/** 采购中 */
	public static final int PURCHASING_STATUS = 2;
	/** 采购完成 */
	public static final int PURCHASED_STATUS = 3;
	/** 等待买家确认收货 */
	public static final int RECEIVERRECEIV_STATUS = 4;
	/** 待评价 */
	public static final int WAITING_COMMENTS = 5;
	/** 订单完成 */
	public static final int FINISHED_STATUS = 6;
}
