package com.renren.shopping.model;

import java.util.Date;

/**
 * 每个行程对应多个商品
 * 
 * @author kk
 * 
 */
public class TravelGood {
	private TravelGoodPK travelGoodPK;
	private Date update_time;

	public TravelGoodPK getTravelGoodPK() {
		return travelGoodPK;
	}

	public void setTravelGoodPK(TravelGoodPK travelGoodPK) {
		this.travelGoodPK = travelGoodPK;
	}

	public Date getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}

	@Override
	public String toString() {
		return "TravelGood [travelGoodPK=" + travelGoodPK + ", update_time="
				+ update_time + "]";
	}

}
