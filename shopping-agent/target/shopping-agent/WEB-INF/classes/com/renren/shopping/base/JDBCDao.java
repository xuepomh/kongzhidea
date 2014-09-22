package com.renren.shopping.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JDBCDao {

	@Autowired
	protected JdbcTemplate jdbc;
}
