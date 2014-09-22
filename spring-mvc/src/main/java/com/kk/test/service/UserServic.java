package com.kk.test.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.kk.test.model.UserTest;

@Service
public class UserServic {
	public static final Log logger = LogFactory.getLog(UserServic.class);

	public void createUser(UserTest user) {
		logger.info(user);
	}
}
