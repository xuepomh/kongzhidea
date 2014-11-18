package com.ebay.dao.jdbc;

import java.awt.geom.Area;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.ebay.model.User;

@Repository
public class JUserDao {
	public static final Log logger = LogFactory.getLog(JUserDao.class);
	@Autowired
	protected JdbcTemplate jdbc;

	class UserRowMapper implements RowMapper<User> {
		public User mapRow(ResultSet rs, int index) throws SQLException {
			User user = new User(rs.getInt("id"), rs.getString("name"),
					rs.getInt("birth_year"));
			return user;
		}
	}

	public void insert(String name) {
		String sql = "insert into e_user(name) values(?)";
		Object[] params = new Object[] { name };
		int[] types = new int[] { Types.VARCHAR };
		jdbc.update(sql, params, types);
	}

	public User getUser(int id) {
		String sql = "select id,name,birth_year from e_user where id= ?";
		return jdbc.queryForObject(sql, new UserRowMapper(), id);
	}

	public List<User> getUserList(String name) {
		List<User> list = new ArrayList<User>();
		try {
			String sql = "select id,name,birth_year from e_user where name like ?";
			Object[] params = new Object[] { name };
			int[] types = new int[] { Types.VARCHAR };
			list = jdbc.query(sql, params, types, new UserRowMapper());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return list;
	}
}
