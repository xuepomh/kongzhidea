package com.lyq.annotion;

import org.hibernate.Session;

import com.lyq.model.User;

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
			
			User user = new User();
			user.setName("kongzhidea");
			session.save(user);
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
