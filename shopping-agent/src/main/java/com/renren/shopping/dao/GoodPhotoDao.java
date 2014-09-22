package com.renren.shopping.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.renren.shopping.base.HibernateDao;
import com.renren.shopping.model.GoodPhoto;

@Repository
public class GoodPhotoDao extends HibernateDao<GoodPhoto> {
	private static final Log logger = LogFactory.getLog(GoodDao.class);

	/**
	 * 得到商品照片列表
	 */
	public List<GoodPhoto> getGoodPhotoListByGoodId(long goodId, int start, int limit) {
		List<GoodPhoto> list = new ArrayList<GoodPhoto>();
		/** 在web工程中 session不要close */
		Session session = sessionFactory.getCurrentSession();
		try {
			session.beginTransaction();

			// 动态赋值 自定义参数 返回结果变成对象需要使用new，否则返回类型为Object[]
			String hql = " from  GoodPhoto  where goodId =? ";
			Query query = session.createQuery(hql);
			query = query.setParameter(0, goodId);
			query.setFirstResult(start);
			query.setMaxResults(limit);
			list = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			session.getTransaction().rollback();
		}
		return list;
	}
}
