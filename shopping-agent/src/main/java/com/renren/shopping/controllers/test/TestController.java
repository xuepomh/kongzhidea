package com.renren.shopping.controllers.test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.support.ServletContextResource;
import org.springframework.web.servlet.ModelAndView;

import com.renren.shopping.annotion.MobileLoginRequired;
import com.renren.shopping.dao.AreaDao;
import com.renren.shopping.model.AddressOther;
import com.renren.shopping.model.AddressUser;
import com.renren.shopping.model.Area;
import com.renren.shopping.model.CollectionPhoto;
import com.renren.shopping.model.CollectionPK;
import com.renren.shopping.model.Comment;
import com.renren.shopping.model.Continent;
import com.renren.shopping.model.Country;
import com.renren.shopping.model.Good;
import com.renren.shopping.model.GoodPhoto;
import com.renren.shopping.model.Province;
import com.renren.shopping.model.Task;
import com.renren.shopping.model.Travel;
import com.renren.shopping.model.TravelGood;
import com.renren.shopping.model.TravelGoodPK;
import com.renren.shopping.model.User;
import com.renren.shopping.model.UserDetail;
import com.renren.shopping.model.UserFollows;
import com.renren.shopping.model.UserFollowsPK;
import com.renren.shopping.services.AddressService;
import com.renren.shopping.services.CommentService;
import com.renren.shopping.services.CountryService;
import com.renren.shopping.services.GoodService;
import com.renren.shopping.services.SearchService;
import com.renren.shopping.services.TaskService;
import com.renren.shopping.services.TravelService;
import com.renren.shopping.services.UserService;
import com.renren.shopping.util.CookieManager;
import com.renren.shopping.util.MD5Util;
import com.renren.shopping.util.ResourceUtil;

/**
 * 第一次学习spring-mvc和hibernate，有使用不当的地方请多多见谅
 * 
 * 加一个字段 需要改的地方有 model xml controller conf supply jsp
 * 
 * @author kk
 * 
 */
@Controller
@RequestMapping("/test")
public class TestController {

	@Autowired
	UserService userService;

	@Autowired
	CountryService countryService;

	@Autowired
	AddressService addressService;

	@Autowired
	GoodService goodService;

	@Autowired
	CommentService commentService;

	@Autowired
	TaskService taskService;

	@Autowired
	TravelService travelService;

	@Autowired
	AreaDao areaDao;

	@Autowired
	SearchService searchService;

	public static final Log logger = LogFactory.getLog(TestController.class);
	public static final Log logger_search = LogFactory.getLog("search");

	/*********************************************************************/
	/**
	 * 测试专用
	 * 
	 * @param userId
	 * @return
	 */
	@RequestMapping("/u/{userId}")
	@ResponseBody
	public Object getUser(@PathVariable("userId") int userId) {
		User user = userService.getUser(userId);
		if (user == null) {
			return "null";
		}
		logger.info(user.toString());
		return user.toString();
	}

	@RequestMapping("/u/add")
	@ResponseBody
	public Object addUser() {
		User user = new User();
		user.setAccount("kongzhidea4");
		user.setPassword_md5(MD5Util.md5("kk"));
		user.setName("孔智慧");
		user.setDescription("没有描述");
		UserDetail userDetail = new UserDetail();
		userDetail.setQq("563");
		user.setUserDetail(userDetail);
		userService.addUser(user);
		return "ok";
	}

	@RequestMapping("/u/update")
	@ResponseBody
	public Object updateUser() {
		User user = userService.getUser(3);
		if (user == null) {
			return "null";
		}
		user.getUserDetail().setEmail("563@qq");
		user.getUserDetail().setAge(21);
		user.getUserDetail().setQq(null);
		userService.updateUser(user);
		return "ok";
	}

	@RequestMapping("/u/update2/{userId}")
	@ResponseBody
	public Object update2User(@PathVariable("userId") int userId) {
		User user = new User();
		user.setId(userId);
		// 这里要检查not-null字段
		user.setAccount("aa");
		user.setPassword_md5("dd");
		user.setName("df");
		UserDetail userDetail = new UserDetail();
		userDetail.setAge(1);
		user.setUserDetail(userDetail);
		userService.updateUser(user);
		return "ok";
	}

