package com.renren.shopping.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.renren.shopping.base.HibernateDao;
import com.renren.shopping.model.Comment;

@Repository
public class CommentDao extends HibernateDao<Comment> {

	private static final Log logger = LogFactory.getLog(CommentDao.class);

	/**
	 * 得到某行程的评论列表 支持limit查询
	 * 
	 * @param travelId
	 * @param start
	 * @param limit
	 * @return
	 */
	public List<Comment> getCommentListDesc(long travelId, int start, int limit) {
		List<Comment> list = new ArrayList<Comment>();
		/** 在web工程中 session不要close */
		Session session = sessionFactory.getCurrentSession();
		try {
			session.beginTransaction();

			// 动态赋值 自定义参数 返回结果变成对象需要使用new，否则返回类型为Object[]
			String hql = " from  Comment  where travelId = ? order by id desc";
			Query query = session.createQuery(hql);
			query = query.setParameter(0, travelId);// 参数从0开始计算
			query.setFirstResult(start);
			query.setMaxResults(limit);// limit :start,:limit
			list = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			session.getTransaction().rollback();
		}
		return list;
	}

	/**
	 * 根据score得到某行程的评论列表 支持limit查询
	 * @param travelId
	 * @param type 
	 * @param start
	 * @param limit
	 * @return
	 */
	public List<Comment> getCommentListByTypeDesc(long travelId, int score,
			int start, int limit) {
		List<Comment> list = new ArrayList<Comment>();
		/** 在web工程中 session不要close */
		Session session = sessionFactory.getCurrentSession();
		try {
			session.beginTransaction();

			// 动态赋值 自定义参数 返回结果变成对象需要使用new，否则返回类型为Object[]
			String hql = " from  Comment  where travelId = ? and score = ? order by id desc";
			Query query = session.createQuery(hql);
			query = query.setParameter(0, travelId);// 参数从0开始计算
			query = query.setParameter(1, score);
			query.setFirstResult(start);
			query.setMaxResults(limit);// limit :start,:limit
			list = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			session.getTransaction().rollback();
		}
		return list;
	}
}
