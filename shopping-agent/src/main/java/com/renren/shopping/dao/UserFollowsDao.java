package com.renren.shopping.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.renren.shopping.base.HibernateDao;
import com.renren.shopping.model.Task;
import com.renren.shopping.model.UserFollows;

@Repository
public class UserFollowsDao extends HibernateDao<UserFollows> {
	private static final Log logger = LogFactory.getLog(UserFollowsDao.class);
	
	/**
	 * 查关注者列表
	 */
	public List getFollowedIds(int fansId) {
		/** 在web工程中 session不要close */
		Session session = sessionFactory.getCurrentSession();
		List followedIds = null;
		try {
			session.beginTransaction();

			Query query = session.getNamedQuery("com.renren.shopping.model.UserFollows.getFollowedIds");
			query.setInteger(0, fansId);
			followedIds = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			session.getTransaction().rollback();
		}
		return followedIds;
	}
}
