package com.renren.shopping.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.renren.shopping.conf.CountryConf;
import com.renren.shopping.conf.ResultConf;
import com.renren.shopping.conf.GoodConf;
import com.renren.shopping.conf.TravelConf;
import com.renren.shopping.conf.UserConf;
import com.renren.shopping.dao.TravelDao;
import com.renren.shopping.dao.TravelGoodDao;
import com.renren.shopping.model.Comment;
import com.renren.shopping.model.Country;
import com.renren.shopping.model.Good;
import com.renren.shopping.model.GoodPhoto;
import com.renren.shopping.model.Travel;
import com.renren.shopping.model.TravelGood;
import com.renren.shopping.model.TravelGoodPK;
import com.renren.shopping.model.User;
import com.renren.shopping.util.CommonUtil;

@Service
public class TravelService {

	@Autowired
	TravelDao travelDao;

	@Autowired
	TravelGoodDao travelGoodDao;

	/***************************/

	@Autowired
	UserService userService;

	@Autowired
	GoodService goodService;

	@Autowired
	CommentService commentService;

	@Autowired
	CountryService countryService;

	private static final Log logger = LogFactory.getLog(TravelService.class);

	/*************** 行程 ****************/
	public void addTravel(Travel travel) {
		travelDao.saveObject(travel);
	}

	public void updateTravel(Travel travel) {
		travelDao.updateObject(travel);
	}

	public void deleteTravel(Travel travel) {
		travelDao.deleteObject(travel);
	}

	public Travel getTravel(long id) {
		return travelDao.getObject(Travel.class, id);
	}

	/**
	 * 得到travel列表
	 * 
	 * @param ids
	 * @return
	 */
	public List<Travel> getTravelList(List<Long> ids) {
		String hql = String.format("  from Travel where id in (%s)",
				CommonUtil.longListToString(ids));
		return travelDao.find(hql);
	}

	/*************** 行程商品 ****************/
	public void addTravelGood(TravelGood travelGood) {
		travelGoodDao.saveObject(travelGood);
	}

	public void updateTravelGood(TravelGood travelGood) {
		travelGoodDao.updateObject(travelGood);
	}

	public void deleteTravelGood(TravelGood travelGood) {
		travelGoodDao.deleteObject(travelGood);
	}

	public TravelGood getTravelGood(long travelId, long goodId) {
		TravelGoodPK travelGoodPK = new TravelGoodPK(travelId, goodId);
		return travelGoodDao.getObject(TravelGood.class, travelGoodPK);
	}

	/**
	 * 通过GoodID得到行程-商品
	 * 
	 * @param goodId
	 * @return
	 */
	public TravelGood getTravelGoodByGoodId(long goodId) {
		String hql = " from TravelGood where travelGoodPK.goodId = ?";
		List<TravelGood> list = travelGoodDao.find(hql, goodId);
		if (list.size() == 0) {
			return null;
		}
		return list.get(0);
	}

	/**
	 * 通过travelID得到行程-商品 id列表
	 * 
	 * @param travelId
	 * @return
	 */
	public List<TravelGood> getTravelGoodIdList(long travelId) {
		String hql = " from TravelGood where travelGoodPK.travelId = ?";
		return travelGoodDao.find(hql, travelId);
	}

	/**
	 * 通过travelID得到行程-商品 商品列表
	 * 
	 * @param travelId
	 * @return
	 */
	public List<Good> getGoodList(long travelId) {
		List<TravelGood> travelGoods = getTravelGoodIdList(travelId);
		// 转成good id列表
		List<Long> ids = new ArrayList<Long>();
		for (TravelGood travelGood : travelGoods) {
			ids.add(travelGood.getTravelGoodPK().getGoodId());
		}
		return goodService.getGoodList(ids);
	}

	/************************************************/

	/**
	 * 得到所有的行程
	 * 
	 * @return
	 */
	public List<Travel> getAllTravels() {
		String hql = " from Travel ";
		List<Travel> travels = travelDao.find(hql);
		return travels;
	}

	/**
	 * 得到帮带首页数据
	 */
	public List<Travel> getTravelsByIndex(long endId, int limit) {
		String hql = " from Travel where id < ? and delete_flag = 0 order by id desc";
		return travelDao.getListForPage2(hql, limit, endId);
	}

	/**
	 * 得到帮带-筛选国家
	 */
	public List<Travel> getTravelsByCountyName(String countryName, long endId,
			int limit) {
		String hql = " from Travel where toCountry = ? and id < ? and delete_flag = 0 order by id desc";
		return travelDao.getListForPage2(hql, limit, countryName, endId);
	}

	/**
	 * 得到帮带-筛选城市
	 */
	public List<Travel> getTravelsByCity(String city, long endId, int limit) {
		String hql = " from Travel where departure_city like ? and id < ? and delete_flag = 0 order by id desc";
		return travelDao.getListForPage2(hql, limit, city + "%", endId);
	}

