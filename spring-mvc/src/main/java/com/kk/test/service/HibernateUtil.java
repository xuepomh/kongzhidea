package com.kk.test.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;

import com.kk.test.model.User;

/**
 * 一般crud使用hibernate,而查询使用jdbcTemplate或iBatis
 * 
 * @author kk
 * 
 */
@Component
public class HibernateUtil {
	public static final Log logger = LogFactory.getLog(HibernateUtil.class);
	@Autowired
	private HibernateTemplate hib;
	@Autowired
	private SessionFactory sessionFactory;

	/**
	 * 保存实体对象,add完成后自动给ID赋值
	 * 
	 * @param user
	 */
	public void addUser(User user) {
		hib.save(user);
	}

	/**
	 * 得到实体对象
	 * 
	 * @return
	 */
	public User getUser(int id) {
		return hib.get(User.class, id);
	}

	/**
	 * 修改实体对象
	 * 
	 * User u = hibUtil.getUser(10); u.setName("test2"); hibUtil.updateUser(u);
	 * 
	 * @param user
	 */
	public void updateUser(User user) {
		hib.update(user);
	}

	/**
	 * 删除一个实体,根据主键来删  如果不存在则抛异常
	 * 
	 * @param user
	 */
	public void deleteUser(User user) {
		if (user == null) {
			return;
		}
		hib.delete(user);
	}

	/**
	 * 使用HQL查询
	 * 
	 * @param name
	 * @return
	 */
	public List<User> findUsersByName(String name) {
		return hib.find("from User where name like ?", name + "%");
	}

	/**
	 * 使用HQL查询,在hmb.xml文件中配置query标签
	 * 
	 * @param name
	 * @return
	 */
	public List<User> findUsersByName2(String name) {
		return hib.findByNamedQuery("com.kk.test.model.User.likeName", name
				+ "%");
	}

	/**
	 * 使用HQL查询,在hmb.xml文件中配置query标签
	 * 
	 * @param name
	 * @return
	 */
	public List<User> findUsersByName3(String name) {
		return hib.findByNamedQueryAndNamedParam(
				"com.kk.test.model.User.likeName2", "name", name + "%");
	}

	/** ............................原生态hibernate........................ */
	/**
	 * sessionFactory中必须配置以下项: <prop
	 * key="hibernate.current_session_context_class">thread</prop> <prop
	 * key="hibernate.transaction.factory_class">org.hibernate.transaction.
	 * JDBCTransactionFactory </prop>
	 * 
	 * 必须开启事务
	 * 
	 * @param user
	 */
	public void addUser2(User user) {
		/** 在web工程中 session不要close */
		Session session = sessionFactory.getCurrentSession();
		try {
			session.beginTransaction();
			session.save(user);
			session.getTransaction().commit();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			session.getTransaction().rollback();
		}
	}

	/**
	 * get 取不到则返回null
	 * 
	 * @param id
	 * @return
	 */
	public User getUser_get(int id) {
		/** 在web工程中 session不要close */
		Session session = sessionFactory.getCurrentSession();
		// select 时候不能再配置文件中加入 自动建表 ,否则删除数据
		try {
			session.beginTransaction();
			User user = (User) session.get(User.class, new Integer(id));
			session.getTransaction().commit();
			return user;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			session.getTransaction().rollback();
		}
		return null;
	}

	/**
	 * load 认为对象一定存在，否则抛出异常
	 * 
	 * @param id
	 * @return
	 */
	public User getUser_load(int id) {
		/** 在web工程中 session不要close */
		Session session = sessionFactory.getCurrentSession();
		// 调用load时候，不会立即发送sql语句进行查询 只有在引用对象的时候才会发送sql语句
		try {
			session.beginTransaction();
			User user = (User) session.load(User.class, new Integer(id));
			// logger.info(user.getId()); //getId 不执行sql
			logger.info("hib load:  " + user.getName());// 如果不执行user.getName则抛出异常
			session.getTransaction().commit();
			return user;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			session.getTransaction().rollback();
		}
		return null;
	}

	/**
	 * select时候自动更新
	 * 
	 * @param id
	 * @param name
	 */
	public void update_auto(int id, String name) {
		/** 在web工程中 session不要close */
		Session session = sessionFactory.getCurrentSession();
		try {
			session.beginTransaction();
			User user = (User) session.get(User.class, new Integer(id));
			user.setName(name);
			session.getTransaction().commit();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			session.getTransaction().rollback();
		}
	}

	/**
	 * update方法
	 * 
	 * User u2 = hibUtil.getUser_get(11); u2.setName("kong2");
	 * hibUtil.update_manual(u2);
	 * 
	 * @param user
	 */
	public void update_manual(User user) {
		/** 在web工程中 session不要close */
		Session session = sessionFactory.getCurrentSession();
		try {
			session.beginTransaction();
			session.update(user);
			session.getTransaction().commit();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			session.getTransaction().rollback();
		}
	}

	/**
	 * delete 根据id来删除
	 * 
	 */
	public void deleteUser2(User user) {
		/** 在web工程中 session不要close */
		Session session = sessionFactory.getCurrentSession();
		try {
			session.beginTransaction();
			session.delete(user);
			session.getTransaction().commit();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			session.getTransaction().rollback();
		}
	}

	/**
	 * hql语句
	 */
	public void testHQL() {
		/** 在web工程中 session不要close */
		Session session = sessionFactory.getCurrentSession();
		try {
			session.beginTransaction();

			// from 实体类
			// select
			String hql = "from  User";
			Query query = session.createQuery(hql);
			List<User> list = query.list();
			logger.info("hql1:  " + list);

			// 条件查询
			hql = "from  User  where id=?";
			// from User as u where u.id=? //这样写也行
			query = session.createQuery(hql);
			query = query.setParameter(0, 1);
			list = query.list();
			logger.info("hql2:  " + list);

			// 动态赋值 自定义参数
			hql = "from  User  where id=:id";
			query = session.createQuery(hql);
			query = query.setParameter("id", 1);
			list = query.list();
			logger.info("hql3:  " + list);

			// 排序
			hql = "from  User  order by id";
			query = session.createQuery(hql);
			list = query.list();
			logger.info("hql4:  " + list);

			// 分组操作 多列时候使用Object[]
			hql = "select count(*) from  User  ";
			query = session.createQuery(hql);
			List<Long> li = query.list();
			for (Long obj : li) {
				logger.info("hql4 count:" + obj);
			}

			// update
			hql = "update User  set name='kkklo' where id=15";
			query = session.createQuery(hql);
			logger.info("hql6:  " + query.executeUpdate());

			session.getTransaction().commit();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			session.getTransaction().rollback();
		}
	}
}
