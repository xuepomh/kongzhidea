package com.kk.test.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.kk.test.model.TestId;

@Repository
public class TestIdDAO {
	public static final Log logger = LogFactory.getLog(TestIdDAO.class);

	class TestIdRowMapper implements RowMapper<TestId> {
		public TestId mapRow(ResultSet rs, int index) throws SQLException {
			TestId u = new TestId(rs.getInt("id"));
			return u;
		}
	}

	@Autowired
	private JdbcTemplate jdbc;

	/**
	 * 查找所有的TestId
	 * 
	 * @return
	 */
	public List<TestId> getTestIdList() {
		List<TestId> list = null;
		try {
			String sql = "select id from test";
			list = jdbc.query(sql, new RowMapperResultSetExtractor<TestId>(
					new TestIdRowMapper()));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return list;
	}
}
