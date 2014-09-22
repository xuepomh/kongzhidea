package com.renren.shopping.controllers.mobile;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.renren.shopping.annotion.MobileLoginRequired;
import com.renren.shopping.conf.ResultConf;
import com.renren.shopping.conf.TravelConf;
import com.renren.shopping.model.Good;
import com.renren.shopping.model.Travel;
import com.renren.shopping.model.TravelGood;
import com.renren.shopping.model.TravelGoodPK;
import com.renren.shopping.services.GoodService;
import com.renren.shopping.services.TravelService;
import com.renren.shopping.services.UploadService;
import com.renren.shopping.services.UserService;

/**
 * 行程相关
 * 
 * @author kk
 * 
 */
@Controller
@RequestMapping("/mobile/travel")
public class MobileTravelController {

	@Autowired
	TravelService travelService;

	@Autowired
	GoodService goodService;

	@Autowired
	UserService userService;

	public static Log logger = LogFactory.getLog(MobileTravelController.class);

	/**
	 * 买手列表页
	 * 
	 * @param user_id
	 *            当前登陆用户 TODO 验证登陆信息 当前登陆用户,未登录为0
	 * @param endId
	 *            加载下一页时候得到上一页的最后一个id
	 * @param limit
	 *            加载条数
	 * @return
	 */
	@RequestMapping(value = "/index", method = { RequestMethod.POST })
	@ResponseBody
	public Object index(HttpServletRequest request,
			HttpServletResponse response, @RequestParam("user_id") int userId,
			@RequestParam(value = "end_id", required = false) Long endId,
			@RequestParam(value = "limit", required = false) Integer limit) {
		if (endId == null || endId == 0) {
			endId = Long.MAX_VALUE;
		}
		if (limit == null || limit == 0) {
			limit = TravelConf.TRAVEL_LOAD_DEFAULTNUM;
		}
		List<Travel> rets = travelService.getTravelsByIndex(endId, limit);
		JSONArray jar = travelService.getTravelIndex(userId, rets);
		return ResultConf.getSuccessResult(jar, limit);
	}

	/**
	 * 买手列表页,根据国家来筛选
	 * 
	 * @param user_id
	 *            当前登陆用户
	 * 
	 * @return
	 */
	@RequestMapping(value = "/screenByCountry", method = { RequestMethod.POST })
	@ResponseBody
	public Object screenByCountry(HttpServletRequest request,
			HttpServletResponse response, @RequestParam("user_id") int userId,
			@RequestParam("country") String countryName,
			@RequestParam(value = "end_id", required = false) Long endId,
			@RequestParam(value = "limit", required = false) Integer limit) {
		if (endId == null || endId == 0) {
			endId = Long.MAX_VALUE;
		}
		if (limit == null || limit == 0) {
			limit = TravelConf.TRAVEL_LOAD_DEFAULTNUM;
		}
		List<Travel> rets = travelService.getTravelsByCountyName(countryName,
				endId, limit);
		JSONArray jar = travelService.getTravelIndex(userId, rets);
		return ResultConf.getSuccessResult(jar, limit);
	}

	/**
	 * 买手列表页,根据出发城市来筛选
	 * 
	 * @param user_id
	 *            当前登陆用户
	 * @return
	 */
	@RequestMapping(value = "/screenByCity", method = { RequestMethod.POST })
	@ResponseBody
	public Object screenByDepartureCity(HttpServletRequest request,
			HttpServletResponse response, @RequestParam("user_id") int userId,
			@RequestParam("city") String city,
			@RequestParam(value = "end_id", required = false) Long endId,
			@RequestParam(value = "limit", required = false) Integer limit) {
		if (endId == null || endId == 0) {
			endId = Long.MAX_VALUE;
		}
		if (limit == null || limit == 0) {
			limit = TravelConf.TRAVEL_LOAD_DEFAULTNUM;
		}
		city = StringUtils.trim(city);
		if (city.endsWith("市")) {
			city = city.substring(0, city.length() - 1);
		}
		List<Travel> rets = travelService.getTravelsByCity(city, endId, limit);
		JSONArray jar = travelService.getTravelIndex(userId, rets);
		return ResultConf.getSuccessResult(jar, limit);
	}

