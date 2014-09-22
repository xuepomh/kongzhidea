package com.renren.shopping.controllers.mobile;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.renren.shopping.annotion.MobileLoginRequired;
import com.renren.shopping.conf.GoodConf;
import com.renren.shopping.conf.OrdersConf;
import com.renren.shopping.conf.ResultConf;
import com.renren.shopping.conf.TaskConf;
import com.renren.shopping.model.AddressUser;
import com.renren.shopping.model.Good;
import com.renren.shopping.model.GoodPhoto;
import com.renren.shopping.model.LocateAddress;
import com.renren.shopping.model.Logistics;
import com.renren.shopping.model.Orders;
import com.renren.shopping.model.Task;
import com.renren.shopping.model.User;
import com.renren.shopping.services.AddressService;
import com.renren.shopping.services.CommentService;
import com.renren.shopping.services.GoodService;
import com.renren.shopping.services.LocateAddressService;
import com.renren.shopping.services.LogisticsService;
import com.renren.shopping.services.OrdersService;
import com.renren.shopping.services.TaskService;
import com.renren.shopping.services.UserService;
import com.renren.shopping.util.KeysGenerator;

/**
 * 用户订单相关
 * @author yu.zhuang
 *
 */
@Controller
@RequestMapping("/mobile/order")
public class MobileOrdersController {
	
	@Autowired
	LocateAddressService laService;
	
	@Autowired
	OrdersService ordersService;
	
	@Autowired
	TaskService taskService;
	
	@Autowired
	GoodService goodService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	AddressService addressService;
	
	@Autowired
	CommentService commentService;
	
	@Autowired
	LogisticsService logisticsService;
	
	public static Log logger = LogFactory.getLog(MobileOrdersController.class);

	/**
	 * 进行定位
	 * @param order_id
	 * @return 
	 */
	@MobileLoginRequired
	@RequestMapping(value = "/location", method = { RequestMethod.POST })
	@ResponseBody
	public Object addLocationMsg(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam("order_id") int order_id,
			@RequestParam(value = "longitude" , required = false) Long longitude,
			@RequestParam(value = "latitude" , required = false) Long latitude,
			@RequestParam(value = "country" , required = false) String country,
			@RequestParam(value = "province" , required = false) String province,
			@RequestParam(value = "city" , required = false) String city,
			@RequestParam(value = "street" , required = false) String street){
		if(longitude == null){
			longitude = (long) 0 ;
		}
		if(latitude == null){
			latitude = (long) 0 ;
		}
		LocateAddress locateAddress = new LocateAddress();
		locateAddress.setOrder_id(order_id);
		locateAddress.setCity(city);
		locateAddress.setCountry(country);
		locateAddress.setLatitude(latitude);
		locateAddress.setLongitude(longitude);
		locateAddress.setProvince(province);
		locateAddress.setStreet(street);
		laService.addLocateAddress(locateAddress);
//		JSONObject obj = new JSONObject();
//		laService.supplyJsonObject(obj, locateAddress);
//		return obj.toString();
		return ResultConf.getSuccessResult(locateAddress.getId());
	}
	
