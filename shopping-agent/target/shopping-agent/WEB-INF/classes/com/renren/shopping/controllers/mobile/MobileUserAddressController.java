package com.renren.shopping.controllers.mobile;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.renren.shopping.annotion.MobileLoginRequired;
import com.renren.shopping.conf.GoodConf;
import com.renren.shopping.conf.ResultConf;
import com.renren.shopping.conf.TaskConf;
import com.renren.shopping.conf.TravelConf;
import com.renren.shopping.model.AddressUser;
import com.renren.shopping.model.CollectionPhoto;
import com.renren.shopping.model.Task;
import com.renren.shopping.model.Travel;
import com.renren.shopping.model.User;
import com.renren.shopping.model.UserDetail;
import com.renren.shopping.model.UserFollows;
import com.renren.shopping.model.UserFollowsPK;
import com.renren.shopping.services.AddressService;
import com.renren.shopping.services.GoodService;
import com.renren.shopping.services.TaskService;
import com.renren.shopping.services.TravelService;
import com.renren.shopping.services.UserService;

/**
 * 用户地址相关
 * 
 * @author kk
 * 
 */
@Controller
@RequestMapping("/mobile/user")
public class MobileUserAddressController {

	@Autowired
	UserService userService;

	@Autowired
	AddressService addressService;

	public Log logger = LogFactory.getLog(MobileUserAddressController.class);

	/**
	 * 添加地址
	 * 
	 * @param user_id
	 * @return
	 */
	@MobileLoginRequired
	@RequestMapping(value = "/addAddress", method = { RequestMethod.POST })
	@ResponseBody
	public Object addAddress(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam("user_id") int userId,
			@RequestParam("receiver_name") String receiverName,
			@RequestParam("tel") String tel,
			@RequestParam("province") String province,
			@RequestParam("city") String city,
			@RequestParam("county") String county,
			@RequestParam("street") String street,
			@RequestParam(value = "zipcode", required = false) String zipcode,
			@RequestParam(value = "is_default", required = false) Integer isDefault) {
		AddressUser address = new AddressUser();
		address.setUserId(userId);
		address.setReceiverName(receiverName);
		address.setTel(tel);
		address.setAddressDetail(addressService.getAddressDetail(province,
				city, county, street).toString());
		address.setZipcode(zipcode);
		addressService.addAddressUser(address);
		if (isDefault != null) {
			addressService.setAddressUserDefault(address);
		}
		return ResultConf.getSuccessResult(address.getId());
	}

	/**
	 * 更新地址
	 * 
	 * @param user_id
	 * @return
	 */
	@MobileLoginRequired
	@RequestMapping(value = "/updateAddress", method = { RequestMethod.POST })
	@ResponseBody
	public Object updateAddress(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam("user_id") int userId,
			@RequestParam("address_id") long addressId,
			@RequestParam("receiver_name") String receiverName,
			@RequestParam("tel") String tel,
			@RequestParam("province") String province,
			@RequestParam("city") String city,
			@RequestParam("county") String county,
			@RequestParam("street") String street,
			@RequestParam(value = "zipcode", required = false) String zipcode,
			@RequestParam(value = "is_default", required = false) Integer isDefault) {
		AddressUser address = addressService.getAddressUser(addressId);
		if (address == null || address.getUserId() != userId) {
			return ResultConf.SOURCE_NOTEXIST_EXCEPTION;
		}
		address.setUserId(userId);
		address.setReceiverName(receiverName);
		address.setTel(tel);
		address.setAddressDetail(addressService.getAddressDetail(province,
				city, county, street).toString());
		address.setZipcode(zipcode);
		addressService.updateAddressUser(address);
		if (isDefault != null) {
			addressService.setAddressUserDefault(address);
		}
		return ResultConf.getSuccessResult(address.getId());
	}

	/**
	 * 删除地址
	 * 
	 * @param user_id
	 * @return
	 */
	@MobileLoginRequired
	@RequestMapping(value = "/deleteAddress", method = { RequestMethod.POST })
	@ResponseBody
	public Object deleteAddress(HttpServletRequest request,
			HttpServletResponse response, @RequestParam("user_id") int userId,
			@RequestParam("address_id") long addressId) {
		AddressUser address = addressService.getAddressUser(addressId);
		if (address == null || address.getUserId() != userId) {
			return ResultConf.SOURCE_NOTEXIST_EXCEPTION;
		}
		addressService.deleteAddressUser(address);
		return ResultConf.getSuccessResult(addressId);
	}

	/**
	 * 得到地址信息
	 * 
	 * @param request
	 * @param response
	 * @param userId
	 * @param addressId
	 * @return
	 */
	@MobileLoginRequired
	@RequestMapping(value = "/getAddress", method = { RequestMethod.POST })
	@ResponseBody
	public Object getAddress(HttpServletRequest request,
			HttpServletResponse response, @RequestParam("user_id") int userId,
			@RequestParam("address_id") long addressId) {
		AddressUser address = addressService.getAddressUser(addressId);
		JSONObject obj = new JSONObject();
		addressService.supplyJsonObject(obj, address);
		return ResultConf.getSuccessResult(obj);
	}

	/**
	 * 得到用户地址列表
	 * 
	 * @param request
	 * @param response
	 * @param userId
	 * @param addressId
	 * @return
	 */
	@MobileLoginRequired
	@RequestMapping(value = "/getAddressList", method = { RequestMethod.POST })
	@ResponseBody
	public Object getAddressList(HttpServletRequest request,
			HttpServletResponse response, @RequestParam("user_id") int userId) {
		List<AddressUser> address = addressService
				.getAddressUserListByUserId(userId);
		JSONObject obj = new JSONObject();
		addressService.supplyJsonObject(obj, address);
		return ResultConf.getSuccessResult(obj);
	}

	/**
	 * 设置默认地址
	 * 
	 * @param request
	 * @param response
	 * @param userId
	 * @param addressId
	 * @return
	 */
	@MobileLoginRequired
	@RequestMapping(value = "/setAddressDefault", method = { RequestMethod.POST })
	@ResponseBody
	public Object setAddressDefault(HttpServletRequest request,
			HttpServletResponse response, @RequestParam("user_id") int userId,
			@RequestParam("address_id") long addressId) {
		AddressUser address = addressService.getAddressUser(addressId);
		if (address == null) {
			return ResultConf.SOURCE_NOTEXIST_EXCEPTION;
		}
		addressService.setAddressUserDefault(address);
		return ResultConf.getSuccessResult(address.getId());
	}

}
