package com.renren.shopping.services;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.renren.shopping.dao.DiscoveryDao;
import com.renren.shopping.model.Discovery;

@Service
public class DiscoveryService {
	
	@Autowired
	DiscoveryDao discoveryDao;
	
	private Log logger = LogFactory.getLog(DiscoveryService.class);
	
	public Discovery getDiscovery(int id){
		return discoveryDao.getObject(Discovery.class, id);
	}
	
	public void addDiscovery(Discovery discovery){
		discoveryDao.saveObject(discovery);
	}
	
	public void updateDiscovery(Discovery discovery){
		discoveryDao.updateObject(discovery);
	}
	
	public void deleteDiscovery(Discovery discovery){
		discoveryDao.deleteObject(discovery);
	}
}
