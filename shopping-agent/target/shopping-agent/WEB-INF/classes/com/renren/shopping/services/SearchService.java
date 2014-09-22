package com.renren.shopping.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.renren.shopping.conf.KVConf;
import com.renren.shopping.dao.SearchHotWordDao;
import com.renren.shopping.dao.TaskIndexDao;
import com.renren.shopping.model.SearchHotWord;
import com.renren.shopping.model.Task;
import com.renren.shopping.model.TaskIndex;
import com.renren.shopping.util.IKAnalyseUtil;
import com.renren.shopping.util.Pinyin4J;
import com.renren.shopping.util.RedisAPI;
import com.renren.shopping.util.RedisUtil;

@Service
public class SearchService {

	@Autowired
	TaskIndexDao taskIndexDao;

	@Autowired
	SearchHotWordDao searchHotWordDao;

	@Autowired
	TaskService taskService;

	public static final Log logger = LogFactory.getLog(SearchService.class);

	@PostConstruct
	private void init() {

	}

	/************* 任务index表操作 **************/
	public void insertTaskIndex(long taskId, String title, String description) {
		try {
			taskIndexDao.insert(taskId, splitWordToPinyin(title),
					splitWordToPinyin(description));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public void deleteTaskIndex(long taskId) {
		taskIndexDao.delete(taskId);
	}

	/**
	 * 根据匹配度得到任务列表
	 * 
	 * @param query
	 * @param endId
	 * @param limit
	 * @return
	 */
	public List<Task> getMatchList(String query, long endId, int limit) {
		// 转换拼音
		String query2 = splitWordToPinyin(query);
		List<TaskIndex> list = taskIndexDao.getMatchList(query2, endId, limit);
		List<Long> ids = new ArrayList<Long>();
		for (TaskIndex taskIndex : list) {
			ids.add(taskIndex.getTaskId());
		}
		return taskService.getTaskListDesc(ids);
	}

	/****************** 热搜词推荐表查询 ***************/
	public void addSearchHotWord(SearchHotWord searchHotWord) {
		searchHotWordDao.saveObject(searchHotWord);
	}

	public void updateSearchHotWord(SearchHotWord searchHotWord) {
		searchHotWordDao.updateObject(searchHotWord);
	}

	public void deleteSearchHotWord(SearchHotWord searchHotWord) {
		searchHotWordDao.deleteObject(searchHotWord);
	}

	public SearchHotWord getSearchHotWord(long id) {
		return searchHotWordDao.getObject(SearchHotWord.class, id);
	}

	/**
	 * 得到前缀匹配的词列表
	 * 
	 * @param word
	 * @param limit
	 * @return
	 */
	public List<SearchHotWord> getSearchHotWordList(int start, int limit) {
		String hql = "from SearchHotWord ";
		return searchHotWordDao.getListForPage(hql, start, limit);
	}

	/**
	 * 得到前缀匹配的词列表，按照搜索数目排序
	 * 
	 * @param word
	 * @param limit
	 * @return
	 */
	public List<SearchHotWord> getSearchHotWordListByWord(String word, int limit) {
		String hql = "from SearchHotWord where word like ? order by searchNum desc";
		return searchHotWordDao.getListForPage2(hql, limit, word + "%");
	}

	/**
	 * 得到推荐词
	 * 
	 * @param word
	 *            如果为空则取热门搜索词
	 * @param limit
	 * @return
	 */
	public List<String> getSuggestion(String word, int limit) {
		if (StringUtils.isBlank(word)) {
			return RedisAPI.lrange(KVConf.HOT_SEARCH_WORD_KEY, 0,
					limit);
		}
		List<SearchHotWord> list = getSearchHotWordListByWord(word, limit);
		List<String> ret = new ArrayList<String>();
		for (SearchHotWord hot : list) {
			ret.add(hot.getWord());
		}
		return ret;
	}

	/******************************************/

	/**
	 * 将content分词，转成拼音
	 * 
	 * @param content
	 * @return
	 */
	public String splitWordToPinyin(String content) {
		if (StringUtils.isBlank(content)) {
			return "";
		}
		List<String> contents = IKAnalyseUtil.match(content);
		StringBuilder ret = new StringBuilder();
		for (String con : contents) {
			try {
				if (con.length() <= 1) {
					continue;
				}
				Set<String> pinyin = Pinyin4J.getPinyinSet(con);
				for (String s : pinyin) {
					ret.append(s + " ");
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		return ret.toString().trim();
	}

}
