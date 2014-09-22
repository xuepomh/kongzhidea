package com.renren.shopping.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.renren.shopping.base.HibernateDao;
import com.renren.shopping.model.CollectionPhoto;

@Repository
public class CollectionPhotoDao extends HibernateDao<CollectionPhoto> {
	private Log logger = LogFactory.getLog(CollectionPhotoDao.class);

	/**
	 * 得到收藏该照片的人数
	 * 
	 * @param goodId
	 * @return
	 */
	public long getCollectionNumByPhotoId(long photoId) {
		List<Long> list = new ArrayList<Long>();
		/** 在web工程中 session不要close */
		Session session = sessionFactory.getCurrentSession();
		try {
			session.beginTransaction();

			// 动态赋值 自定义参数 返回结果变成对象需要使用new，否则返回类型为Object[]
			String hql = "select count(*) from CollectionPhoto where collectionPK.photoId = ?";
			Query query = session.createQuery(hql);
			query = query.setParameter(0, photoId);
			list = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			session.getTransaction().rollback();
		}
		if (list.size() == 0) {
			return 0;
		}
		return list.get(0);
	}
}
