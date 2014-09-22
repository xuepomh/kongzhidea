package com.lyq.util;

import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
/**
 * 手动导出数据表
 * @author Li Yong Qiang
 */
public class ExportTables {
	/**
	 * main 方法
	 * @param args
	 */
	public static void main(String[] args) {
		//加载配置信息
		Configuration cfg = new Configuration().configure();
		//实例化SchemaExport对象
		SchemaExport export = new SchemaExport(cfg);
		//导出数据表
		export.create(true, true);
	}
}
