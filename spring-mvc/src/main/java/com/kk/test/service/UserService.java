package com.kk.test.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.stereotype.Service;

import com.kk.test.dao.TestIdDAO;
import com.kk.test.model.TestId;
import com.mysql.jdbc.Driver;

@Service
public class UserService {
	public static final Log logger = LogFactory.getLog(UserService.class);
	@Autowired
	TestIdDAO testDao;

	public List<TestId> getTestIdList() {
		return testDao.getTestIdList();
	}

}