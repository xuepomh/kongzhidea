package com.renren.shopping.model;

import java.util.Date;

/**
 * 评论
 * 
 * @author kk
 * 
 */
public class Comment {
	private long id;
	private int userId;
	private long travelId;
	private String content;
	private int score;// 评论打分:0-10
	private long toId;// 回复评论的ID
	private Date update_time;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public long getTravelId() {
		return travelId;
	}

	public void setTravelId(long travelId) {
		this.travelId = travelId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public long getToId() {
		return toId;
	}

	public void setToId(long toId) {
		this.toId = toId;
	}

	public Date getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}

	@Override
	public String toString() {
		return "Comment [id=" + id + ", userId=" + userId + ", travelId="
				+ travelId + ", content=" + content + ", score=" + score
				+ ", toId=" + toId + ", update_time=" + update_time + "]";
	}

}
