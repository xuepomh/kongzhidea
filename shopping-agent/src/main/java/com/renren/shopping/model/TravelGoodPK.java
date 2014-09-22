package com.renren.shopping.model;

import java.io.Serializable;

public class TravelGoodPK implements Serializable {
	private long travelId;
	private long goodId;

	public TravelGoodPK() {
	}

	public TravelGoodPK(long travelId, long goodId) {
		this.travelId = travelId;
		this.goodId = goodId;
	}

	public long getTravelId() {
		return travelId;
	}

	public void setTravelId(long travelId) {
		this.travelId = travelId;
	}

	public long getGoodId() {
		return goodId;
	}

	public void setGoodId(long goodId) {
		this.goodId = goodId;
	}

	@Override
	public String toString() {
		return "TravelGoodPK [travelId=" + travelId + ", goodId=" + goodId
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (goodId ^ (goodId >>> 32));
		result = prime * result + (int) (travelId ^ (travelId >>> 32));
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
		TravelGoodPK other = (TravelGoodPK) obj;
		if (goodId != other.goodId)
			return false;
		if (travelId != other.travelId)
			return false;
		return true;
	}

}
