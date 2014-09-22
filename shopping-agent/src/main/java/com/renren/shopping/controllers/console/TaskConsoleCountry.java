package com.renren.shopping.controllers.console;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.renren.shopping.conf.KVConf;
import com.renren.shopping.conf.ResultConf;
import com.renren.shopping.model.SearchHotWord;
import com.renren.shopping.services.SearchService;
import com.renren.shopping.services.TaskService;
import com.renren.shopping.util.RedisAPI;
import com.renren.shopping.util.RedisUtil;

/**
 * 任务信息
 * 
 * @author kk
 * 
 */
@Controller
@RequestMapping("/console/task")
public class TaskConsoleCountry {

	@Autowired
	TaskService taskService;

	@Autowired
	SearchService searchService;

	private static Logger logger = Logger.getLogger(TaskConsoleCountry.class);

	/**
	 * 展示热门设置页,当搜索为词为空的时候展示
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("showSearchHotWordDefault")
	public ModelAndView showSearchHotWordDefault(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mav = new ModelAndView("console/task/search-hot-default");
		// List<String> hots = RedisAPI.lrange(
		// KVConf.HOT_SEARCH_WORD_KEY, 0, 100);
		List<String> hots = RedisAPI.lrange(KVConf.HOT_SEARCH_WORD_KEY, 0, 100);
		mav.addObject("hots", hots);
		return mav;
	}

	/**
	 * 设置热门搜索词，当搜索为词为空的时候展示
	 * 
	 * @param request
	 * @param response
	 * @param words
	 * @return
	 */
	@RequestMapping(value = "setSearchHotWordDefault", method = RequestMethod.POST)
	@ResponseBody
	public Object setSearchHotWordDefault(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam("words[]") String[] words) {
		long len = RedisAPI.llen(KVConf.HOT_SEARCH_WORD_KEY);
		for (int i = 0; i < len; i++) {
			RedisAPI.lpop(KVConf.HOT_SEARCH_WORD_KEY);
		}
		for (int i = 0; i < words.length; i++) {
			RedisAPI.rpush(KVConf.HOT_SEARCH_WORD_KEY, words[i]);
		}
		return ResultConf.SUCCESSRET;
	}

	/**
	 * 展示所有热词
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("showSearchHotWord")
	public ModelAndView showSearchHotWord(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "p", required = false) Integer p,
			@RequestParam(value = "limit", required = false) Integer limit) {
		if (p == null || p == 0) {
			p = 1;
		}
		if (limit == null || limit == 0) {
			limit = 20;
		}
		ModelAndView mav = new ModelAndView("console/task/search-hot");
		List<SearchHotWord> list = searchService.getSearchHotWordList((p - 1)
				* limit, limit);
		mav.addObject("list", list);
		mav.addObject("p", p);
		mav.addObject("limit", limit);
		return mav;
	}

	/**
	 * 删除搜索热词
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "deleteSearchHotWord", method = RequestMethod.POST)
	@ResponseBody
	public Object deleteSearchHotWord(@RequestParam("id") long id) {
		SearchHotWord hot = searchService.getSearchHotWord(id);
		if (hot == null) {
			return ResultConf.SOURCE_NOTEXIST_EXCEPTION;
		}
		searchService.deleteSearchHotWord(hot);
		return ResultConf.SUCCESSRET;
	}

	/**
	 * 修改热词 搜索数
	 * 
	 * @param id
	 * @param num
	 * @return
	 */
	@RequestMapping(value = "setSearchHotWordSearchNum", method = RequestMethod.POST)
	@ResponseBody
	public Object setSearchHotWordSearchNum(@RequestParam("id") long id,
			@RequestParam("num") int num) {
		SearchHotWord hot = searchService.getSearchHotWord(id);
		if (hot == null || num < 0) {
			return ResultConf.SOURCE_NOTEXIST_EXCEPTION;
		}
		hot.setSearchNum(num);
		searchService.updateSearchHotWord(hot);
		return ResultConf.SUCCESSRET;
	}

	/**
	 * 添加搜索热词
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "addSearchHotWord", method = RequestMethod.POST)
	public ModelAndView addSearchHotWord(@RequestParam("word") String word,
			@RequestParam("search_num") int searchNum,
			@RequestParam("p") int p, @RequestParam("limit") int limit) {
		SearchHotWord hot = new SearchHotWord(word, searchNum);
		searchService.addSearchHotWord(hot);
		return new ModelAndView(new RedirectView(String.format(
				"/console/task/showSearchHotWord?p=%d&limit=%d", p, limit)));
	}

	/**
	 * 搜索
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "search", method = RequestMethod.POST)
	public ModelAndView search(@RequestParam("word") String word) {
		int limit = 10000;
		ModelAndView mav = new ModelAndView("console/task/search-word");
		List<SearchHotWord> list = searchService.getSearchHotWordListByWord(
				word, limit);
		mav.addObject("list", list);
		return mav;
	}
}
