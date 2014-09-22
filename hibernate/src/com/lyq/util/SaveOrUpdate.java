package com.lyq.util;

import org.hibernate.Session;

import com.lyq.model.Medicine;

/**
 * 临时对象则save，游离对象则udate,update时候如果不存在则抛出异常
 * 
 * @author Li Yong Qiang
 */
public class SaveOrUpdate {
	public static void main(String[] args) {
		Session session = null; // 声明Session对象
		try {
			// 获取Session
			session = HibernateUtil.getSession();
			// 开启事物
			session.beginTransaction();
			// 实例化药品对象，并对其属性赋值
			Medicine medicine = new Medicine();

			// id默认是null的时候表示是临时对象，执行save操作;需要改ID的unsaved-value值为0才表示ID是0的时候是临时对象
			// medicine.setId(0);
			medicine.setName("XX");
			medicine.setPrice(6.00);
			medicine.setFactoryAdd("kk");
			medicine.setDescription("new");
			// ID不存在则保存，存在则update 根据主键来判断
			session.saveOrUpdate(medicine);
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
