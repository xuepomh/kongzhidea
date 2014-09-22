package com.renren.shopping.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.renren.shopping.conf.GoodConf;
import com.renren.shopping.conf.UserConf;
import com.renren.shopping.dao.CollectionPhotoDao;
import com.renren.shopping.dao.GoodDao;
import com.renren.shopping.dao.GoodPhotoDao;
import com.renren.shopping.model.CollectionPhoto;
import com.renren.shopping.model.CollectionPK;
import com.renren.shopping.model.GoodPhoto;
import com.renren.shopping.model.Good;
import com.renren.shopping.model.Travel;
import com.renren.shopping.model.TravelGood;
import com.renren.shopping.model.UploadModel;
import com.renren.shopping.util.CommonUtil;

@Service
public class GoodService {

	@Autowired
	GoodPhotoDao goodPhotoDao;

	@Autowired
	GoodDao goodDao;

	@Autowired
	CollectionPhotoDao collectionPhotoDao;

	@Autowired
	TaskService taskService;

	@Autowired
	TravelService travelService;

	/****************************************************/
	public static final Log logger = LogFactory.getLog(GoodService.class);

	/****************** 商品照片 *************/
	public void addGoodPhoto(GoodPhoto goodPhoto) {
		goodPhotoDao.saveObject(goodPhoto);
	}

	public void updateGoodPhoto(GoodPhoto goodPhoto) {
		goodPhotoDao.updateObject(goodPhoto);
	}

	public void deleteGoodPhoto(GoodPhoto goodPhoto) {
		goodPhotoDao.deleteObject(goodPhoto);
	}

	public GoodPhoto getGoodPhoto(long id) {
		return goodPhotoDao.getObject(GoodPhoto.class, id);
	}

	/**
	 * 得到该商品对应的所有照片信息
	 * 
	 * @param goodId
	 * @return
	 */
	public List<GoodPhoto> getGoodPhotoListByGoodId(long goodId) {
		String hql = "from GoodPhoto where goodId=?";
		return goodPhotoDao.find(hql, goodId);
	}

	/**
	 * 得到该商品对应的所有照片信息
	 * 
	 * limit :start,:limit
	 * 
	 * @param goodId
	 * @return
	 */
	public List<GoodPhoto> getGoodPhotoListByGoodId(long goodId, int start,
			int limit) {
		return goodPhotoDao.getGoodPhotoListByGoodId(goodId, start, limit);
	}

	/************** 商品 **************/
	public void addGood(Good good) {
		goodDao.saveObject(good);
	}

	public void updateGood(Good good) {
		goodDao.updateObject(good);
	}

	public void deleteGood(Good good) {
		goodDao.deleteObject(good);
	}

	public Good getGood(long id) {
		return goodDao.getObject(Good.class, id);
	}

	/**
	 * 得到商品列表
	 * 
	 * @param ids
	 * @return
	 */
	public List<Good> getGoodList(List<Long> ids) {
		return goodDao.getGoodList(ids);
	}

	/****************** 收藏 照片，仅行程商品 ****************/

	public void addCollectionPhoto(CollectionPhoto collectionPhoto) {
		collectionPhotoDao.saveObject(collectionPhoto);
	}

	public CollectionPhoto getCollectionPhoto(int uid, long pid) {
		CollectionPK collectionPK = new CollectionPK(uid, pid);
		return collectionPhotoDao
				.getObject(CollectionPhoto.class, collectionPK);
	}

	public void deleteCollectionPhoto(CollectionPhoto collectionPhoto) {
		collectionPhotoDao.deleteObject(collectionPhoto);
	}

	/**
	 * 得到用户的收藏列表
	 * 
	 * @param uid
	 * @return
	 */
	public List<CollectionPhoto> getCollectionPhotoListByUserId(int uid) {
		String hql = "from CollectionPhoto where collectionPK.userId = :userId";
		return collectionPhotoDao.findByNamedParam(hql, "userId", uid);
	}

	/**
	 * 得到用户的收藏列表,按照时间倒序
	 * 
	 * @param uid
	 * @param page
	 *            从1开始
	 * @param limit
	 * @return
	 */
	public List<CollectionPhoto> getCollectionPhotoListByUserId(int uid,
			int page, int limit) {
		page = page <= 0 ? 1 : page;
		limit = limit <= 0 ? GoodConf.USER_COLLECTION_DEFAULT_NUM : limit;
		List<CollectionPhoto> list = getCollectionPhotoListByUserId(uid);
		Collections.sort(list, new Comparator<CollectionPhoto>() {

			@Override
			public int compare(CollectionPhoto o1, CollectionPhoto o2) {
				long deta = o2.getUpdate_time().getTime()
						- o1.getUpdate_time().getTime();
				return CommonUtil.getIntegerCompare0(deta);
			}
		});
		int start = (page - 1) * limit;
		int end = Math.min(list.size(), page * limit);
		if (start > list.size()) {
			return new ArrayList<CollectionPhoto>();
		}
		return list.subList(start, end);
	}

