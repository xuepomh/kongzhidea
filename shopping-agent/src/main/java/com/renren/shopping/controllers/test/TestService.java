package com.renren.shopping.controllers.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.mvc.LastModified;

import com.renren.shopping.model.LocateAddress;
import com.renren.shopping.services.LocateAddressService;

public class TestService {
	@Autowired
	LocateAddressService laService;
	
	public static void main(String args []){
		LocateAddressService service = new LocateAddressService();
		LocateAddress address = new LocateAddress();
		address.setCity("北京");
		address.setContinent("亚洲");
		address.setCountry("中国");
		address.setLongitude(1234567);
		address.setLatitude(987654);
		address.setOrder_id(123);
		address.setProvince("北京");
		address.setStreet("酒仙桥中路18号");
		service.addLocateAddress(address);
	}
}
