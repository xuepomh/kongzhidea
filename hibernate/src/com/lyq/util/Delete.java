package com.lyq.util;

import org.hibernate.Session;

import com.lyq.model.Medicine;

/**
 * 删除数据 可以删除游离对象(只需要设置ID)和持久化对象 不存在则抛出异常
 * 
 * @author Li Yong Qiang
 */
public class Delete {
	public static void main(String[] args) {
		Session session = null; // 声明Session对象
		try {
			// 获取Session
			session = HibernateUtil.getSession();
			// 开启事物
			session.beginTransaction();
			// 加载对象
			Medicine medicine = (Medicine) session.load(Medicine.class,
					new Integer(7));
			// 输出药品信息
			session.delete(medicine); // 默认删除后medicine实例没有修改，Medicine [id=7,
										// name=XXx,除非配置use_identifier_rollback属性为true
			System.out.println(medicine);
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