	/**
	 * 得到收藏该照片的人数
	 * 
	 * @param goodId
	 * @return
	 */
	public long getCollectionNumByPhotoId(long photoId) {
		return collectionPhotoDao.getCollectionNumByPhotoId(photoId);
	}

	/**
	 * 得到用户的收藏列表，解析后的
	 * 
	 * @param userId
	 * @return
	 */
	public JSONArray getParsedUserCollectionList(int currentUserId,
			List<CollectionPhoto> collections) {
		JSONArray jar = new JSONArray();
		for (CollectionPhoto collection : collections) {
			JSONObject obj = new JSONObject();
			// 收藏信息
			parseJsonCollection(obj, collection);
			// 商品信息
			Good good = getGood(collection.getGoodId());
			supplyMiniGoodJsonObject(obj, currentUserId, good);
			jar.add(obj);
		}
		return jar;
	}

	/**
	 * 将收藏信息加到JsonObject中
	 * 
	 * @param obj
	 * @param collection
	 */
	public void parseJsonCollection(JSONObject obj, CollectionPhoto collection) {
		JSONObject c = new JSONObject();
		if (collection == null) {
			return;
		}
		try {
			c.put(GoodConf.COLLECTION_USER_ID, collection.getCollectionPK()
					.getUserId());
			c.put(GoodConf.COLLECTION_PHOTO_ID, collection.getCollectionPK()
					.getPhotoId());
			c.put(GoodConf.COLLECTION_GOOD_ID, collection.getGoodId());
			c.put(GoodConf.COLLECTION_TRAVEL_ID, collection.getTravelId());
			Travel travel = travelService.getTravel(collection.getTravelId());
			c.put(GoodConf.COLLECTION_TRAVEL_DELETEFLAG,
					travel.getDelete_flag());
			c.put(GoodConf.COLLECTION_POPULARITY,
					getCollectionNumByPhotoId(collection.getCollectionPK()
							.getPhotoId()));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			c = new JSONObject();
		}
		obj.put(GoodConf.COLLECTIONKEY, c);
	}

	/***************************************************/

	/**
	 * 将good信息添加到JsonObject中
	 * 
	 * @param obj
	 * @param currentUserId
	 *            当前登陆用户，0表示未登录
	 * @param good
	 */
	public void supplyJsonObject(JSONObject obj, int currentUserId, Good good) {
		if (good == null) {
			return;
		}
		List<Good> goods = new ArrayList<Good>();
		goods.add(good);
		supplyJsonObject(obj, currentUserId, goods);
	}

