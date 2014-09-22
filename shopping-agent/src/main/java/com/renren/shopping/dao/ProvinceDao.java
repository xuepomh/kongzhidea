package com.renren.shopping.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.renren.shopping.base.HibernateDao;
import com.renren.shopping.model.Province;

@Repository
public class ProvinceDao extends HibernateDao<Province> {
	public static final String ProvinceLIST = "com.renren.shopping.model.Province.getProvinceList";
	public static final String ProvinceIDLIST = "com.renren.shopping.model.Province.getProvinceIDList";

	/**
	 * 得到所有的大洲
	 * 
	 * @return
	 */
	public List<Province> getProvinceList() {
		return hib.findByNamedQuery(ProvinceLIST);
	}

	/**
	 * 得到所有的大洲ID
	 * 
	 * @return
	 */
	public List<Integer> getProvinceIDList() {
		return hib.findByNamedQuery(ProvinceIDLIST);
	}
}
