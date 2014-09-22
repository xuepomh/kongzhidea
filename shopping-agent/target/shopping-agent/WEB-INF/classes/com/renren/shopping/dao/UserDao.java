package com.renren.shopping.dao;

import org.springframework.stereotype.Repository;

import com.renren.shopping.base.HibernateDao;
import com.renren.shopping.model.User;

@Repository
public class UserDao extends HibernateDao<User> {
}