	/**
	 * 得到帮带-筛选时间
	 */
	public List<Travel> getTravelsByTime(int type, long endId, int limit) {
		if (!TravelConf.back_days.containsKey(type)) {
			return new ArrayList<Travel>();
		}

		List<Travel> travels = new ArrayList<Travel>();
		if (TravelConf.back_days.get(type) == 0) {// 常驻
			String hql = " from Travel where resident=1 and id < ? and delete_flag = 0 order by id desc";
			travels = travelDao.getListForPage2(hql, limit, endId);
		} else {
			long now = new Date().getTime();
			long back_time = now + TravelConf.back_days.get(type)
					* TravelConf.DAY_UNIX_TIME;
			String hql = " from Travel where back_time < ? and id < ? and delete_flag = 0 order by id desc";
			travels = travelDao.getListForPage2(hql, limit, back_time, endId);
		}
		return travels;
	}

	/**
	 * 将得到帮带首页数据解析为json格式
	 * 
	 * @param rets
	 * @return
	 */
	public JSONArray getTravelIndex(int currentUserId, List<Travel> rets) {
		JSONArray jar = new JSONArray();
		for (Travel travel : rets) {
			try {
				JSONObject obj = new JSONObject();
				// user信息
				User user = userService.getUser(travel.getUserId());
				userService.supplyJsonObject(obj, currentUserId, user);
				// travel信息
				supplyJsonObject(obj, travel);
				// good信息
				List<Good> goods = getGoodList(travel.getId());
				goodService.supplyMiniGoodJsonObject(obj, currentUserId, goods);
				jar.add(obj);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		return jar;
	}

	/**
	 * 根据是否是常驻推断from是否为空
	 * 
	 * @param travel
	 * @return
	 */
	private String getTravelFromCity(Travel travel) {
		return travel.getResident() == 0 ? travel.getDepartureCity() : "";
	}

	/********************************************************/
	/**
	 * 得到买手详情页数据,具体一个行程
	 * 
	 * @param currentUserId
	 *            当前登陆用户
	 * @param id
	 *            行程id
	 * @return
	 */
	public JSONObject getParsedUserTravel(int currentUserId, long id) {
		JSONObject obj = new JSONObject();
		try {
			Travel travel = getTravel(id);
			if (!travelExists(travel)) {
				return ResultConf.NULL;
			}
			// user信息
			User user = userService.getUser(travel.getUserId());
			userService.supplyJsonObject(obj, currentUserId, user);
			// travel信息
			supplyJsonObject(obj, travel);
			// good信息
			List<Good> goods = getGoodList(travel.getId());
			goodService.supplyJsonObject(obj, currentUserId, goods);
			// comment信息
			List<Comment> comments = commentService.getCommentListDesc(
					travel.getId(), 0, TravelConf.TRAVEL_SHOW_COMMENTNUM);
			commentService.supplyJsonObject(obj, currentUserId, comments);

			return obj;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResultConf.NULL;
		}
	}

	/**
	 * 判断travel是否存在
	 * 
	 * @param travel
	 * @return true存在，false不存在
	 */
	public boolean travelExists(Travel travel) {
		if (travel == null || travel.getDelete_flag() != 0) {
			return false;
		}
		return true;
	}

	/**
	 * 判断travel是否存在
	 * 
	 * @param travel
	 * @return true存在，false不存在
	 */
	public boolean travelExists(long travelId) {
		return travelExists(getTravel(travelId));
	}

	/**
	 * 将travel信息添加到JsonObject中
	 * 
	 * @param obj
	 * @param travel
	 * @param country
	 */
	public void supplyJsonObject(JSONObject obj, Travel travel) {
		JSONObject t = new JSONObject();
		if (travel == null) {
			return;
		}
		try {
			t.put(TravelConf.TRAVEL_ID, travel.getId());
			t.put(TravelConf.TRAVEL_DESCRIPTION, travel.getDescription());
			t.put(TravelConf.TRAVEL_START_TIME, travel.getStart_time());
			t.put(TravelConf.TRAVEL_BACK_TIME, travel.getBack_time());
			t.put(TravelConf.FROM, getTravelFromCity(travel));
			t.put(TravelConf.TO, travel.getToCountry());
			t.put(CountryConf.COUNTRY_FLAG,
					countryService.getCountryFlag(travel.getToCountry()));
			t.put(UserConf.USER_ID, travel.getUserId());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			t = new JSONObject();
		}

		obj.put(TravelConf.TRAVELKEY, t);
	}

	/**
	 * 得到某用户的行程列表
	 */
	public List<Travel> getTravelsByUser(int userId, long endId, int limit) {
		String hql = " from Travel where userId = ? and id < ? and delete_flag = 0 order by id desc";
		return travelDao.getListForPage2(hql, limit, userId, endId);
	}

	/**
	 * 将用户行程列表数据解析为json格式
	 * 
	 * @param rets
	 * @return
	 */
	public JSONArray getParsedUserTravelList(int currentUserId,
			List<Travel> rets) {
		JSONArray jar = new JSONArray();
		for (Travel travel : rets) {
			try {
				JSONObject obj = new JSONObject();
				// travel信息
				supplyJsonObject(obj, travel);
				// good信息
				List<Good> goods = getGoodList(travel.getId());
				goodService.supplyMiniGoodJsonObject(obj, currentUserId, goods);
				jar.add(obj);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		return jar;
	}

	/**
	 * 删除travel后续操作
	 * 
	 * @param travel
	 */
	public void afterDeleteTravel(Travel travel) {

	}
}
