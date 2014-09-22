package com.renren.shopping.controllers.console;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.renren.shopping.model.Province;
import com.renren.shopping.model.City;
import com.renren.shopping.services.CountryService;

/**
 * 后台管理城市信息
 * 
 * @author kk
 * 
 */
@Controller
@RequestMapping("/console/province")
public class ProvinceConsoleController {

	@Autowired
	CountryService countryService;

	public Log logger = LogFactory.getLog(ProvinceConsoleController.class);

	/**
	 * 展示城市
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("show")
	public ModelAndView show(HttpServletRequest request,
			HttpServletResponse response) {
		List<Integer> clist = countryService.getProvinceIDList();
		if (clist.isEmpty()) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
		return show(request, response, clist.get(0));
	}

	/**
	 * 展示城市
	 * 
	 * @param request
	 * @param response
	 * @param cid
	 * @return
	 */
	@RequestMapping("show/{cid}")
	public ModelAndView show(HttpServletRequest request,
			HttpServletResponse response, @PathVariable("cid") int cid) {
		ModelAndView mav = new ModelAndView("console/province/show");
		// 得到列表
		List<Province> clist = countryService.getProvinceList();
		mav.addObject("clist", clist);
		// 得到洲的详细信息
		Province province = countryService.getProvince(cid);
		if (province == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
		mav.addObject("province", province);
		return mav;
	}

	/**
	 * 展示城市
	 * 
	 * @param request
	 * @param response
	 * @param cid
	 * @return
	 */
	@RequestMapping("hot")
	public ModelAndView hot(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mav = new ModelAndView("console/province/hot");
		// 得到列表
		List<Province> clist = countryService.getProvinceList();
		mav.addObject("clist", clist);
		// 得到洲的详细信息
		List<City> cities = countryService.getHotCityList();
		mav.addObject("cities", cities);
		return mav;
	}

	/**
	 * 添加城市
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("add")
	public ModelAndView add(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "source", required = false) Integer source) {
		if (source == null) {
			source = 0;
		}
		ModelAndView mav = new ModelAndView("console/province/add");
		List<Province> clist = countryService.getProvinceList();
		mav.addObject("clist", clist);
		mav.addObject("source", source);
		return mav;
	}

	/**
	 * 添加城市
	 * 
	 * @param city
	 */
	@RequestMapping(value = "addCity", method = RequestMethod.POST)
	public ModelAndView addCity(@RequestParam("name") String name,
			@RequestParam("province") int provinceId,
			@RequestParam(value = "hot", required = false) Integer hot,
			@RequestParam(value = "weight", required = false) Integer weight) {
		City city = new City();
		city.setName(name);
		city.setProvinceId(provinceId);
		if (hot != null) {
			city.setHot(hot);
		}
		if (weight != null) {
			city.setWeight(weight);
		}
		countryService.addCity(city);
		ModelAndView mav = new ModelAndView(new RedirectView(
				"/console/province/show/" + provinceId + "?source="
						+ city.getId()));
		return mav;

	}

	/**
	 * 添加城市
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("edit/{id}")
	public ModelAndView edit(HttpServletRequest request,
			HttpServletResponse response, @PathVariable("id") int id) {
		ModelAndView mav = new ModelAndView("console/province/edit");
		List<Province> clist = countryService.getProvinceList();
		mav.addObject("clist", clist);
		City city = countryService.getCity(id);
		mav.addObject("city", city);
		return mav;
	}

	/**
	 * 添加城市
	 * 
	 * @param city
	 */
	@RequestMapping(value = "editCity", method = RequestMethod.POST)
	public ModelAndView editCity(@RequestParam("id") int id,
			@RequestParam("name") String name,
			@RequestParam("province") int provinceId,
			@RequestParam(value = "hot", required = false) Integer hot,
			@RequestParam(value = "weight", required = false) Integer weight) {
		City city = countryService.getCity(id);
		city.setName(name);
		city.setProvinceId(provinceId);
		if (hot != null) {
			city.setHot(hot);
		}
		if (weight != null) {
			city.setWeight(weight);
		}
		countryService.updateCity(city);
		ModelAndView mav = new ModelAndView(new RedirectView(
				"/console/province/show/" + provinceId + "?source=" + id));
		return mav;

	}

	/**
	 * 删除城市
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "deleteCity", method = RequestMethod.POST)
	@ResponseBody
	public Object delete(HttpServletRequest request,
			HttpServletResponse response, @RequestParam("id") int id) {
		City city = countryService.getCity(id);
		if (city == null) {
			return "null";
		}
		countryService.deleteCity(city);
		return "success";
	}
}
