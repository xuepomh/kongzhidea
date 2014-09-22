package com.renren.shopping.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.renren.shopping.base.HibernateDao;
import com.renren.shopping.model.AddressUser;

@Repository
public class AddressUserDao extends HibernateDao<AddressUser> {
	public Log logger = LogFactory.getLog(AddressUserDao.class);

	/**
	 * 将user的所有地址全部设置成非默认地址
	 * 
	 * @param userId
	 */
	public void setAddressDefaultNull(int userId) {
		/** 在web工程中 session不要close */
		Session session = sessionFactory.getCurrentSession();
		try {
			session.beginTransaction();

			// 动态赋值 自定义参数 返回结果变成对象需要使用new，否则返回类型为Object[]
			String hql = " update AddressUser set isDefault = 0 where userId = ?";
			Query query = session.createQuery(hql);
			query = query.setParameter(0, userId);// 参数从0开始计算
			query.executeUpdate();
			session.getTransaction().commit();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			session.getTransaction().rollback();
		}
	}
}
