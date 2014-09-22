package com.renren.shopping.services;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.renren.shopping.conf.OrdersConf;
import com.renren.shopping.dao.OrdersDao;
import com.renren.shopping.model.Orders;
import com.renren.shopping.model.Task;

/**
 * 订单管理
 * @author yu.zhuang
 *
 */
@Service
public class OrdersService {
	
	@Autowired
	OrdersDao orderDao;
	
	@Autowired
	TaskService taskService;
	
	public Log logger = LogFactory.getLog(OrdersService.class);
	
	public void addOrders(Orders orders){
		orderDao.saveObject(orders);
	}
	
	public void updateOrders(Orders orders){
		orderDao.updateObject(orders);
	}
	
	public void deleteOrders(Orders orders){
		orderDao.deleteObject(orders);
	}
	
	public Orders getOrders(int id){
		return orderDao.getObject(Orders.class, id);
	}
	
	public Task getTask(int task_id){
		return taskService.getTask(task_id);
	}
	
	/**
	 * 获取买入订单列表
	 * @param user_id
	 * @return
	 */
	public List<Orders> getBuyOrdersList(int user_id){
		String hql = "from Orders where owner_id = ? order by id desc";
		return orderDao.find(hql, user_id);
	}
	
	/**
	 * 获取卖出订单列表
	 * @param buyer_id
	 * @return
	 */
	public List<Orders> getSellOrdersList(int buyer_id){
		String hql = "from Orders where buyer_id = ? order by id  desc";
		return orderDao.find(hql, buyer_id);
	}
	
	
	
	/**
	 * 将订单信息添加到JsonObject中
	 * @param obj
	 * @param orders
	 */
	public void supplyJsonObject(JSONObject obj, Orders orders){
		if(orders == null){
			return;
		}
		List<Orders> list = new ArrayList<Orders>();
		list.add(orders);
		supplyJsonObject(obj, list);
	}
	
	/**
	 * 将订单类表信息添加到JsonObject中
	 * @param obj
	 * @param orders
	 */
	public void supplyJsonObject(JSONObject obj , List<Orders> list){
		if(list == null){
			return ;
		}
		JSONArray jar = new JSONArray();
		for(Orders order : list){
			JSONObject t = new JSONObject();
			t.put(OrdersConf.ORDERS_ID, order.getId());
			t.put(OrdersConf.BUYER_ID, order.getBuyer_id());
			t.put(OrdersConf.OWNER_ID, order.getOwner_id());
			t.put(OrdersConf.COMMENTS, order.getComments());
			t.put(OrdersConf.ORDER_NUMBER, order.getOrder_number());
			t.put(OrdersConf.ORDER_STATUS, order.getOrder_status());
			t.put(OrdersConf.ADDRESSID , order.getAddressId());
			t.put(OrdersConf.SCORE, order.getScore());
			t.put(OrdersConf.TASK_ID, order.getTask_id());
			t.put(OrdersConf.TOTAL_MOENY, order.getTotal_money());
			t.put(OrdersConf.UPDATE_TIME, order.getUpdate_time());
			t.put(OrdersConf.CREATE_TIME, order.getCreate_time());
			t.put(OrdersConf.LEAVE_MESSAGE, order.getLeave_message());
			t.put(OrdersConf.GRATUITY, order.getGratuity());
			t.put(OrdersConf.TITLE, order.getTitle());
			t.put(OrdersConf.MESSAGE, order.getMessage());
			t.put(OrdersConf.MONEY, order.getMoney());
			jar.add(t);
		}
		obj.put(OrdersConf.ORDERSKEY, jar);
	}
}