	@RequestMapping("/u/update3/{userId}")
	@ResponseBody
	public Object update3User(@PathVariable("userId") int userId) {
		User user = userService.getUser(userId);
		if (user == null) {
			return "null";
		}
		user.setHead_url("www");
		userService.updateUser(user);
		return "ok";
	}

	@RequestMapping("/u/delete/{userId}")
	@ResponseBody
	public Object deleteUser(@PathVariable("userId") int userId) {
		User user = userService.getUser(userId);
		if (user == null) {
			return "null";
		}
		userService.deleteUser(user);
		return "ok";
	}

	@RequestMapping("/u/delete2/{userId}")
	@ResponseBody
	public Object delete2User(@PathVariable("userId") int userId) {
		User user = new User();
		user.setId(userId);
		// 这里要检查not-null字段
		user.setAccount("aa");
		user.setPassword_md5("dd");
		user.setName("df");
		userService.deleteUser(user);
		return "ok";
	}

	@RequestMapping("/att/add/{fanId}/{userId}")
	@ResponseBody
	public Object attUser(@PathVariable("fanId") int fanId,
			@PathVariable("userId") int userId) {
		UserFollows userFollows = new UserFollows();
		UserFollowsPK userFollowsPK = new UserFollowsPK();
		userFollowsPK.setFanId(fanId);
		userFollowsPK.setUserId(userId);
		userFollows.setUserFollowsPK(userFollowsPK);
		userService.attention(userFollows);
		return "ok";
	}

	@RequestMapping("/att/get/f/{fanId}")
	@ResponseBody
	public Object getAttUser(@PathVariable("fanId") int fanId) {
		List<UserFollows> list = userService.getFanAttentionUserIds(fanId);
		if (list == null) {
			return "null";
		}
		return list.toString();
	}

	@RequestMapping("/att/get/u/{userId}")
	@ResponseBody
	public Object getAttUser2(@PathVariable("userId") int userId) {
		List<UserFollows> list = userService.getUserFollowedFansId(userId);
		if (list == null) {
			return "null";
		}
		return list.toString();
	}

	@RequestMapping("/att/get/u2/{userId}")
	@ResponseBody
	public Object getAttUser22(@PathVariable("userId") int userId) {
		List<User> list = userService.getFollowsFans(userId);
		if (list == null) {
			return "null";
		}
		return list.toString();
	}

	@RequestMapping("/att/get/o/{fanId}/{userId}")
	@ResponseBody
	public Object getAttUser3(@PathVariable("fanId") int fanId,
			@PathVariable("userId") int userId) {
		UserFollows userFollows = userService.getUserFollows(fanId, userId);
		if (userFollows == null) {
			return "null";
		}
		return userFollows.toString();
	}

	@RequestMapping("/att/d/{fanId}/{userId}")
	@ResponseBody
	public Object getAttUser4(@PathVariable("fanId") int fanId,
			@PathVariable("userId") int userId) {
		UserFollows userFollows = userService.getUserFollows(fanId, userId);
		if (userFollows == null) {
			return "null";
		}
		userService.deleteUserFollows(fanId, userId);
		return "ok";
	}

	@RequestMapping("/conti/a")
	@ResponseBody
	public Object addContinent() {
		String[] names = new String[] { "欧洲", "非洲", "北美洲", "南美洲", "大洋洲" };
		for (String name : names) {
			Continent continent = new Continent();
			continent.setName(name);
			countryService.addContinent(continent);
		}
		return "ok";
	}

	@RequestMapping("/conti/g/{cid}")
	@ResponseBody
	public Object getContinent(@PathVariable("cid") int cid) {
		Continent continent = countryService.getContinent(cid);
		if (continent == null) {
			return "null";
		}
		for (Country country : continent.getCountries()) {
			logger.info(country);
		}
		return continent.toString();
	}

