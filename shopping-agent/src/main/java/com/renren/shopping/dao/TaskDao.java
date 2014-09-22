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

@Repository
public class TaskDao extends HibernateDao<Task> {

	private static final Log logger = LogFactory.getLog(TaskDao.class);

	/**
	 * 通过in查询 得到商品列表
	 */
	public List<Task> getTaskListDesc(List<Long> ids) {
		List<Task> list = new ArrayList<Task>();
		if (ids == null || ids.size() == 0) {
			return list;
		}
		/** 在web工程中 session不要close */
		Session session = sessionFactory.getCurrentSession();
		try {
			session.beginTransaction();

			// 动态赋值 自定义参数 返回结果变成对象需要使用new，否则返回类型为Object[]
			String hql = " from  Task  where id in (:id) order by id desc";
			Query query = session.createQuery(hql);
			query = query.setParameterList("id", ids);
			list = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			session.getTransaction().rollback();
		}
		return list;
	}
}