	/**
	 * 添加订单
	 * @return
	 */
	@MobileLoginRequired
	@RequestMapping(value = "/addOrders" , method = { RequestMethod.POST })
	@ResponseBody
	public Object addOrders(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam("user_id") int user_id, 
			@RequestParam("addressId") int addressId,
			@RequestParam("buyer_id") int buyer_id,
			@RequestParam("task_id") long task_id,
			@RequestParam("title") String title,
			@RequestParam("gratuity") double gratuity,
			@RequestParam("message") String message,
			@RequestParam("money") double money){
		if(addressId <= 0){
			return ResultConf.getResult(101, "亲，收货地址不存在哦");
		}
		if(task_id <= 0){
			return ResultConf.getResult(102, "亲，任务不存在哦");
		}
		AddressUser addressUser = addressService.getAddressUser(addressId);
		if(addressUser == null){
			return ResultConf.getResult(101, "亲，收货地址不存在哦");
		}
		Task task = taskService.getTask(task_id);
		if(task == null){
			return ResultConf.getResult(102, "亲，任务不存在哦");
		}
		if(user_id != task.getUserId()){
			return ResultConf.getResult(103, "亲，只有买家才可以创建订单哦");
		}
		if(task.getStatus() == TaskConf.TASK_EXPIRED){
			return ResultConf.getResult(115, "亲，任务已过期哦");
		}
		if(task.getStatus() == TaskConf.TASK_TAKE_AWAY){
			return ResultConf.getResult(116, "亲，该任务已经被认领了哦");
		}
		double total_money = money * (1.0 + task.getGratuity() / 100);
		Orders orders = new Orders();
		orders.setBuyer_id(buyer_id);
		orders.setOwner_id(user_id);
		orders.setOrder_status(OrdersConf.BUYERACK_STATUS);
		orders.setTotal_money(total_money);
		orders.setTask_id(task_id);
		orders.setOrder_number(KeysGenerator.getInstance().generatorId());
		orders.setAddressId(addressId);
		orders.setTitle(title);
		orders.setGratuity(gratuity);
		orders.setMessage(message);
		orders.setMoney(money);
		orders.setCreate_time(new Date());

		ordersService.addOrders(orders);
		
		task.setGratuity(gratuity);
		task.setTitle(title);
		task.setMessage(message);
		task.setMoney(money);
		task.setUpdate_time(new Date());
		//更改任务状态为 已认领
		task.setStatus(TaskConf.TASK_TAKE_AWAY);
		taskService.updateTask(task);
		taskService.afterDeleteTask(task);
		taskService.afterAddTask(task);
		
//		JSONObject json = new JSONObject();
//		ordersService.supplyJsonObject(json, orders);
//		addressService.supplyJsonObject(json, addressUser);
//		taskService.supplyJsonObject(json, task);		
		
		Good goods = goodService.getGood(task.getGoodId());
		JSONObject data = new JSONObject();
		data.put("order_id", orders.getId());
		data.put("task_id", orders.getTask_id());
		data.put("addressId", orders.getAddressId());
		data.put("order_num", orders.getOrder_number());
		data.put("order_time", orders.getCreate_time().getTime() / 1000);
		data.put("receiverName", addressUser.getReceiverName());
		data.put("receiverTel", addressUser.getTel());
		data.put("addressDetail", addressUser.getAddressDetail());
		data.put("buyer_id", orders.getBuyer_id());
		data.put("defautGoodsUrl", goods == null ? "" : goods.getDefaultHeadUrl());
		data.put("task_title", task.getTitle());
		data.put("gratuity", task.getGratuity());
		data.put("money", task.getMoney());
		data.put("message", task.getMessage());
		data.put("total_money", orders.getTotal_money());

		return ResultConf.getSuccessResult(data);
	}
	