	@RequestMapping("/conti/d/{cid}")
	@ResponseBody
	public Object delContinent(@PathVariable("cid") int cid) {
		Continent continent = countryService.getContinent(cid);
		if (continent == null) {
			return "null";
		}
		countryService.deleteContinent(continent);
		logger.info(continent);
		return "ok";
	}

	@RequestMapping("/contr/a/{country}/{cid}")
	@ResponseBody
	public Object addCountry(@PathVariable("country") String countryName,
			@PathVariable("cid") int cid) {
		Country country = new Country();
		country.setName(countryName);
		Continent continent = countryService.getContinent(cid);
		country.setContinent(continent);
		countryService.addCountry(country);
		return "ok";
	}

	@RequestMapping("/contr/g/{id}")
	@ResponseBody
	public Object getCountry(@PathVariable("id") int id) {
		Country country = countryService.getCountry(id);
		if (country == null) {
			return "null";
		}
		return country.toString();
	}

	@RequestMapping("/contr/d/{id}")
	@ResponseBody
	public Object delCountry(@PathVariable("id") int id) {
		Country country = countryService.getCountry(id);
		if (country == null) {
			return "null";
		}
		countryService.deleteCountry(country);
		return "ok";
	}

	@RequestMapping("/addruser/a")
	@ResponseBody
	public Object addAddressUser() {
		AddressUser aur = new AddressUser();
		aur.setUserId(1);
		aur.setReceiverName("kk");
		aur.setCountryId(2);
		aur.setAddressDetail("beijing");
		aur.setZipcode("111");
		aur.setTel("222");
		addressService.addAddressUser(aur);
		return "ok";
	}

	@RequestMapping("/addrother/a")
	@ResponseBody
	public Object addAddressOther() {
		AddressOther aur = new AddressOther();
		aur.setUserId(1);
		aur.setReceiverName("kk");
		aur.setCountryId(2);
		aur.setAddressDetail("beijing");
		aur.setZipcode("111");
		aur.setTel("222");
		addressService.addAddressOther(aur);
		return "ok";
	}

	@RequestMapping("/goodphoto/a")
	@ResponseBody
	public Object addGoodPhoto() {
		GoodPhoto goodPhoto = new GoodPhoto();
		goodPhoto.setGoodId(1);
		goodPhoto.setPhoto_url("url");
		goodPhoto.setDescription("我是描述");
		goodService.addGoodPhoto(goodPhoto);
		return "ok";
	}

	@RequestMapping("/goodphoto/g/{id}")
	@ResponseBody
	public Object getGoodPhoto(@PathVariable("id") long id) {
		GoodPhoto goodPhoto = goodService.getGoodPhoto(id);
		if (goodPhoto == null) {
			return "null";
		}
		return goodPhoto.toString();
	}

	@RequestMapping("/good/a")
	@ResponseBody
	public Object addGood() {
		Good good = new Good();
		good.setName("商品1");
		goodService.addGood(good);
		return "ok";
	}

	@RequestMapping("/good/g/{id}")
	@ResponseBody
	public Object getGood(@PathVariable("id") long id) {
		Good good = goodService.getGood(id);
		if (good == null) {
			return "null";
		}
		return good.toString();
	}

	@RequestMapping("/goodc/a/{uid}/{gid}")
	@ResponseBody
	public Object addGoodC(@PathVariable("uid") int uid,
			@PathVariable("gid") long gid) {
		CollectionPhoto collectionPhoto = new CollectionPhoto();
		CollectionPK collectionPK = new CollectionPK(uid, gid);
		collectionPhoto.setCollectionPK(collectionPK);
		goodService.addCollectionPhoto(collectionPhoto);
		return "ok";
	}

	@RequestMapping("/goodc/g/{uid}/{gid}")
	@ResponseBody
	public Object getGoodC(@PathVariable("uid") int uid,
			@PathVariable("gid") long gid) {
		CollectionPhoto collectionPhoto = goodService.getCollectionPhoto(uid,
				gid);
		if (collectionPhoto == null) {
			return "null";
		}
		return collectionPhoto.toString();
	}

