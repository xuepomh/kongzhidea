package com.lyq.util;

import org.hibernate.Session;

import com.lyq.model.Medicine;

/**
 * merge 临时对象直接save，游离对象如果存在则update(当游离对象和缓存中不一致时候才update)，不存在则save,
 * 
 * 将对象复制一份，返回对象的引用,与缓存中的对象是同一个实例
 * 
 * @author Li Yong Qiang
 */
public class Merge {
	public static void main(String[] args) {
		Session session = null; // 声明Session对象
		try {
			// 获取Session
			session = HibernateUtil.getSession();
			// 开启事物
			session.beginTransaction();
			// 实例化药品对象，并对其属性赋值
			Medicine medicine = new Medicine();

			medicine.setName("XXx");
			medicine.setPrice(7.00);
			medicine.setFactoryAdd("kk");
			medicine.setDescription("new");
			Medicine me = (Medicine) session.merge(medicine);
			System.out.println(medicine.getId() + ".." + me.getId());//null..9
			// 提交事物
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			// 出错将回滚事物
			session.getTransaction().rollback();
		} finally {
			// 关闭Session对象
			HibernateUtil.closeSession(session);
		}
	}
}