	/**
	 * 买手列表页,根据回国时间
	 * 
	 * @param user_id
	 *            当前登陆用户
	 * @return
	 */
	@RequestMapping(value = "/screenByTime", method = { RequestMethod.POST })
	@ResponseBody
	public Object screenByTime(HttpServletRequest request,
			HttpServletResponse response, @RequestParam("user_id") int userId,
			@RequestParam("type") int type,
			@RequestParam(value = "end_id", required = false) Long endId,
			@RequestParam(value = "limit", required = false) Integer limit) {
		if (endId == null || endId == 0) {
			endId = Long.MAX_VALUE;
		}
		if (limit == null || limit == 0) {
			limit = TravelConf.TRAVEL_LOAD_DEFAULTNUM;
		}

		List<Travel> rets = travelService.getTravelsByTime(type, endId, limit);
		JSONArray jar = travelService.getTravelIndex(userId, rets);
		return ResultConf.getSuccessResult(jar, limit);
	}

	/**
	 * 买手详情页
	 * 
	 * @param user_id
	 *            当前登陆用户
	 * @param travel_id
	 * @return
	 */
	@RequestMapping(value = "/getUserTravel", method = { RequestMethod.POST })
	@ResponseBody
	public Object userTravel(HttpServletRequest request,
			HttpServletResponse response, @RequestParam("user_id") int userId,
			@RequestParam("travel_id") long travelId) {
		JSONObject ret = travelService.getParsedUserTravel(userId, travelId);
		return ResultConf.getSuccessResult(ret);
	}

	/**
	 * 发布行程
	 * 
	 * @return
	 */
	@MobileLoginRequired
	@RequestMapping(value = "/publish", method = { RequestMethod.POST })
	@ResponseBody
	public Object publish(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam("user_id") int userId,
			@RequestParam("to_country") String toCountry,
			@RequestParam("resident") int resident,
			@RequestParam(value = "departure_city", required = false) String departureCity,
			@RequestParam("back_time") long back_time,
			@RequestParam("start_time") long start_time,
			@RequestParam(value = "description", required = false) String description) {
		// 添加商品s
		List<Good> goods = new ArrayList<Good>();
		// 转型为MultipartHttpRequest：
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		for (int i = 1;; i++) {
			String photoTag = "good_photos_" + i;
			String goodNameTag = "good_name_" + i;
			// 获得文件：
			List<MultipartFile> good_photos = multipartRequest
					.getFiles(photoTag);
			// 获得商品名称
			String goodName = request.getParameter(goodNameTag);
			if (StringUtils.isBlank(goodName) || good_photos == null
					|| good_photos.size() == 0) {
				break;
			}
			// 验证
			JSONObject validate = UploadService.getInstance().validate(
					good_photos);
			if (!ResultConf.isSuccess(validate)) {
				return validate;
			}
			// 保存商品
			Good good = goodService.saveGoodAndPhoto(goodName, good_photos);
			if (good != null) {
				goods.add(good);
			}
		}

		// 保存行程
		Travel travel = new Travel();
		travel.setUserId(userId);
		travel.setToCountry(toCountry);
		travel.setResident(resident);
		travel.setDepartureCity(departureCity);
		travel.setBack_time(back_time);
		travel.setStart_time(start_time);
		travel.setDescription(description);
		travelService.addTravel(travel);

		// 保存行程-商品
		for (Good good : goods) {
			TravelGood travelGood = new TravelGood();
			travelGood.setTravelGoodPK(new TravelGoodPK(travel.getId(), good
					.getId()));
			travelService.addTravelGood(travelGood);
		}

		return ResultConf.getSuccessResult(travel.getId());
	}

	/**
	 * 删除行程
	 * 
	 * @param userId
	 * @param travelId
	 * @return
	 */
	@MobileLoginRequired
	@RequestMapping(value = "/delete", method = { RequestMethod.POST })
	@ResponseBody
	public Object delete(HttpServletRequest request,
			HttpServletResponse response, @RequestParam("user_id") int userId,
			@RequestParam("travel_id") long travelId) {
		Travel travel = travelService.getTravel(travelId);
		if (travel == null || travel.getUserId() != userId
				|| travel.getDelete_flag() != 0) {
			return ResultConf.SOURCE_NOTEXIST_EXCEPTION;
		}
		travel.setDelete_flag(1);
		travelService.updateTravel(travel);
		travelService.afterDeleteTravel(travel);
		return ResultConf.getSuccessResult(travelId);
	}

}