	/**
	 * 编辑订单
	 * @return
	 */
	@MobileLoginRequired
	@RequestMapping(value = "/updateOrders" , method = { RequestMethod.POST })
	@ResponseBody
	public Object updateOrders(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam("user_id") int user_id, 
			@RequestParam("addressId") int addressId,
			@RequestParam("buyer_id") int buyer_id,
			@RequestParam("task_id") long task_id,
			@RequestParam("title") String title,
			@RequestParam("gratuity") double gratuity,
			@RequestParam("message") String message,
			@RequestParam("money") double money,
			@RequestParam("order_id") int order_id){
		if(order_id <= 0){
			logger.error("[---error---]:order_id 不能为空！" );
			return ResultConf.getResult(106, "亲，编辑订单失败哦");
		}
		if(addressId <= 0){
			return ResultConf.getResult(101, "亲，收货地址不存在哦");
		}
		if(task_id <= 0){
			return ResultConf.getResult(102, "亲，任务不存在哦");
		}
		AddressUser addressUser = addressService.getAddressUser(addressId);
		if(addressUser == null){
			return ResultConf.getResult(101, "亲，收货地址不存在哦");
		}
		Task task = taskService.getTask(task_id);
		if(task == null){
			return ResultConf.getResult(102, "亲，任务不存在哦");
		}
		if(user_id != task.getUserId()){
			return ResultConf.getResult(103, "亲，只有买家才可以编辑订单哦");
		}
		if(task.getStatus() == TaskConf.TASK_EXPIRED){
			return ResultConf.getResult(115, "亲，任务已过期哦");
		}
		if(task.getStatus() == TaskConf.TASK_TAKE_AWAY){
			return ResultConf.getResult(116, "亲，该任务已经被认领了哦");
		}
		double total_money = money * (1.0 + task.getGratuity() / 100);
		Orders orders = ordersService.getOrders(order_id);
		int status = orders.getOrder_status();
		if(status != OrdersConf.BUYERACK_STATUS){
			return ResultConf.getResult(153, "亲，买手已经确认该订单了哦");
		}
		orders.setTotal_money(total_money);
		orders.setAddressId(addressId);
		orders.setTitle(title);
		orders.setGratuity(gratuity);
		orders.setMessage(message);
		orders.setMoney(money);
		orders.setUpdate_time(new Date());

		ordersService.updateOrders(orders);
		
		task.setGratuity(gratuity);
		task.setTitle(title);
		task.setMessage(message);
		task.setMoney(money);
		task.setUpdate_time(new Date());
		//更改任务状态为 已认领
		task.setStatus(TaskConf.TASK_TAKE_AWAY);
		taskService.updateTask(task);
		taskService.afterDeleteTask(task);
		taskService.afterAddTask(task);
		
//		JSONObject json = new JSONObject();
//		ordersService.supplyJsonObject(json, orders);
//		addressService.supplyJsonObject(json, addressUser);
//		taskService.supplyJsonObject(json, task);		
		
		Good goods = goodService.getGood(task.getGoodId());
		JSONObject data = new JSONObject();
		data.put("order_id", orders.getId());
		data.put("task_id", orders.getTask_id());
		data.put("addressId", orders.getAddressId());
		data.put("order_num", orders.getOrder_number());
		data.put("order_time", orders.getCreate_time().getTime() / 1000);
		data.put("receiverName", addressUser.getReceiverName());
		data.put("receiverTel", addressUser.getTel());
		data.put("addressDetail", addressUser.getAddressDetail());
		data.put("buyer_id", orders.getBuyer_id());
		data.put("defautGoodsUrl", goods == null ? "" : goods.getDefaultHeadUrl());
		data.put("task_title", task.getTitle());
		data.put("gratuity", task.getGratuity());
		data.put("money", task.getMoney());
		data.put("message", task.getMessage());
		data.put("total_money", orders.getTotal_money());

		return ResultConf.getSuccessResult(data);
	}
	
	
	/**
	 * 买家任务表中无任务时，添加订单
	 * @return
	 */
	/*
	@MobileLoginRequired
	@RequestMapping(value = "/addOrders2" , method = { RequestMethod.POST })
	@ResponseBody
	public Object addOrders2(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam("user_id") int user_id,  //用户判断
			@RequestParam("addressId") int addressId,
			@RequestParam("buyer_id") int buyer_id,
			@RequestParam("title") String title,
			@RequestParam("gratuity") double gratuity,
			@RequestParam("deadline") long deadline,
			@RequestParam("message") String message,
			@RequestParam(value = "remark", required = false) String remark,
			@RequestParam(value = "good_country", required = false) String goodCountry,
			@RequestParam(value = "money", required = false) Double money,
			@RequestParam(value = "good_number", required = false) Integer goodNumber,
			@RequestParam(value = "live_city", required = false) String liveCity){
		if (money == null) {
			money = 0.0;
		}
		if (goodNumber == null) {
			goodNumber = 1;
		}
		if (addressId == 0){
			return ResultConf.getResult(101, "亲，收货地址不存在哦");
		}
		if (title == null || title.equals("")){
			return ResultConf.getResult(104, "亲，任务名不能为空哦");
		}
		// 添加商品
		// 转型为MultipartHttpRequest：
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		// 获得文件：
		List<MultipartFile> good_photos = multipartRequest
				.getFiles("good_photos");
		// 验证
		JSONObject validate = UploadService.getInstance().validate(good_photos);
		if (!ResultConf.isSuccess(validate)) {
			return validate;
		}
		Good good = goodService.saveGoodAndPhoto(title, good_photos);
		// 保存任务
		Task newTask = new Task();
		newTask.setUserId(user_id);
		newTask.setTitle(title);
		newTask.setGratuity(gratuity);
		newTask.setDeadline(deadline);
		newTask.setMessage(message);
		newTask.setRemark(remark);
		newTask.setGoodCountry(goodCountry);
		newTask.setMoney(money);
		newTask.setGoodNumber(goodNumber);
		newTask.setLiveCity(liveCity);
		//设置为被认领 状态
		newTask.setStatus(TaskConf.TASK_TAKE_AWAY);
		if (good != null) {
			newTask.setGoodId(good.getId());
		}
		taskService.addTask(newTask);
		taskService.afterAddTask(newTask);
		
		double total_money = newTask.getMoney() * (1.0 + newTask.getGratuity() / 100);
		Orders orders = new Orders();
		orders.setBuyer_id(buyer_id);
		orders.setOwner_id(user_id);
		orders.setOrder_status(OrdersConf.BUYERACK_STATUS);
		orders.setAddressId(addressId);
		orders.setTotal_money(total_money);
		orders.setTask_id(newTask.getId());
		orders.setOrder_number(KeysGenerator.getInstance().generatorId());
		orders.setTitle(title);
		orders.setMessage(message);
		orders.setMoney(money);
		orders.setGratuity(gratuity);
		ordersService.addOrders(orders);
		AddressUser addressUser = addressService.getAddressUser(addressId);
		JSONObject json = new JSONObject();
		ordersService.supplyJsonObject(json, orders);
		taskService.supplyJsonObject(json, newTask);
		addressService.supplyJsonObject(json, addressUser);
		return ResultConf.getSuccessResult(json);
	}
*/
	
