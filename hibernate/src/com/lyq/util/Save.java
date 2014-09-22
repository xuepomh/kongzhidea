package com.lyq.util;

import org.hibernate.Session;

import com.lyq.model.Medicine;

/**
 * 向数据库中添加数据
 * 
 * @author Li Yong Qiang
 */
public class Save {
	public static void main(String[] args) {
		Session session = null; // 声明Session对象
		try {
			// 获取Session
			session = HibernateUtil.getSession();
			// 开启事物
			session.beginTransaction();
			// 实例化药品对象，并对其属性赋值
			Medicine medicine = new Medicine();
			// save时候 即使设置ID也是无效的 insert into
			// tb_medicine_save (name, price, factoryAdd, description)
			medicine.setName("XX");
			medicine.setPrice(5.00);
			medicine.setFactoryAdd("kk");
			medicine.setDescription("new");
			// 保存药品对象   save后不允许setID,否则抛出异常
			session.save(medicine);
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