	@RequestMapping("/goodc/d/{uid}/{gid}")
	@ResponseBody
	public Object deleteGoodC(@PathVariable("uid") int uid,
			@PathVariable("gid") long gid) {
		CollectionPhoto collectionPhoto = goodService.getCollectionPhoto(uid,
				gid);
		if (collectionPhoto == null) {
			return "null";
		}
		goodService.deleteCollectionPhoto(collectionPhoto);
		return "ok";
	}

	@RequestMapping("/comment/a")
	@ResponseBody
	public Object addComment() {
		Comment comment = new Comment();
		comment.setUserId(1);
		comment.setTravelId(1);
		comment.setContent("评论1");
		comment.setScore(5);
		commentService.addComment(comment);
		return "ok";
	}

	@RequestMapping("/comment/g/{id}")
	@ResponseBody
	public Object getComment(@PathVariable("id") long id) {
		Comment comment = commentService.getComment(id);
		if (comment == null) {
			return "null";
		}
		return comment.toString();
	}

	@RequestMapping("/comment/d/{id}")
	@ResponseBody
	public Object deleteComment(@PathVariable("id") long id) {
		Comment comment = commentService.getComment(id);
		if (comment == null) {
			return "null";
		}
		commentService.deleteComment(comment);
		return "ok";
	}

	@RequestMapping("/task/a")
	@ResponseBody
	public Object addTask() {
		Task task = new Task();
		task.setUserId(1);
		task.setTitle("title");
		task.setGratuity(0.1);
		task.setDeadline(new Date().getTime());
		task.setMessage("留言");
		task.setGoodCountry("中国");
		task.setMoney(123.3);
		task.setGoodNumber(1);
		task.setAddressOtherId(1);
		task.setGoodId(1);
		taskService.addTask(task);
		return "ok";
	}

	@RequestMapping("/task/g/{id}")
	@ResponseBody
	public Object getTask(@PathVariable("id") long id) {
		Task task = taskService.getTask(id);
		if (task == null) {
			return "null";
		}
		return task.toString();
	}

	@RequestMapping("/task/d/{id}")
	@ResponseBody
	public Object deleteTask(@PathVariable("id") long id) {
		Task task = taskService.getTask(id);
		if (task == null) {
			return "null";
		}
		taskService.deleteTask(task);
		return "ok";
	}

	@RequestMapping("/travel/a")
	@ResponseBody
	public Object addTravel() {
		Travel travel = new Travel();
		travel.setUserId(1);
		travel.setResident(1);
		travel.setToCountry("韩国");
		travel.setDepartureCity("邯郸市");
		travel.setBack_time(new Date().getTime());
		travel.setStart_time(new Date().getTime());
		travel.setDescription("描述");
		travelService.addTravel(travel);
		return "ok";
	}

	@RequestMapping("/travel/g/{id}")
	@ResponseBody
	public Object getTravel(@PathVariable("id") long id) {
		Travel travel = travelService.getTravel(id);
		if (travel == null) {
			return "null";
		}
		return travel.toString();
	}

	@RequestMapping("/travel/d/{id}")
	@ResponseBody
	public Object deleteTravel(@PathVariable("id") long id) {
		Travel travel = travelService.getTravel(id);
		if (travel == null) {
			return "null";
		}
		travelService.deleteTravel(travel);
		return "ok";
	}

	@RequestMapping("/travel_good/a/{tid}/{gid}")
	@ResponseBody
	public Object addTravelGood(@PathVariable("tid") long travelId,
			@PathVariable("gid") long goodId) {
		TravelGood travelGood = new TravelGood();
		TravelGoodPK travelGoodPK = new TravelGoodPK(travelId, goodId);
		travelGood.setTravelGoodPK(travelGoodPK);
		travelService.addTravelGood(travelGood);
		return "ok";
	}

