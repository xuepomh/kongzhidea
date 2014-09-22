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
import com.renren.shopping.model.TaskIndex;

@Repository
public class TaskIndexDao extends JDBCDao {
	private static final String TABLENAME = " task_index ";
	private static final Log logger = LogFactory.getLog(TaskIndexDao.class);

	class TaskIndexRowMapper implements RowMapper<TaskIndex> {
		public TaskIndex mapRow(ResultSet rs, int index) throws SQLException {
			return new TaskIndex(rs.getLong(1), rs.getLong(2), rs.getString(3),
					rs.getString(4), rs.getDate(5), rs.getDouble(6));
		}
	}

	public void insert(long taskId, String title, String description) {
		String sql = "insert into task_index(task_id,title,description) values(?,?,?)";
		Object[] params = new Object[] { taskId, title, description };
		jdbc.update(sql, params);
	}

	/**
	 * 
	 * @param content
	 *            仅包含拼音，不能包含其他字符
	 * @param endId
	 * @param limit
	 * @return
	 */
	public List<TaskIndex> getMatchList(String content, long endId, int limit) {
		List<TaskIndex> list = new ArrayList<TaskIndex>();
		try {
			String sql = String
					.format("select id,task_id,title,description,update_time,match(title,description) against(\"%s\") as score from task_index where match(title,description) against(\"%s\") and task_id < ? limit ?",
							content, content);
			Object[] params = new Object[] { endId, limit };
			list = jdbc.query(sql, params,
					new RowMapperResultSetExtractor<TaskIndex>(
							new TaskIndexRowMapper()));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return list;
	}

	public void delete(long taskId) {
		String sql = "delete from task_index where task_id=" + taskId;
		jdbc.execute(sql);
	}
}
