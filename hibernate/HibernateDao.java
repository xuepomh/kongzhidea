package com.renren.shopping.base;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class HibernateDao<T> {

	@Autowired
	protected HibernateTemplate hib;

	@Autowired
	protected SessionFactory sessionFactory;

	public String[] getStringArray(String... s) {
		return s;
	}

	public Object[] getObjectArray(Object... objs) {
		return objs;
	}

	/**
	 * 保存实体对象
	 * 
	 * @param obj
	 */
	public void saveObject(T obj) {
		hib.save(obj);
	}

	/**
	 * 得到实体对象 区分int和long
	 * 
	 * @param <T>
	 * 
	 * @return
	 */
	public T getObject(Class<T> className, int id) {
		return hib.get(className, id);
	}

	/**
	 * 得到实体对象
	 * 
	 * @param <T>
	 * 
	 * @return
	 */
	public T getObject(Class<T> className, long id) {
		return hib.get(className, id);
	}

	/**
	 * 得到实体对象
	 * 
	 * @param <T>
	 * 
	 * @return
	 */
	public T getObject(Class<T> className, Serializable id) {
		return hib.get(className, id);
	}

	/**
	 * 修改实体对象，如果不存在则抛出异常，
	 * 
	 * 可以使用游离状态的Object 注意对int类型和null
	 * 
	 * @param obj
	 */
	public void updateObject(T obj) {
		if (obj == null) {
			return;
		}
		hib.update(obj);
	}

	/**
	 * 删除一个实体,根据主键来删 不存在则抛出异常
	 * 
	 * @param user
	 */
	public void deleteObject(T obj) {
		if (obj == null) {
			return;
		}
		hib.delete(obj);
	}

	/**
	 * HQL查询 hibernate使用in查询 不适用占位符 直接hql拼接
	 * 
	 * @return
	 */
	public List<T> find(String hql) {
		return hib.find(hql);
	}

	/**
	 * HQL查询
	 * 
	 * @return
	 */
	public List<T> find(String hql, Object... objs) {
		return hib.find(hql, objs);
	}

	/**
	 * HQL查询 byName
	 * 
	 * @return
	 */
	public List<T> findByNamedParam(String hql, String paramName, Object param) {
		return hib.findByNamedParam(hql, paramName, param);
	}

	/**
	 * HQL查询 byNames
	 * 
	 * @return
	 */
	public List<T> findByNamedParam(String hql, String[] paramNames,
			Object[] params) {
		return hib.findByNamedParam(hql, paramNames, params);
	}

	/**
	 * 使用HQL查询,在hmb.xml文件中配置query标签
	 * 
	 * @return
	 */
	public List<T> findByNamedQuery(String nameQuery) {
		return hib.findByNamedQuery(nameQuery);
	}

	/**
	 * 使用HQL查询,在hmb.xml文件中配置query标签
	 * 
	 * @return
	 */
	public List<T> findByNamedQuery(String nameQuery, Object... objs) {
		return hib.findByNamedQuery(nameQuery, objs);
	}

	/**
	 * 使用HQL查询,在hmb.xml文件中配置query标签
	 * 
	 * @return
	 */
	public List<T> findByNamedQueryAndNamedParam(String nameQuery,
			String paramName, Object param) {
		return hib.findByNamedQueryAndNamedParam(nameQuery, paramName, param);
	}

	/**
	 * 使用HQL查询,在hmb.xml文件中配置query标签
	 * 
	 * @return
	 */
	public List<T> findByNamedQueryAndNamedParam(String nameQuery,
			String[] paramNames, Object[] params) {
		return hib.findByNamedQueryAndNamedParam(nameQuery, paramNames, params);
	}

	/**
	 * hibernate 翻页查询 limit :start,:limit
	 * 
	 * @param hql
	 * @param start
	 * @param limit
	 * @param objs
	 * @return
	 */
	public List<T> getListForPage(final String hql, final int start,
			final int limit, final Object... objs) {
		List list = hib.executeFind(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery(hql);
				for (int i = 0; i < objs.length; i++) {
					query.setParameter(i, objs[i]);
				}
				query.setFirstResult(start);
				query.setMaxResults(limit);
				List<T> list = query.list();
				return list;
			}

		});
		return list;
	}

	/**
	 * hibernate 翻页查询 limit:limit
	 * 
	 * @param hql
	 * @param limit
	 * @param objs
	 * @return
	 */
	public List<T> getListForPage2(final String hql, final int limit,
			final Object... objs) {
		List list = hib.executeFind(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery(hql);
				for (int i = 0; i < objs.length; i++) {
					query.setParameter(i, objs[i]);
				}
				query.setMaxResults(limit);
				List<T> list = query.list();
				return list;
			}

		});
		return list;
	}
}
