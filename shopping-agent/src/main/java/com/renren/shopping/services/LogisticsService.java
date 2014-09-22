package com.renren.shopping.services;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.renren.shopping.dao.LogisticsDao;
import com.renren.shopping.model.Logistics;
import com.renren.shopping.model.Orders;
import com.renren.shopping.model.Task;

/**
 * 订单管理
 * @author yu.zhuang
 *
 */
@Service
public class LogisticsService {
	
	@Autowired
	LogisticsDao logisticsDao;
	
	public Log logger = LogFactory.getLog(LogisticsService.class);
	
	public void addLogisticsDao(Logistics logistics){
		logisticsDao.saveObject(logistics);
	}
	
	public void updateLogisticsDao(Logistics logistics){
		logisticsDao.updateObject(logistics);
	}
	
	public void deleteLogisticsDao(Logistics logistics){
		logisticsDao.deleteObject(logistics);
	}
	
	public Logistics getLogistics(int id){
		return logisticsDao.getObject(Logistics.class, id);
	}
	
	/**
	 * 获取物流信息
	 * @param user_id
	 * @return
	 */
	public List<Logistics> getLogisticsbyOrder(long orders_number){
		String hql = "from Logistics where orders_number = ?";
		List<Logistics> list = logisticsDao.find(hql, orders_number);
		if(list == null || list.size() == 0){
			return null;
		}
		return list;
	}

}
