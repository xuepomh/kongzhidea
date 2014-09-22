package com.baobaotao.utils;

public class ResourceUtils {
	/**
	 * 得到相对该class的文件路径
	 * 
	 * @param cls
	 * @param name
	 * @return
	 */
	public static String getResourceFullPath(Class cls, String name) {
		String path = cls.getResource("").getPath();
		path = path.substring(1);
		StringBuffer fileName = new StringBuffer(path);
		fileName.append(name);
		return fileName.toString();
	}

}