	@RequestMapping("/travel_good/g/{tid}/{gid}")
	@ResponseBody
	public Object getTravelGood(@PathVariable("tid") long travelId,
			@PathVariable("gid") long goodId) {
		TravelGood travelGood = travelService.getTravelGood(travelId, goodId);
		if (travelGood == null) {
			return "null";
		}
		return travelGood.toString();
	}

	@RequestMapping("/travel/g2/{tid}/{tid2}")
	@ResponseBody
	public Object getTravelGood2(@PathVariable("tid") long travelId,
			@PathVariable("tid2") long tid2) {
		List<Long> ids = new ArrayList<Long>();
		ids.add(travelId);
		ids.add(tid2);
		List<Travel> travels = travelService.getTravelList(ids);
		if (travels == null) {
			return "null";
		}
		return travels.toString();
	}

	@RequestMapping("/travel_good/d/{tid}/{gid}")
	@ResponseBody
	public Object deleteTravelGood(@PathVariable("tid") long travelId,
			@PathVariable("gid") long goodId) {
		TravelGood travelGood = travelService.getTravelGood(travelId, goodId);
		if (travelGood == null) {
			return "null";
		}
		travelService.deleteTravelGood(travelGood);
		return travelGood.toString();
	}

	@RequestMapping("/area/a")
	@ResponseBody
	public Object addArea() {
		File file = new File(
				"C:\\Users\\Administrator.PC-20130106WFJK\\Desktop\\codes");
		List<String> list = new ArrayList<String>();
		try {
			list = FileUtils.readLines(file, "utf-8");
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			return "error";
		}
		for (String s : list) {
			// System.out.println(s);
			String code = StringUtils.split(s, " ")[0];
			String name = StringUtils.split(s, " ")[1];
			areaDao.insert(code, name);
		}
		return list.toString();
	}

	@RequestMapping("/area/g/{code}")
	@ResponseBody
	public Object getArea(@PathVariable("code") String code) {
		Area area = areaDao.getArea(code);
		if (area == null) {
			return "null";
		}
		return area.toString();
	}

	@RequestMapping("/area/l/{code}")
	@ResponseBody
	public Object getListArea(@PathVariable("code") String code) {
		List<Area> area = areaDao.getAreaList(code + "%");
		if (area == null) {
			return "null";
		}
		return area.toString();
	}

	@RequestMapping("/area/generate")
	@ResponseBody
	public Object getGenerate() {
		JSONArray jar = new JSONArray();
		for (int i = 11; i < 70; i++) {
			String code = i + "00";
			List<Area> list = areaDao.getAreaList(code + "%");
			if (list.isEmpty()) {
				continue;
			}
			for (Area area : list) {
				JSONObject obj = JSONObject.fromObject(area);
				JSONArray sjar = new JSONArray();
				for (int j = 1; j <= 99; j++) {
					String cd = "";
					if (j < 10) {
						cd = i + "0" + j;
					} else {
						cd = i + "" + j;
					}
					List<Area> slt = areaDao.getAreaList(cd + "%");
					for (Area sa : slt) {
						JSONObject sobj = JSONObject.fromObject(sa);
						sjar.add(sobj);
						break;
					}
				}
				obj.put("sublist", sjar);
				jar.add(obj);
			}
		}
		return jar.toString();
	}

	@RequestMapping("/area/genecity")
	@ResponseBody
	public Object getGenerateArea() {
		return ResourceUtil.getCityInfos().toString();
	}

	@RequestMapping("/add_task")
	public ModelAndView add_task() {
		ModelAndView mav = new ModelAndView("test/add_task");
		List<User> users = userService.getUserList(0, 100);
		mav.addObject("users", users);
		return mav;
	}

	@RequestMapping("/set_headurl")
	public ModelAndView set_headurl() {
		ModelAndView mav = new ModelAndView("test/set_headurl");
		List<User> users = userService.getUserList(0, 100);
		mav.addObject("users", users);
		return mav;
	}

	@RequestMapping("/add_travel")
	public ModelAndView add_travel() {
		ModelAndView mav = new ModelAndView("test/add_travel");
		List<User> users = userService.getUserList(0, 100);
		mav.addObject("users", users);
		return mav;
	}

