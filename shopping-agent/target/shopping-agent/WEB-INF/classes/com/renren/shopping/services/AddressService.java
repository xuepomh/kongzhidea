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
import com.renren.shopping.conf.UserConf;
import com.renren.shopping.dao.AddressOtherDao;
import com.renren.shopping.dao.AddressUserDao;
import com.renren.shopping.model.AddressOther;
import com.renren.shopping.model.AddressUser;

@Service
public class AddressService {

	@Autowired
	AddressUserDao addressUserDao;

	@Autowired
	AddressOtherDao addressOtherDao;

	public Log logger = LogFactory.getLog(AddressService.class);

	/************* 用户地址薄 ***********/
	public void addAddressUser(AddressUser addressUser) {
		addressUserDao.saveObject(addressUser);
	}

	public void updateAddressUser(AddressUser addressUser) {
		addressUserDao.updateObject(addressUser);
	}

	public void deleteAddressUser(AddressUser addressUser) {
		addressUserDao.deleteObject(addressUser);
	}

	public AddressUser getAddressUser(long id) {
		return addressUserDao.getObject(AddressUser.class, id);
	}

	public List<AddressUser> getAddressUserListByUserId(int userId) {
		String hql = "from AddressUser where userId = ? order by id desc";
		return addressUserDao.find(hql, userId);
	}

	/***************** @作用待定 *********************/

	public void addAddressOther(AddressOther addressOther) {
		addressOtherDao.saveObject(addressOther);
	}

	public void updateAddressOther(AddressOther addressOther) {
		addressOtherDao.updateObject(addressOther);
	}

	public void deleteAddressOther(AddressOther addressOther) {
		addressOtherDao.deleteObject(addressOther);
	}

	public AddressOther getAddressOther(long id) {
		return addressOtherDao.getObject(AddressOther.class, id);
	}

	/****************** 地址服务 ***************/
	/**
	 * 生成json结构，存储到数据库中
	 * 
	 * @param province
	 * @param city
	 * @param county
	 * @param street
	 * @return
	 */
	public JSONObject getAddressDetail(String province, String city,
			String county, String street) {
		JSONObject obj = new JSONObject();
		obj.put(AddressConf.PROVINCE, province);
		obj.put(AddressConf.CITY, city);
		obj.put(AddressConf.COUNTY, county);
		obj.put(AddressConf.STREET, street);
		return obj;
	}

	/**
	 * 将地址设置为用户的默认地址
	 * 
	 * @param address
	 */
	public void setAddressUserDefault(AddressUser address) {
		addressUserDao.setAddressDefaultNull(address.getUserId());
		address.setIsDefault(1);
		updateAddressUser(address);
	}

	/**
	 * 将地址信息添加到JsonObject中
	 * 
	 * @param obj
	 * @param address
	 */
	public void supplyJsonObject(JSONObject obj, AddressUser address) {
		if (address == null) {
			return;
		}
		List<AddressUser> list = new ArrayList<AddressUser>();
		list.add(address);
		supplyJsonObject(obj, list);

	}

	/**
	 * 将地址类表信息添加到JsonObject中
	 * 
	 * @param obj
	 * @param address
	 */
	public void supplyJsonObject(JSONObject obj, List<AddressUser> addresses) {
		if (addresses == null) {
			return;
		}
		JSONArray jar = new JSONArray();
		for (AddressUser address : addresses) {
			JSONObject t = new JSONObject();
			t.put(AddressConf.ADDRESS_ID, address.getId());
			t.put(AddressConf.RECEIVER_NAME, address.getReceiverName());
			t.put(AddressConf.TEL, address.getTel());
			t.put(AddressConf.ADDRESS_DETAIL, address.getAddressDetail());
			t.put(AddressConf.ZIPCODE, address.getZipcode());
			t.put(AddressConf.ADDRESS_IS_DEFAULT, address.getIsDefault());
			t.put(UserConf.USER_ID, address.getUserId());
			jar.add(t);
		}
		obj.put(AddressConf.ADDRESSKEY, jar);
	}
}
