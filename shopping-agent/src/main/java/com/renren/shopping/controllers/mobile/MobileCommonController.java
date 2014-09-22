package com.renren.shopping.controllers.mobile;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.renren.shopping.conf.CityConf;
import com.renren.shopping.conf.CountryConf;
import com.renren.shopping.conf.ResultConf;
import com.renren.shopping.model.City;
import com.renren.shopping.model.Continent;
import com.renren.shopping.model.Country;
import com.renren.shopping.model.Province;
import com.renren.shopping.services.CountryService;

/**
 * 通用的一些服务
 * 
 * @author kk
 * 
 */
@Controller
@RequestMapping("/mobile/common")
public class MobileCommonController {

	@Autowired
	CountryService countryService;

	/**
	 * 得到筛选国家的国家列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getCountryList", method = { RequestMethod.POST })
	@ResponseBody
	public Object getCountryList() {
		JSONArray jar = new JSONArray();

		// 热门国家列表
		JSONObject hot = new JSONObject();
		hot.put(CountryConf.CONTINENT_NAME, "热门国家");
		List<Country> hotCountries = countryService.getHotCountryList();
		countryService.supplyMiniJsonObject(hot, hotCountries);

		jar.add(hot);

		// 大洲国家列表
		List<Continent> continents = countryService.getContinentList();
		for (Continent continent : continents) {
			JSONObject obj = new JSONObject();
			obj.put(CountryConf.CONTINENT_NAME, continent.getName());
			List<Country> countries = new ArrayList<Country>(
					continent.getCountries());
			countryService.supplyMiniJsonObject(obj, countries);
			jar.add(obj);
		}

		return ResultConf.getSuccessResult2(jar);
	}

	/**
	 * 得到筛选城市的城市列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getCityList", method = { RequestMethod.POST })
	@ResponseBody
	public Object getCityList() {
		JSONArray jar = new JSONArray();

		// 热门国家列表
		JSONObject hot = new JSONObject();
		hot.put(CityConf.PROVINCE_NAME, "热门城市");
		List<City> hotCities = countryService.getHotCityList();
		countryService.supplyMiniCityJsonObject(hot, hotCities);

		jar.add(hot);

		// 省 城市 列表
		List<Province> provinces = countryService.getProvinceList();
		for (Province province : provinces) {
			JSONObject obj = new JSONObject();
			obj.put(CityConf.PROVINCE_NAME, province.getName());
			List<City> cities = new ArrayList<City>(province.getCities());
			countryService.supplyMiniCityJsonObject(obj, cities);
			jar.add(obj);
		}

		return ResultConf.getSuccessResult2(jar);
	}
}