	@RequestMapping("/test_cookie")
	public ModelAndView test_cookie(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mav = new ModelAndView("jsonView");
		CookieManager.getInstance().saveCookie(response, "test", "test2", -1,
				"/", "pubic.renren.com");
		return mav;
	}

	@RequestMapping("/crud/{className}")
	@ResponseBody
	public Object crud(@PathVariable("className") String className) {
		String ret = "";
		String obj = className.substring(0, 1).toLowerCase()
				+ className.substring(1);
		String daoObj = obj + "Dao";
		ret += String.format("public void %s%s(%s %s) {%s.%sObject(%s);}",
				"add", className, className, obj, daoObj, "save", obj);
		ret += "\n";
		ret += String.format("public void %s%s(%s %s) {%s.%sObject(%s);}",
				"update", className, className, obj, daoObj, "update", obj);
		ret += "\n";
		ret += String.format("public void %s%s(%s %s) {%s.%sObject(%s);}",
				"delete", className, className, obj, daoObj, "delete", obj);
		ret += "\n";
		ret += String.format(
				"public %s %s%s(%s %s) {return %s.%sObject(%s.class,%s);}",
				className, "get", className, "int", "id", daoObj, "get",
				className, "id");
		ret += "\n";
		return ret;
	}

	@RequestMapping("/good_setdefaultphoto")
	public ModelAndView good_setdefaultphoto() {
		ModelAndView mav = new ModelAndView("jsonView");
		List<Long> ids = new ArrayList<Long>();
		for (long i = 1; i <= 10; i++) {
			ids.add(i);
		}
		List<Good> goods = goodService.getGoodList(ids);
		for (Good good : goods) {
			List<GoodPhoto> photos = goodService.getGoodPhotoListByGoodId(
					good.getId(), 0, 1);
			good.setDefaultPhoto(photos.get(0).getPhoto_url());
			good.setDefaultMainUrl(photos.get(0).getMain_url());
			good.setDefaultHeadUrl(photos.get(0).getHead_url());
			good.setDefaultTinyUrl(photos.get(0).getTiny_url());
			goodService.updateGood(good);
		}
		return mav;
	}

	@RequestMapping("/test_after_add_task")
	public ModelAndView test_after_add_task(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mav = new ModelAndView("jsonView");
		List<Task> tasks = taskService.getTaskList();
		for (Task task : tasks) {
			if (task.getDelete_flag() == 0) {
				taskService.afterAddTask(task);
			}
		}
		return mav;
	}

	@RequestMapping("/test_log")
	public ModelAndView test_log(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mav = new ModelAndView("jsonView");
		logger.info("test");
		logger_search.info("search|123456789|test");
		return mav;
	}

	@RequestMapping("/test_updatecountryflagcache")
	public ModelAndView test_updatecountryflagcache(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mav = new ModelAndView("jsonView");
		countryService.updateCountryNameFlagCache();
		return mav;
	}

	@RequestMapping("/test_province")
	public ModelAndView test_province(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mav = new ModelAndView("jsonView");
		// Province province = new Province();
		// province.setName("ss");
		// countryService.addProvince(province);
		// province.setName("ssd");
		// countryService.updateProvince(province);
		// System.out.println(countryService.getProvince(province.getId()));
		countryService.deleteProvince(countryService.getProvince(7));
		return mav;
	}

	@MobileLoginRequired
	@RequestMapping("/test_interce")
	public ModelAndView test_interce(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("jsonView");
		return mav;
	}

	@RequestMapping("/test_resource")
	public ModelAndView test_resource(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("jsonView");

		Resource resource = new ClassPathResource("ext_stopword.dic");
		// Resource resource = new FileSystemResource(
		// "D:/workspace/shopping-agent/pom.xml");
		System.out.println(resource.getFilename());
		InputStream in;
		try {
			in = resource.getInputStream();
			byte[] b = new byte[10];
			int len;
			while ((len = in.read(b)) > 0) {
				System.out.println(new String(b, 0, len));
			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return mav;
	}
}
