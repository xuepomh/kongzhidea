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

import com.renren.shopping.model.Continent;
import com.renren.shopping.model.Country;
import com.renren.shopping.services.CountryService;

/**
 * 后台管理国家信息
 * 
 * @author kk
 * 
 */
@Controller
@RequestMapping("console/country")
public class CountryConsoleController {

	@Autowired
	CountryService countryService;

	public Log logger = LogFactory.getLog(CountryConsoleController.class);

	/**
	 * 展示国家
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("show")
	public ModelAndView show(HttpServletRequest request,
			HttpServletResponse response) {
		List<Integer> clist = countryService.getContinentIDList();
		if (clist.isEmpty()) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
		return show(request, response, clist.get(0));
	}

	/**
	 * 展示国家
	 * 
	 * @param request
	 * @param response
	 * @param cid
	 * @return
	 */
	@RequestMapping("show/{cid}")
	public ModelAndView show(HttpServletRequest request,
			HttpServletResponse response, @PathVariable("cid") int cid) {
		ModelAndView mav = new ModelAndView("console/country/show");
		// 得到列表
		List<Continent> clist = countryService.getContinentList();
		mav.addObject("clist", clist);
		// 得到洲的详细信息
		Continent continent = countryService.getContinent(cid);
		if (continent == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
		mav.addObject("continent", continent);
		return mav;
	}

	/**
	 * 展示热门国家
	 * 
	 * @param request
	 * @param response
	 * @param cid
	 * @return
	 */
	@RequestMapping("hot")
	public ModelAndView hot(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mav = new ModelAndView("console/country/hot");
		// 得到列表
		List<Continent> clist = countryService.getContinentList();
		mav.addObject("clist", clist);
		// 得到洲的详细信息
		List<Country> countries = countryService.getHotCountryList();
		mav.addObject("countries", countries);
		return mav;
	}

	/**
	 * 添加国家
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
		ModelAndView mav = new ModelAndView("console/country/add");
		List<Continent> clist = countryService.getContinentList();
		mav.addObject("clist", clist);
		mav.addObject("source", source);
		return mav;
	}

	/**
	 * 添加国家
	 * 
	 * @param country
	 */
	@RequestMapping(value = "addCountry", method = RequestMethod.POST)
	public ModelAndView addCountry(
			@RequestParam("name") String name,
			@RequestParam(value = "flag", required = false) String flag,
			@RequestParam(value = "flag_thumb", required = false) String flag_thumb,
			@RequestParam("continent") int continent,
			@RequestParam(value = "hot", required = false) Integer hot,
			@RequestParam(value = "weight", required = false) Integer weight) {
		Country country = new Country();
		country.setName(name);
		country.setFlag(flag);
		country.setFlag_thumb(flag_thumb);
		country.setContinent(countryService.getContinent(continent));
		if (hot != null) {
			country.setHot(hot);
		}
		if (weight != null) {
			country.setWeight(weight);
		}
		countryService.addCountry(country);
		ModelAndView mav = new ModelAndView(new RedirectView(
				"/console/country/show/" + continent + "?source="
						+ country.getId()));
		return mav;

	}

	/**
	 * 添加国家
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("edit/{id}")
	public ModelAndView edit(HttpServletRequest request,
			HttpServletResponse response, @PathVariable("id") int id) {
		ModelAndView mav = new ModelAndView("console/country/edit");
		List<Continent> clist = countryService.getContinentList();
		mav.addObject("clist", clist);
		Country country = countryService.getCountry(id);
		mav.addObject("country", country);
		return mav;
	}

	/**
	 * 添加国家
	 * 
	 * @param country
	 */
	@RequestMapping(value = "editCountry", method = RequestMethod.POST)
	public ModelAndView editCountry(
			@RequestParam("id") int id,
			@RequestParam("name") String name,
			@RequestParam(value = "flag", required = false) String flag,
			@RequestParam(value = "flag_thumb", required = false) String flag_thumb,
			@RequestParam("continent") int continent,
			@RequestParam(value = "hot", required = false) Integer hot,
			@RequestParam(value = "weight", required = false) Integer weight) {
		Country country = countryService.getCountry(id);
		country.setName(name);
		country.setFlag(flag);
		country.setFlag_thumb(flag_thumb);
		country.setContinent(countryService.getContinent(continent));
		if (hot != null) {
			country.setHot(hot);
		}
		if (weight != null) {
			country.setWeight(weight);
		}
		countryService.updateCountry(country);
		ModelAndView mav = new ModelAndView(new RedirectView(
				"/console/country/show/" + continent + "?source=" + id));
		return mav;

	}

	/**
	 * 删除国家
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "deleteCountry", method = RequestMethod.POST)
	@ResponseBody
	public Object delete(HttpServletRequest request,
			HttpServletResponse response, @RequestParam("id") int id) {
		Country country = countryService.getCountry(id);
		if (country == null) {
			return "null";
		}
		countryService.deleteCountry(country);
		return "success";
	}
}