	/**
	 * 等待卖家确认
	 * @return
	 */
	@MobileLoginRequired
	@RequestMapping(value = "/buyerAck" , method = { RequestMethod.POST })
	@ResponseBody
	public Object buyerAck(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam("user_id") int user_id,
			@RequestParam("order_id") int order_id){
		Orders order = ordersService.getOrders(order_id);
		if(user_id != order.getBuyer_id()){
			return ResultConf.getResult(105, "亲，需要卖家来确认哦");
		}
		if(order.getOrder_status() == OrdersConf.BUYERACK_STATUS){
			order.setOrder_status(OrdersConf.RECEIVERPAY_STATUS);
			order.setUpdate_time(new Date());
			ordersService.updateOrders(order);
		}else{
			return ResultConf.getResult(151, "亲，订单确认失败哦");
		}
		JSONObject obj = new JSONObject();
		obj.put("order_status", OrdersConf.RECEIVERPAY_STATUS);
		return ResultConf.getSuccessResult(obj);
	}
	
	/**
	 * 等待买家付款
	 * @return
	 */
	@MobileLoginRequired
	@RequestMapping(value = "/receiverPay" , method = { RequestMethod.POST })
	@ResponseBody
	public Object receiverPay(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam("user_id") int user_id,
			@RequestParam("order_id") int order_id){
		Orders order = ordersService.getOrders(order_id);
		Task task = taskService.getTask(order.getTask_id()); // 获取userId
		if (user_id != task.getUserId()) {
			return ResultConf.getResult(106, "亲，需要买家来付款哦");
		}
		if (order.getOrder_status() == OrdersConf.RECEIVERPAY_STATUS) {
			order.setOrder_status(OrdersConf.PURCHASING_STATUS);
			order.setUpdate_time(new Date());
			ordersService.updateOrders(order);
		} else {
			return ResultConf.getResult(157, "付款失败，请稍后重试");
		}
		JSONObject obj = new JSONObject();
		obj.put("order_status", OrdersConf.PURCHASING_STATUS);
		return ResultConf.getSuccessResult(obj);
	}
	
	/**
	 * 采购中
	 * @return
	 */
	@MobileLoginRequired
	@RequestMapping(value = "/purchasing" , method = { RequestMethod.POST })
	@ResponseBody
	public Object purchasing(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam("user_id") int user_id,
			@RequestParam("order_id") int order_id){
		Orders order = ordersService.getOrders(order_id);
		if(user_id != order.getBuyer_id()){
			return ResultConf.getResult(107, "亲，采购信息由卖家确认哦");
		}
		if(order.getOrder_status() == OrdersConf.PURCHASING_STATUS){
			order.setOrder_status(OrdersConf.PURCHASED_STATUS);
			ordersService.updateOrders(order);
		}
		JSONObject obj = new JSONObject();
		obj.put("order_status", OrdersConf.PURCHASED_STATUS);
		return ResultConf.getSuccessResult(obj);
	}
	
