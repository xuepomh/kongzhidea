package com.renren.shopping.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.renren.shopping.conf.CityConf;
import com.renren.shopping.conf.CountryConf;
import com.renren.shopping.dao.CityDao;
import com.renren.shopping.dao.ContinentDao;
import com.renren.shopping.dao.CountryDao;
import com.renren.shopping.dao.ProvinceDao;
import com.renren.shopping.model.City;
import com.renren.shopping.model.Continent;
import com.renren.shopping.model.Country;
import com.renren.shopping.model.Province;
import com.renren.shopping.util.CommonUtil;

@Service
public class CountryService {

	@Autowired
	ContinentDao continentDao;

	@Autowired
	CountryDao countryDao;

	@Autowired
	ProvinceDao provinceDao;

	@Autowired
	CityDao cityDao;

	/** 国家和国旗 映射 flag,flag_thumb */
	private Map<String, Map<String, String>> countryNameFlagMap = new HashMap<String, Map<String, String>>();

	/********************** 大洲 *******************/
	public void addContinent(Continent continent) {
		continentDao.saveObject(continent);
	}

	public void updateContinent(Continent continent) {
		continentDao.updateObject(continent);
	}

	public void deleteContinent(Continent continent) {
		continentDao.deleteObject(continent);
	}

	public Continent getContinent(int id) {
		return continentDao.getObject(Continent.class, id);
	}

	/**
	 * 得到所有的大洲 按照weight排序
	 * 
	 * @return
	 */
	public List<Continent> getContinentList() {
		return continentDao.getContinentList();
	}

	/**
	 * 得到所有的大洲ID
	 * 
	 * @return
	 */
	public List<Integer> getContinentIDList() {
		return continentDao.getContinentIDList();
	}

	/************************ 国家 ******************************/

	public void addCountry(Country country) {
		countryDao.saveObject(country);
	}

	public void updateCountry(Country country) {
		countryDao.updateObject(country);
	}

	public void deleteCountry(Country country) {
		countryDao.deleteObject(country);
	}

	public Country getCountry(int id) {
		return countryDao.getObject(Country.class, id);
	}

