package com.renren.shopping.model;

import java.io.Serializable;

public class CollectionPK implements Serializable {
	private int userId;
	private long photoId;

	public CollectionPK(int userId, long photoId) {
		this.userId = userId;
		this.photoId = photoId;
	}

	public CollectionPK() {
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public long getPhotoId() {
		return photoId;
	}

	public void setPhotoId(long photoId) {
		this.photoId = photoId;
	}

	@Override
	public String toString() {
		return "CollectionPK [userId=" + userId + ", photoId=" + photoId + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (photoId ^ (photoId >>> 32));
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
		CollectionPK other = (CollectionPK) obj;
		if (photoId != other.photoId)
			return false;
		if (userId != other.userId)
			return false;
		return true;
	}

}
