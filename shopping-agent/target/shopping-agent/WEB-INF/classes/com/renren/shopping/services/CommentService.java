package com.renren.shopping.services;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.renren.shopping.conf.CommentConf;
import com.renren.shopping.conf.TravelConf;
import com.renren.shopping.conf.UserConf;
import com.renren.shopping.dao.CommentDao;
import com.renren.shopping.model.Comment;
import com.renren.shopping.model.User;

@Service
public class CommentService {

	@Autowired
	CommentDao commentDao;

	private static final Log logger = LogFactory.getLog(CommentService.class);
	/**********************************/
	@Autowired
	UserService userService;

	public void addComment(Comment comment) {
		commentDao.saveObject(comment);
	}

	public void updateComment(Comment comment) {
		commentDao.updateObject(comment);
	}

	public void deleteComment(Comment comment) {
		commentDao.deleteObject(comment);
	}

	public Comment getComment(long id) {
		return commentDao.getObject(Comment.class, id);
	}

	/**
	 * 得到某行程的所有评论列表
	 * 
	 * @param travelId
	 * @return
	 */
	public List<Comment> getCommentList(long travelId) {
		String hql = "from Comment where travelId = ?";
		return commentDao.find(hql, travelId);
	}

	/**
	 * 得到某行程的评论列表 支持limit查询
	 * 
	 * @param travelId
	 * @param start
	 * @param limit
	 * @return
	 */
	public List<Comment> getCommentListDesc(long travelId, int start, int limit) {
		return commentDao.getCommentListDesc(travelId, start, limit);
	}

	/**
	 * 得到某行程的评论列表 支持limit查询
	 * 
	 * @param travelId
	 * @param start
	 * @param limit
	 * @return
	 */
	public List<Comment> getCommentListByTypeDesc(long travelId, int score,
			int start, int limit) {
		return commentDao.getCommentListByTypeDesc(travelId, score, start,
				limit);
	}

	/**********************************/

	/**
	 * 将评论信息添加到JsonObject中
	 * 
	 * @param obj
	 * @param currentUserId
	 *            当前登陆用户，0表示未登录
	 * @param comment
	 */
	public void supplyJsonObject(JSONObject obj, int currentUserId,
			List<Comment> comments) {
		JSONArray jar = new JSONArray();
		if (comments == null || comments.isEmpty()) {
			return;
		}
		try {
			for (Comment comment : comments) {
				JSONObject jcomment = new JSONObject();
				if (comment == null) {
					continue;
				}
				// 评论信息
				supplyCommentDetail(jcomment, comment);

				// 用户信息
				User user = userService.getUser(comment.getUserId());
				userService.supplyMiniJsonObject(jcomment, currentUserId, user);

				jar.add(jcomment);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			jar = new JSONArray();
		}
		obj.put(CommentConf.COMMENTKEY, jar);
		obj.put(CommentConf.COMMENT_NUMBER, jar.size());
	}

	/**
	 * 评论详细信息
	 * 
	 * @param jcomment
	 * @param comment
	 */
	private void supplyCommentDetail(JSONObject jcomment, Comment comment) {
		jcomment.put(CommentConf.COMMENT_ID, comment.getId());
		jcomment.put(CommentConf.COMMENT_CONTENT, comment.getContent());
		jcomment.put(CommentConf.COMMENT_SCORE, comment.getScore());
		supplyCommentReply(jcomment, comment); // 评论回复信息
		jcomment.put(CommentConf.COMMENT_TIME, comment.getUpdate_time()
				.getTime());
		jcomment.put(UserConf.USER_ID, comment.getUserId());
		jcomment.put(TravelConf.TRAVEL_ID, comment.getTravelId());
	}

	/**
	 * 评论回复信息
	 * 
	 * @param jcomment
	 * @param comment
	 */
	private void supplyCommentReply(JSONObject jcomment, Comment comment) {
		if (comment.getToId() > 0) {
			try {
				Comment toComment = getComment(comment.getToId());
				if (toComment != null) {
					User toUser = userService.getUser(toComment.getUserId());
					jcomment.put(CommentConf.COMMENT_TO_USERNAME,
							toUser.getName());
					jcomment.put(CommentConf.COMMENT_TO_USERID, toUser.getId());
					jcomment.put(CommentConf.COMMENT_TOID, comment.getToId());
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		} else {
			jcomment.put(CommentConf.COMMENT_TOID, 0);
		}
	}
}
