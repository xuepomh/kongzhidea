package com.ebay.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ebay.dao.UserDao;
import com.ebay.dao.jdbc.JUserDao;
import com.ebay.model.User;

@Service
public class UserService {
	public static final Log logger = LogFactory.getLog(UserService.class);

	// jdbc
	@Autowired
	JUserDao juserDao;

	// mybatis
	@Autowired
	UserDao userDao;

	// jdbc
	public User getJUser(int id) {
		return juserDao.getUser(id);
	}

	public void saveJUser(String name) {
		juserDao.insert(name);
	}

	public List<User> getJUserList(String name) {
		return juserDao.getUserList(name);
	}

	// mybatis
	public User getUser(int id) {
		return userDao.getUser(id);
	}

	public List<User> getUserList(String name) {
		return userDao.getUserList(name);
	}

	public void saveUser(User user) {
		userDao.addUser(user);
	}

	public void updateUser(User user) {
		userDao.updateUser(user);
	}

	public void deleteUser(int id) {
		userDao.deleteUser(id);
	}

	public List<User> getUserLimit(Map param) {
		return userDao.getUserLimit(param);
	}

}
