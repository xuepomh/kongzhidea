package com.ebay.dao;

import java.util.List;
import java.util.Map;

import com.ebay.model.User;

@MyBatisRepository
public interface UserDao {

	public User getUser(int id);

	public List<User> getUserList(String name);

	public void addUser(User user);

	public void updateUser(User user);

	public void deleteUser(int id);

	public List<User> getUserLimit(Map param);
}