	/**
	 * 将good信息添加到JsonObject中
	 * 
	 * @param obj
	 * @param currentUserId
	 *            当前登陆用户，0表示未登录
	 * @param goods
	 */
	public void supplyJsonObject(JSONObject obj, int currentUserId,
			List<Good> goods) {
		JSONArray jar = new JSONArray();
		if (goods == null || goods.isEmpty()) {
			return;
		}
		try {
			for (Good good : goods) {
				if (good == null) {
					continue;
				}
				JSONObject jgood = new JSONObject();
				List<GoodPhoto> photos = getGoodPhotoListByGoodId(good.getId());
				supplyGoodDetail(jgood, currentUserId, good, photos);

				jar.add(jgood);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			jar = new JSONArray();
		}

		obj.put(GoodConf.GOODKEY, jar);
	}

	/**
	 * 填充商品信息
	 * 
	 * @param jgood
	 * @param currentUserId
	 * @param good
	 * @param photos
	 */
	private void supplyGoodDetail(JSONObject jgood, int currentUserId,
			Good good, List<GoodPhoto> photos) {
		jgood.put(GoodConf.GOOD_ID, good.getId());
		jgood.put(GoodConf.GOOD_NAME, good.getName());
		jgood.put(GoodConf.GOOD_DESCRIPTION, good.getDescription());
		// 默认图是第一张图片
		jgood.put(GoodConf.GOODD_EFAULT_PHOTO_URL, good.getDefaultPhoto());
		jgood.put(GoodConf.GOOD_DEFAULT_MAIN_URL, good.getDefaultMainUrl());
		jgood.put(GoodConf.GOOD_DEFAULT_HEAD_URL, good.getDefaultHeadUrl());
		jgood.put(GoodConf.GOOD_DEFAULT_TINY_URL, good.getDefaultTinyUrl());

		// if (photos != null && !photos.isEmpty()) {
		// jgood.put(GoodConf.GOODDEFAULTPHOTOURL, photos.get(0)
		// .getPhoto_url());
		// } else {
		// List<GoodPhoto> tmps = getGoodPhotoListByGoodId(good.getId(), 0, 1);
		// if (!tmps.isEmpty()) {
		// jgood.put(GoodConf.GOODDEFAULTPHOTOURL, tmps.get(0)
		// .getPhoto_url());
		// }
		// }
		supplyGoodPhotoDetail(jgood, photos);

		// 收藏商品信息
		jgood.put(UserConf.CURRENT_USER_ID, currentUserId);
		if (currentUserId == 0) {
			jgood.put(GoodConf.GOOD_IS_COLLECTIONED, 0);
		} else {
			CollectionPhoto cg = getCollectionPhoto(currentUserId, good.getId());
			jgood.put(GoodConf.GOOD_IS_COLLECTIONED, cg == null ? 0 : 1);
		}
	}

	/**
	 * 填充商品照片信息
	 * 
	 * @param jgood
	 * @param photos
	 */
	private void supplyGoodPhotoDetail(JSONObject jgood, List<GoodPhoto> photos) {
		if (photos == null) {
			return;
		}
		JSONArray jphotos = new JSONArray();
		for (GoodPhoto photo : photos) {
			JSONObject jphoto = new JSONObject();
			jphoto.put(GoodConf.GOODPHOTOID, photo.getId());
			jphoto.put(GoodConf.GOOD_PHOTO_URL, photo.getPhoto_url());
			jphoto.put(GoodConf.GOOD_MAIN_URL, photo.getMain_url());
			jphoto.put(GoodConf.GOOD_HEAD_URL, photo.getHead_url());
			jphoto.put(GoodConf.GOOD_TINY_URL, photo.getTiny_url());
			jphoto.put(GoodConf.GOODPHOTODESCRIPTION, photo.getDescription());
			jphotos.add(jphoto);
		}
		jgood.put(GoodConf.GOODPHOTOS, jphotos);
	}

	/**
	 * 将good 基本 信息 添加到JsonObject中
	 * 
	 * @param obj
	 * @param currentUserId
	 *            当前登陆用户，0表示未登录
	 * @param good
	 */
	public void supplyMiniGoodJsonObject(JSONObject obj, int currentUserId,
			Good good) {
		if (good == null) {
			return;
		}
		List<Good> goods = new ArrayList<Good>();
		goods.add(good);
		supplyMiniGoodJsonObject(obj, currentUserId, goods);
	}

	/**
	 * 将good 基本 信息 添加到JsonObject中
	 * 
	 * @param obj
	 * @param currentUserId
	 *            当前登陆用户，0表示未登录
	 * @param goods
	 */
	public void supplyMiniGoodJsonObject(JSONObject obj, int currentUserId,
			List<Good> goods) {
		JSONArray jar = new JSONArray();
		if (goods == null || goods.isEmpty()) {
			return;
		}
		try {
			for (Good good : goods) {
				if (good == null) {
					continue;
				}
				JSONObject jgood = new JSONObject();
				supplyGoodDetail(jgood, currentUserId, good, null);

				jar.add(jgood);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			jar = new JSONArray();
		}

		obj.put(GoodConf.GOODKEY, jar);
	}

	/**
	 * 上传图片，并且生成good类，插入到数据库中
	 * 
	 * @param name
	 * @param good_photos
	 * @return
	 */
	public Good saveGoodAndPhoto(String name, List<MultipartFile> good_photos) {
		// 上传图片
		List<UploadModel> models = UploadService.getInstance().upload(
				good_photos);
		// 保存good
		Good good = new Good();
		good.setName(name);
		if (models.size() > 0) {
			UploadModel model = models.get(0);
			good.setDefaultPhoto(model.getOriginal_url());
			good.setDefaultMainUrl(model.getMain_url());
			good.setDefaultHeadUrl(model.getHead_url());
			good.setDefaultTinyUrl(model.getTiny_url());
		}
		addGood(good);
		// 保存good_photos
		for (UploadModel model : models) {
			GoodPhoto photo = new GoodPhoto();
			photo.setGoodId(good.getId());
			photo.setPhoto_url(model.getOriginal_url());
			photo.setMain_url(model.getMain_url());
			photo.setHead_url(model.getHead_url());
			photo.setTiny_url(model.getTiny_url());
			addGoodPhoto(photo);
		}
		return good;
	}
}