	/**
	 * 添加物流信息
	 * @return
	 */
	@MobileLoginRequired
	@RequestMapping(value = "/addLogistics" , method = { RequestMethod.POST })
	@ResponseBody
	public Object addLogistics(HttpServletRequest request,
			HttpServletRequest response,
			@RequestParam("user_id") int user_id,
			@RequestParam("company") String company,
			@RequestParam("orderId") int orderId,
			@RequestParam("logistics_number") String logistics_number){
//		if(orders_number == null){
//			orders_number = (long) 0;
//		}
		Orders order = ordersService.getOrders(orderId);
		if(user_id != order.getBuyer_id()){
			return ResultConf.getResult(108, "亲，需要卖家添加物流信息哦");
		}
		Logistics logistics = new Logistics();
		logistics.setCompany(company);
		logistics.setLogistics_number(logistics_number);
		logistics.setOrders_number(order.getOrder_number());
		logisticsService.addLogisticsDao(logistics);
		//结构参考supplyJsonObject(json,orders)
		if(order.getOrder_status() == OrdersConf.PURCHASED_STATUS){
			order.setOrder_status(OrdersConf.RECEIVERRECEIV_STATUS);
			ordersService.updateOrders(order);
		}else{
			return ResultConf.getResult(109, "亲，订单状态错误哦");
		}
		JSONObject json = new JSONObject();
		JSONArray jar = new JSONArray();
		JSONObject object = new JSONObject();
		object.put("id", logistics.getId());
		object.put("company", logistics.getCompany());
		object.put("orders_number", logistics.getOrders_number());
		object.put("logistics_number" , logistics.getLogistics_number());
		jar.add(object);
		json.put("logistics_info", jar);
		json.put("order_status", OrdersConf.RECEIVERRECEIV_STATUS);
		return ResultConf.getSuccessResult(json);
	}
	
	/**
	 * 买家确认收货
	 * @return
	 */
	@MobileLoginRequired
	@RequestMapping(value = "/received" , method = { RequestMethod.POST })
	@ResponseBody
	public Object received(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam("user_id") int user_id,
			@RequestParam("order_id") int order_id){
		Orders order = ordersService.getOrders(order_id);
		Task task = taskService.getTask(order.getTask_id()); //获取userId
		if(user_id != task.getUserId()){
			return ResultConf.getResult(110, "亲，需要买家确认收货哦");
		}
		if(order.getOrder_status() == OrdersConf.RECEIVERRECEIV_STATUS){
			order.setOrder_status(OrdersConf.WAITING_COMMENTS);
			order.setUpdate_time(new Date());
			ordersService.updateOrders(order);
		}
		JSONObject obj = new JSONObject();
		obj.put("order_status", OrdersConf.WAITING_COMMENTS);
		return ResultConf.getSuccessResult(obj);
	}
	
//	/**
//	 * 添加评价
//	 */
//	@MobileLoginRequired
//	@RequestMapping(value = "/addComment" , method = { RequestMethod.POST })
//	@ResponseBody
//	public Object addComment(HttpServletRequest request,
//			HttpServletResponse response,
//			@RequestParam("user_id") int user_id,
//			@RequestParam("order_id") int order_id,
//			@RequestParam("star") int star,
//			@RequestParam("comment") String comment){
//		Orders order = ordersService.getOrders(order_id);
//		Task task = taskService.getTask(order.getTask_id()); //获取userId
//		
//		if(user_id != task.getUserId()){
//			return ResultConf.getResult(110, "亲，需要买家确认收货哦");
//		}
//		if(order.getOrder_status() == OrdersConf.RECEIVERRECEIV_STATUS){
//	}
	
	
	/**
	 * 添加留言
	 * @return
	 */
	@MobileLoginRequired
	@RequestMapping(value = "/addLeaveMessage" , method = { RequestMethod.POST })
	@ResponseBody
	public Object addLeaveMessage(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam("user_id") int user_id,
			@RequestParam("order_id") int order_id,
			@RequestParam("leave_message") String leave_message){
		Orders orders = ordersService.getOrders(order_id);
		if(orders == null){
			return ResultConf.getResult(111, "亲，留言添加失败哦");
		}
		if(user_id != orders.getOwner_id()){
			return ResultConf.getResult(112, "亲，只有买家可以添加留言哦");
		}
		if(orders.getOrder_status() != OrdersConf.PURCHASING_STATUS){
			return ResultConf.getResult(113, "亲，现在不能添加留言哦");
		}
		orders.setLeave_message(leave_message);
		orders.setUpdate_time(new Date());
		ordersService.updateOrders(orders);
		JSONObject json = new JSONObject();
		json.put(OrdersConf.LEAVE_MESSAGE, orders.getLeave_message());
		json.put(OrdersConf.ORDERS_ID, order_id);
		json.put(OrdersConf.OWNER_ID, user_id);
		return ResultConf.getSuccessResult(json);
	}
	
