package com.renren.shopping.controllers.mobile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.renren.shopping.annotion.MobileLoginRequired;
import com.renren.shopping.conf.ResultConf;
import com.renren.shopping.model.CollectionPhoto;
import com.renren.shopping.model.CollectionPK;
import com.renren.shopping.model.Good;
import com.renren.shopping.model.GoodPhoto;
import com.renren.shopping.model.Travel;
import com.renren.shopping.model.TravelGood;
import com.renren.shopping.services.GoodService;
import com.renren.shopping.services.TaskService;
import com.renren.shopping.services.TravelService;
import com.renren.shopping.services.UserService;

/**
 * 商品相关
 * 
 * @author kk
 * 
 */
@Controller
@RequestMapping("/mobile/good")
public class MobileGoodController {
	@Autowired
	TravelService travelService;

	@Autowired
	TaskService taskService;

	@Autowired
	GoodService goodService;

	@Autowired
	UserService userService;

	public static Log logger = LogFactory.getLog(MobileGoodController.class);

	/**
	 * 收藏照片
	 * 
	 * @return
	 */
	@MobileLoginRequired
	@RequestMapping(value = "/addCollection", method = { RequestMethod.POST })
	@ResponseBody
	public Object addCollectionPhoto(HttpServletRequest request,
			HttpServletResponse response, @RequestParam("user_id") int userId,
			@RequestParam("photo_id") long photoId,
			@RequestParam("good_id") long goodId,
			@RequestParam("travel_id") long travelId) {
		if (userId == 0 || photoId == 0 || goodId == 0 || travelId == 0) {
			return ResultConf.PARAM_EXCEPTION;
		}
		if (goodService.getCollectionPhoto(userId, photoId) != null) {
			return ResultConf.getResult(1, "已收藏");
		}
		GoodPhoto goodPhoto = goodService.getGoodPhoto(photoId);
		if (goodPhoto == null || goodPhoto.getGoodId() != goodId) {
			return ResultConf.SOURCE_NOTEXIST_EXCEPTION;
		}
		Travel travel = travelService.getTravel(travelId);
		if (travel == null) {
			return ResultConf.SOURCE_NOTEXIST_EXCEPTION;
		}
		TravelGood travelGood = travelService.getTravelGood(travel.getId(),
				goodId);
		if (travelGood == null) {
			return ResultConf.SOURCE_NOTEXIST_EXCEPTION;
		}
		CollectionPhoto collectionGood = new CollectionPhoto();
		collectionGood.setCollectionPK(new CollectionPK(userId, photoId));
		collectionGood.setGoodId(goodId);
		collectionGood.setTravelId(travelId);
		goodService.addCollectionPhoto(collectionGood);
		return ResultConf.SUCCESSRET;
	}

	/**
	 * 取消收藏照片
	 * 
	 * @param userId
	 * @param goodId
	 * @return
	 */
	@MobileLoginRequired
	@RequestMapping(value = "/deleteCollection", method = { RequestMethod.POST })
	@ResponseBody
	public Object deleteCollectionPhoto(HttpServletRequest request,
			HttpServletResponse response, @RequestParam("user_id") int userId,
			@RequestParam("photo_id") long photoId) {
		if (userId == 0 || photoId == 0) {
			return ResultConf.PARAM_EXCEPTION;
		}
		CollectionPhoto collectionGood = goodService.getCollectionPhoto(userId,
				photoId);
		if (collectionGood == null) {
			return ResultConf.SOURCE_NOTEXIST_EXCEPTION;
		}
		goodService.deleteCollectionPhoto(collectionGood);
		return ResultConf.SUCCESSRET;
	}

	/**
	 * 得到商品照片信息
	 * 
	 * @param userId
	 * @param goodId
	 * @return
	 */
	@RequestMapping(value = "/getGoodInfo", method = RequestMethod.POST)
	@ResponseBody
	public Object getGoodInfo(HttpServletRequest request,
			HttpServletResponse response, @RequestParam("user_id") int userId,
			@RequestParam("good_id") long goodId) {
		Good good = goodService.getGood(goodId);
		JSONObject obj = new JSONObject();
		goodService.supplyJsonObject(obj, userId, good);
		return ResultConf.getSuccessResult(obj);
	}

	/**
	 * 得到未登录下商品照片信息
	 * 
	 * @param userId
	 * @param goodId
	 * @return
	 */
	@RequestMapping(value = "/getGoodInfo2", method = RequestMethod.POST)
	@ResponseBody
	public Object getGoodInfo2(HttpServletRequest request,
			HttpServletResponse response, @RequestParam("good_id") long goodId) {
		int userId = 0;
		Good good = goodService.getGood(goodId);
		JSONObject obj = new JSONObject();
		goodService.supplyJsonObject(obj, userId, good);
		return ResultConf.getSuccessResult(obj);
	}
}
