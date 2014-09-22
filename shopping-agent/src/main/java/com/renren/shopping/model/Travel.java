package com.renren.shopping.model;

import java.util.Date;

import com.renren.shopping.conf.TravelConf;

/**
 * 买手发布行程
 * 
 * @author kk
 * 
 */
public class Travel {
	private long id;
	private int userId;// 买手id
	private String toCountry;// 去往国家
	private int resident;// 是否常驻 默认0，不常住;1常驻
	private String departureCity;// 出发城市
	private long back_time;// 回国时间 unix时间戳
	private long start_time;// 出发时间 unix时间戳
	private String description;// 帮带说明
	private int addreddOtherId;// 发货地址 @delete
	private int delete_flag;// 是否删除标记,0:未删除;1被删除
	private Date update_time;
	private int status;//0:未开始 , 1:进行中 ,2:已过期

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

	public String getToCountry() {
		return toCountry;
	}

	public void setToCountry(String toCountry) {
		this.toCountry = toCountry;
	}

	public int getResident() {
		return resident;
	}

	public void setResident(int resident) {
		this.resident = resident;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getAddreddOtherId() {
		return addreddOtherId;
	}

	public void setAddreddOtherId(int addreddOtherId) {
		this.addreddOtherId = addreddOtherId;
	}

	public int getDelete_flag() {
		return delete_flag;
	}

	public void setDelete_flag(int delete_flag) {
		this.delete_flag = delete_flag;
	}

	public Date getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}

	public String getDepartureCity() {
		return departureCity;
	}

	public void setDepartureCity(String departureCity) {
		this.departureCity = departureCity;
	}

	public long getBack_time() {
		return back_time;
	}

	public void setBack_time(long back_time) {
		this.back_time = back_time;
	}

	public long getStart_time() {
		return start_time;
	}

	public void setStart_time(long start_time) {
		this.start_time = start_time;
	}

	public int getStatus() {
		long currentTime = System.currentTimeMillis()/1000;
		if(currentTime < start_time){
			return TravelConf.TRAVEL_NOTSTART;
		}else if(currentTime > back_time){
			return TravelConf.TRAVEL_EXPIRED;
		}else{
			return TravelConf.TRAVEL_STARTING;
		}	
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Travel [id=" + id + ", userId=" + userId + ", toCountry="
				+ toCountry + ", resident=" + resident + ", departureCity="
				+ departureCity + ", back_time=" + back_time + ", start_time="
				+ start_time + ", description=" + description
				+ ", addreddOtherId=" + addreddOtherId + ", delete_flag="
				+ delete_flag + ", update_time=" + update_time + ", status="
				+ status + "]";
	}

}
