package com.renren.shopping.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.renren.shopping.base.HibernateDao;
import com.renren.shopping.model.Continent;

@Repository
public class ContinentDao extends HibernateDao<Continent> {
	public static final String CONTINENTLIST = "com.renren.shopping.model.Continent.getContinentList";
	public static final String CONTINENTIDLIST = "com.renren.shopping.model.Continent.getContinentIDList";

	/**
	 * 得到所有的大洲
	 * 
	 * @return
	 */
	public List<Continent> getContinentList() {
		return hib.findByNamedQuery(CONTINENTLIST);
	}

	/**
	 * 得到所有的大洲ID
	 * 
	 * @return
	 */
	public List<Integer> getContinentIDList() {
		return hib.findByNamedQuery(CONTINENTIDLIST);
	}
}
