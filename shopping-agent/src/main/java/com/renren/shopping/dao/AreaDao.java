package com.renren.shopping.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.renren.shopping.base.JDBCDao;
import com.renren.shopping.model.Area;
import com.renren.shopping.util.CommonUtil;

@Repository
public class AreaDao extends JDBCDao {

	private static final String TABLENAME = " china_area ";
	private static final String FIELD = " code, name ";
	private static final Log logger = LogFactory.getLog(AreaDao.class);

	class AreaRowMapper implements RowMapper<Area> {
		public Area mapRow(ResultSet rs, int index) throws SQLException {
			return new Area(rs.getString("code"), rs.getString("name"));
		}
	}

	public void insert(String code, String name) {
		String sql = "insert into china_area(code, name) values(?,?)";
		Object[] params = new Object[] { code, name };
		// int[] types = new int[] { Types.INTEGER, Types.VARCHAR, Types.CHAR,
		// Types.VARCHAR };
		int[] types = new int[] { Types.VARCHAR, Types.VARCHAR };
		jdbc.update(sql, params, types);
	}

	public Area getArea(String code) {
		List<Area> list = new ArrayList<Area>();
		try {
			String sql = String.format(
					"select %s from china_area where code=?", FIELD);
			Object[] params = new Object[] { code };
			int[] types = new int[] { Types.VARCHAR };
			list = jdbc.query(sql, params, types,
					new RowMapperResultSetExtractor<Area>(new AreaRowMapper()));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		if (list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}

	public List<Area> getAreaList(String code) {
		List<Area> list = null;
		try {
			String sql = String.format(
					"select %s from china_area where code like ?", FIELD);
			Object[] params = new Object[] { code };
			int[] types = new int[] { Types.VARCHAR };
			list = jdbc.query(sql, params, types,
					new RowMapperResultSetExtractor<Area>(new AreaRowMapper()));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return list;
	}
}
