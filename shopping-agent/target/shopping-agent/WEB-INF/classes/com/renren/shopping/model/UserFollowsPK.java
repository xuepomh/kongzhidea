package com.renren.shopping.model;

import java.io.Serializable;

public class UserFollowsPK implements Serializable {
	private int fanId;// 粉丝ID
	private int userId;// 被关注者ID

	public UserFollowsPK(int fanId, int userId) {
		super();
		this.fanId = fanId;
		this.userId = userId;
	}

	public UserFollowsPK() {
	}

	public int getFanId() {
		return fanId;
	}

	public void setFanId(int fanId) {
		this.fanId = fanId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "UserFollowsPK [fanId=" + fanId + ", userId=" + userId + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + fanId;
		result = prime * result + userId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserFollowsPK other = (UserFollowsPK) obj;
		if (fanId != other.fanId)
			return false;
		if (userId != other.userId)
			return false;
		return true;
	}

}
