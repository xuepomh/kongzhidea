package com.lyq.util;

import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
/**
 * �ֶ��������ݱ�
 * @author Li Yong Qiang
 */
public class ExportTables {
	/**
	 * main ����
	 * @param args
	 */
	public static void main(String[] args) {
		//����������Ϣ
		Configuration cfg = new Configuration().configure();
		//ʵ����SchemaExport����
		SchemaExport export = new SchemaExport(cfg);
		//�������ݱ�
		export.create(true, true);
	}
}