	/**
	 * 根据名字得到国家,只取id和name字段,其他为空
	 * 
	 * @param name
	 * @return
	 */
	public Country getMiniCountryByName(String name) {
		List<Country> list = countryDao
				.find("select new Country(id,name,flag,flag_thumb) from Country where name =?",
						name);
		if (list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}

	/**
	 * 只取id和name和flag字段,其他为空
	 * 
	 * @return
	 */
	public Country getMiniCountryById(int id) {
		List<Country> list = countryDao
				.find("select new Country(id,name,flag,flag_thumb) from Country where id=?",
						id);
		if (list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}

	/**
	 * 根据国家名字得到国旗 有缓存
	 * 
	 * @param countryName
	 * @return
	 */
	public Map<String, String> getCountryFlag(String countryName) {
		if (StringUtils.isBlank(countryName)) {
			return null;
		}
		// cache
		if (countryNameFlagMap.containsKey(countryName)) {
			return countryNameFlagMap.get(countryName);
		}
		Country country = getMiniCountryByName(countryName);
		if (country == null) {
			return null;
		}
		Map<String, String> mat = getTmpFlagCache(country);
		countryNameFlagMap.put(countryName, mat);
		return mat;
	}

	private Map<String, String> getTmpFlagCache(Country country) {
		Map<String, String> mat = new HashMap<String, String>();
		if (!StringUtils.isBlank(country.getFlag())) {
			mat.put(CountryConf.COUNTRY_FLAG, country.getFlag());
		}
		if (!StringUtils.isBlank(country.getFlag_thumb())) {
			mat.put(CountryConf.COUNTRY_FLAG_THUMB, country.getFlag_thumb());
		}
		return mat;
	}

	/**
	 * 更新通过countryName得到国旗的缓存
	 * 
	 * @param countryName
	 */
	public void updateCountryNameFlagCache(String countryName) {
		if (StringUtils.isBlank(countryName)) {
			return;
		}
		Country country = getMiniCountryByName(countryName);
		if (country == null || StringUtils.isBlank(country.getFlag())) {
			countryNameFlagMap.remove(countryName);
			return;
		}
		countryNameFlagMap.put(countryName, getTmpFlagCache(country));
	}

	/**
	 * 更新通过countryName得到国旗的缓存,更新所有的国家缓存
	 */
	public void updateCountryNameFlagCache() {
		List<Continent> list = getContinentList();
		for (Continent continent : list) {
			for (Country country : continent.getCountries()) {
				updateCountryNameFlagCache(country.getName());
			}
		}
	}

	/**
	 * 得到热门国家 按照Country.hot排序
	 * 
	 * @return
	 */
	public List<Country> getHotCountryList() {
		String hql = "from Country where hot > 0 order by hot desc";
		return countryDao.find(hql);
	}

	/**
	 * 将国家信息添加到jsonObject中
	 * 
	 * @param obj
	 * @param countrys
	 */
	public void supplyJsonObject(JSONObject obj, List<Country> countries) {
		if (countries == null || countries.size() == 0) {
			return;
		}
		JSONArray jar = new JSONArray();
		for (Country country : countries) {
			JSONObject t = new JSONObject();
			t.put(CountryConf.COUNTRY_ID, country.getId());
			t.put(CountryConf.COUNTRY_NAME, country.getName());
			t.put(CountryConf.COUNTRY_WEIGHT, country.getWeight());
			t.put(CountryConf.COUNTRY_HOT, country.getHot());
			t.put(CountryConf.COUNTRY_FLAG, country.getFlag());
			t.put(CountryConf.COUNTRY_FLAG_THUMB, country.getFlag_thumb());

			jar.add(t);
		}

		obj.put(CountryConf.COUNTRYKEY, jar);
	}

	/**
	 * 将国家 基本信息添加到jsonObject中
	 * 
	 * @param obj
	 * @param countrys
	 */
	public void supplyMiniJsonObject(JSONObject obj, List<Country> countries) {
		if (countries == null || countries.size() == 0) {
			return;
		}
		JSONArray jar = new JSONArray();
		for (Country country : countries) {
			JSONObject t = new JSONObject();
			t.put(CountryConf.COUNTRY_ID, country.getId());
			t.put(CountryConf.COUNTRY_NAME, country.getName());

			jar.add(t);
		}

		obj.put(CountryConf.COUNTRYKEY, jar);
	}

	/*************** 省 ******************/
	public void addProvince(Province province) {
		provinceDao.saveObject(province);
	}

	public void updateProvince(Province province) {
		provinceDao.updateObject(province);
	}

	public void deleteProvince(Province province) {
		provinceDao.deleteObject(province);
	}

	public Province getProvince(int id) {
		return provinceDao.getObject(Province.class, id);
	}

	/**
	 * 得到所有的省 按照weight排序
	 * 
	 * @return
	 */
	public List<Province> getProvinceList() {
		return provinceDao.getProvinceList();
	}

	/**
	 * 得到所有的省ID
	 * 
	 * @return
	 */
	public List<Integer> getProvinceIDList() {
		return provinceDao.getProvinceIDList();
	}

	/**************** 城市 ****************/
	public void addCity(City city) {
		cityDao.saveObject(city);
	}

	public void updateCity(City city) {
		cityDao.updateObject(city);
	}

	public void deleteCity(City city) {
		cityDao.deleteObject(city);
	}

	public City getCity(int id) {
		return cityDao.getObject(City.class, id);
	}

	/**
	 * 得到热门城市 按照City.hot排序
	 * 
	 * @return
	 */
	public List<City> getHotCityList() {
		String hql = "from City where hot > 0 order by hot desc";
		return cityDao.find(hql);
	}

	/**
	 * 将城市 基本信息添加到jsonObject中
	 * 
	 * @param obj
	 * @param countrys
	 */
	public void supplyMiniCityJsonObject(JSONObject obj, List<City> cities) {
		if (cities == null || cities.size() == 0) {
			return;
		}
		JSONArray jar = new JSONArray();
		for (City city : cities) {
			JSONObject t = new JSONObject();
			t.put(CityConf.CITY_ID, city.getId());
			t.put(CityConf.CITY_NAME, city.getName());

			jar.add(t);
		}

		obj.put(CityConf.CITYKEY, jar);
	}
}