	/**
	 * 获取user买入的订单
	 * @param request
	 * @param response
	 * @param user_id
	 * @return
	 */
	@MobileLoginRequired
	@RequestMapping(value = "/getBuyOrders" , method = { RequestMethod.POST })
	@ResponseBody
	public Object getBuyOrders(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam("user_id") int user_id){
		List<Orders> orderList = ordersService.getBuyOrdersList(user_id);
		JSONArray jar = new JSONArray();
		JSONObject json = new JSONObject();
		for(Orders order : orderList){
			if(order == null) {
				continue;
			}
			Task task = taskService.getTask(order.getTask_id());
			if(task == null) {
				continue;
			}
			User user = userService.getUser(order.getBuyer_id());
			if (user == null) {
				continue;
			}
			Good goods = goodService.getGood(task.getGoodId());
			json.put("user_id", order.getBuyer_id());
			json.put("user_name", user.getName());
			json.put("head_url", user.getHead_url());
			json.put("order_number", order.getOrder_number());
			json.put("order_status", order.getOrder_status());
			json.put("order_id",order.getId() );
			json.put("total_money", order.getTotal_money());
			json.put("task_id", order.getTask_id());
			json.put("title", task.getTitle());
			json.put("goods_pic", goods == null ? "" : goods.getDefaultHeadUrl());
			jar.add(json);
		}
		JSONObject ret = JSONObject.fromObject(ResultConf.SUCCESSRET);
		ret.put(ResultConf.DATA, jar);
		return ret;
	}
	
	/**
	 * 获取卖出订单列表
	 * @param request
	 * @param response
	 * @param user_id
	 * @return
	 */
	@MobileLoginRequired
	@RequestMapping(value = "/getSellOrders" , method = { RequestMethod.POST })
	@ResponseBody
	public Object getSellOrders(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam("user_id") int user_id){
		List<Orders> orderList = ordersService.getSellOrdersList(user_id);
		JSONArray jar = new JSONArray();
		JSONObject json = new JSONObject();
		for(Orders order : orderList){
			if(order == null) {
				continue;
			}
			Task task = taskService.getTask(order.getTask_id());
			if(task == null) {
				continue;
			}
			User user = userService.getUser(order.getOwner_id());
			if(user == null) {
				continue;
			}
			Good goods = goodService.getGood(task.getGoodId());
			json.put("user_id", order.getOwner_id());
			json.put("user_name", user.getName());
			json.put("head_url", user.getHead_url());
			json.put("order_number", order.getOrder_number());
			json.put("order_status", order.getOrder_status());
			json.put("order_id",order.getId() );
			json.put("total_money", order.getTotal_money());
			json.put("task_id", order.getTask_id());
			json.put("title", task.getTitle());
			json.put("goods_pic", goods == null ? "" : goods.getDefaultHeadUrl());
			jar.add(json);
		}
		JSONObject ret = JSONObject.fromObject(ResultConf.SUCCESSRET);
		ret.put(ResultConf.DATA, jar);
		return ret;
	}
	
