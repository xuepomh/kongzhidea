package com.renren.shopping.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 联合主键必须实现Serializable接口,在get时候也使用Serializable对象
 * 
 * 关注表
 * 
 * @author kk
 * 
 */
public class UserFollows {
	private UserFollowsPK userFollowsPK;
	private Date update_time;

	public UserFollowsPK getUserFollowsPK() {
		return userFollowsPK;
	}

	public void setUserFollowsPK(UserFollowsPK userFollowsPK) {
		this.userFollowsPK = userFollowsPK;
	}

	public Date getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}

	@Override
	public String toString() {
		return "UserFollows [userFollowsPK=" + userFollowsPK + ", update_time="
				+ update_time + "]";
	}

}
