package com.renren.shopping.util;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.json.JSONArray;

public class ResourceUtil {
	private static final Log logger = LogFactory.getLog(ResourceUtil.class);

	/**
	 * 得到城市地区信息 精确到市级别,对直辖市特殊处理下
	 * 
	 * @return
	 */
	public static JSONArray getCityInfos() {
		try {
			String path = ResourceUtil.class.getResource("/cityinfo.txt").getPath();
			List<String> list = FileUtils.readLines(new File(path), "utf-8");
			return JSONArray.fromObject(list.get(0));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}
}