	/**
	 * 获取订单详细信息
	 * @param request
	 * @param response
	 * @param user_id
	 * @param order_id
	 * @return
	 */
	@MobileLoginRequired
	@RequestMapping(value = "/getOrdersDetail" , method = { RequestMethod.POST })
	@ResponseBody
	public Object getOrdersDetail(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam("user_id") int user_id,
			@RequestParam("order_id") int order_id){
		if(order_id <= 0){
			return ResultConf.getResult(131, "亲，订单不存在哦");
		}
		Orders order = ordersService.getOrders(order_id);
		if(order == null){
			return ResultConf.getResult(134, "亲，订单不存在哦");
		}
//		if(user_id != order.getOwner_id()){
//			return ResultConf.getResult(132, "亲，查看订单错误哦");
//		}
		Task task = taskService.getTask(order.getTask_id());
		if(task == null ){
			return ResultConf.getResult(133, "亲，获取任务信息失败哦");
		}
		User buyer = userService.getUser(order.getBuyer_id());
		AddressUser addressUser = addressService.getAddressUser(order.getAddressId());
		List<Logistics> logistics = logisticsService.getLogisticsbyOrder(order.getOrder_number());
		Good goods = goodService.getGood(task.getGoodId());
		JSONObject json = new JSONObject();
		if(logistics != null){
			json.put("logistics", logistics.get(0).getCompany());//物流信息
		}
		if(addressUser != null){
			json.put("receiverName", addressUser.getReceiverName() );
			json.put("addressDetail" , addressUser.getAddressDetail());
			json.put("receiverTel", addressUser.getTel());
			json.put("addressId", addressUser.getId());
		}
		if(buyer != null){
			json.put("buyerName", buyer.getName());
			json.put("buyerId", buyer.getId());
		}
		json.put("orderStatus", order.getOrder_status());
		json.put("orderNumber", order.getOrder_number());
		json.put("owner_id", order.getOwner_id());
		json.put("orderCreateTime", order.getCreate_time().getTime() / 1000);
		json.put("goodPic", goods == null ? "" :goods.getDefaultHeadUrl());
		json.put("gratuity", task.getGratuity());
		json.put("money", task.getMoney());
		json.put("taskMessage", task.getMessage());
		json.put("taskTitle", task.getTitle());
		json.put("totalMoney", order.getTotal_money());
		json.put("leaveMessage", order.getLeave_message());
		json.put("taskId", task.getId());
		return ResultConf.getSuccessResult(json);
	}
	
	@MobileLoginRequired
	@RequestMapping(value = "/getTaskAndBuyerInfo" , method = { RequestMethod.POST })
	@ResponseBody
	public Object getOrdersDetail(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam("user_id") int user_id,
			@RequestParam("buyer_id") int buyer_id,
			@RequestParam("task_id") int task_id){

		Task task = taskService.getTask(task_id);
		if(task == null ){
			return ResultConf.getResult(133, "亲，获取任务信息失败哦");
		}
		User buyer = userService.getUser(buyer_id);
		if (buyer == null) {
			return ResultConf.getResult(134, "亲，获取买手信息失败哦");
		}
		Good goods = goodService.getGood(task.getGoodId());
		if (goods == null) {
			return ResultConf.getResult(135, "亲，获取商品信息失败哦");
		}
		List<GoodPhoto> photoList = goodService.getGoodPhotoListByGoodId(goods.getId());
		AddressUser addressUser = addressService.getAddressUserDefault(user_id);
		if (addressUser == null) {
			List<AddressUser> lists = addressService.getAddressUserListByUserId(user_id);
			if (lists != null && lists.size() > 0) {
				addressUser = lists.get(0);
			}
		}	
		
		JSONObject json = new JSONObject();
		if(addressUser != null){
			json.put("receiverName", addressUser.getReceiverName() );
			json.put("addressDetail" , addressUser.getAddressDetail());
			json.put("receiverTel", addressUser.getTel());
			json.put("addressId", addressUser.getId());
		}
		json.put("buyerName", buyer.getName());
		json.put("buyerId", buyer.getId());
		
		json.put("gratuity", task.getGratuity());
		json.put("money", task.getMoney());
		json.put("taskMessage", task.getMessage());
		json.put("taskTitle", task.getTitle());
		json.put("taskId", task.getId());
		
		json.put("goodsId", goods.getId());
		JSONArray jphotos = new JSONArray();
		if (photoList != null) {
			for (GoodPhoto photo : photoList) {
				JSONObject jphoto = new JSONObject();
				jphoto.put(GoodConf.GOODPHOTOID, photo.getId());
				jphoto.put(GoodConf.GOOD_PHOTO_URL, photo.getPhoto_url());
				jphoto.put(GoodConf.GOOD_MAIN_URL, photo.getMain_url());
				jphoto.put(GoodConf.GOOD_HEAD_URL, photo.getHead_url());
				jphoto.put(GoodConf.GOOD_TINY_URL, photo.getTiny_url());
				jphoto.put(GoodConf.GOODPHOTODESCRIPTION, photo.getDescription());
				jphotos.add(jphoto);
			}
		}
		json.put("goodsPhotos", jphotos);
		return ResultConf.getSuccessResult(json);
	}

}
