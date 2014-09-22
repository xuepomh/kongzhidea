package com.renren.shopping.model;

import java.util.Date;

/**
 * 收藏商品
 * 
 * @author kk
 * 
 */
public class CollectionPhoto {
	private CollectionPK collectionPK;
	private long goodId;
	private long travelId;
	private Date update_time;

	public CollectionPK getCollectionPK() {
		return collectionPK;
	}

	public void setCollectionPK(CollectionPK collectionPK) {
		this.collectionPK = collectionPK;
	}

	public long getGoodId() {
		return goodId;
	}

	public void setGoodId(long goodId) {
		this.goodId = goodId;
	}

	public long getTravelId() {
		return travelId;
	}

	public void setTravelId(long travelId) {
		this.travelId = travelId;
	}

	public Date getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}

	@Override
	public String toString() {
		return "CollectionPhoto [collectionPK=" + collectionPK + ", goodId="
				+ goodId + ", travelId=" + travelId + ", update_time="
				+ update_time + "]";
	}

}
