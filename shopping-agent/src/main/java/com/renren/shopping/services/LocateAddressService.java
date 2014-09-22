package com.renren.shopping.services;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.renren.shopping.conf.AddressConf;
import com.renren.shopping.dao.LocateAddressDao;
import com.renren.shopping.model.LocateAddress;

/**
 * 定位service
 * @author yu.zhuang
 *
 */
@Service
public class LocateAddressService {
	
	@Autowired
	LocateAddressDao locateAddressDao;
	
	public Log logger = LogFactory.getLog(LocateAddressService.class);
	
	public void addLocateAddress(LocateAddress locateAddress){
		locateAddressDao.saveObject(locateAddress);
	}
	
	//文档要求位置信息不进行更新，只在采购时触发定位
	public void updateLocateAddress(LocateAddress locateAddress){
		locateAddressDao.updateObject(locateAddress);
	}
	
	public void deleteLocateAddress(LocateAddress locateAddress){
		locateAddressDao.deleteObject(locateAddress);
	}
	
	public LocateAddress getLocateAddress(int order_id){
		String hql = "from LocateAddress where order_id = ?";
		return locateAddressDao.find(hql, order_id).get(0);
	}
	
	public LocateAddress getLocateAddressById(int id){
		return locateAddressDao.getObject(LocateAddress.class, id);
	}
	
	/**
	 * 将地址定位信息添加到JsonObject中
	 * @param obj
	 * @param locateAddress
	 */
	public void supplyJsonObject(JSONObject obj, LocateAddress locateAddress){
		if(locateAddress == null){
			return;
		}
		List<LocateAddress> list = new ArrayList<LocateAddress>();
		list.add(locateAddress);
		supplyJsonObject(obj,list);
	}
	/**
	 * 将地址定位类表信息添加到JsonObject中
	 * @param obj
	 * @param address
	 */
	public void supplyJsonObject(JSONObject obj, List<LocateAddress> addresses){
		if(addresses.size() == 0 || addresses == null){
			return;
		}
		JSONArray jar = new JSONArray();
		for(LocateAddress address : addresses){
			JSONObject t = new JSONObject();
			t.put(AddressConf.ADDRESS_ID, address.getId());
			t.put(AddressConf.LONGITUDE, address.getLongitude());
			t.put(AddressConf.LATITUDE, address.getLatitude());
			t.put(AddressConf.CONTINENT, address.getContinent());
			t.put(AddressConf.COUNTRY, address.getCountry());
			t.put(AddressConf.PROVINCE, address.getProvince());
			t.put(AddressConf.CITY, address.getCity());
			t.put(AddressConf.STREET, address.getStreet());
			t.put("order_id", address.getOrder_id());
			t.put("update_time", address.getCreate_time());
			jar.add(t);
		}
		obj.put(AddressConf.ADDRESSKEY, jar);
	}
}
