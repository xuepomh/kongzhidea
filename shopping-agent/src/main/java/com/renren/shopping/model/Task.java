package com.renren.shopping.model;

import java.util.Date;

import com.renren.shopping.conf.TaskConf;

/**
 * 买家发布购买任务
 * 
 * @author kk
 * 
 */
public class Task {
	private long id;
	private int userId;
	private String title;// 标题
	private double gratuity;// 跑腿费 比例 0-1之间
	private long deadline;// 失效时间 unix时间戳
	private String message;// 任务描述
	private String goodCountry;// 购买物品的国家 补充信息
	private double money;// 金额上限 补充信息
	private int goodNumber;// 商品个数 补充信息
	private String liveCity;// 居住城市 补充信息
	private int addressOtherId;// 地址id 补充信息 @delete
	private long goodId;// 商品ID 一个任务对应一个商品
	private String remark;// 备注
	private int delete_flag;// 是否删除标记,0:未删除;1被删除
	private Date update_time;
	private int status; //0:待认领 , 1:已认领 ,2:已过期

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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public double getGratuity() {
		return gratuity;
	}

	public void setGratuity(double gratuity) {
		this.gratuity = gratuity;
	}

	public long getDeadline() {
		return deadline;
	}

	public void setDeadline(long deadline) {
		this.deadline = deadline;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getGoodCountry() {
		return goodCountry;
	}

	public void setGoodCountry(String goodCountry) {
		this.goodCountry = goodCountry;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public int getGoodNumber() {
		return goodNumber;
	}

	public void setGoodNumber(int goodNumber) {
		this.goodNumber = goodNumber;
	}

	public String getLiveCity() {
		return liveCity;
	}

	public void setLiveCity(String liveCity) {
		this.liveCity = liveCity;
	}

	public int getAddressOtherId() {
		return addressOtherId;
	}

	public void setAddressOtherId(int addressOtherId) {
		this.addressOtherId = addressOtherId;
	}

	public long getGoodId() {
		return goodId;
	}

	public void setGoodId(long goodId) {
		this.goodId = goodId;
	}

	public int getDelete_flag() {
		return delete_flag;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setDelete_flag(int delete_flag) {
		this.delete_flag = delete_flag;
	}

	public Date getUpdate_time() {
		return update_time;
	}

	@Override
	public String toString() {
		return "Task [id=" + id + ", userId=" + userId + ", title=" + title
				+ ", gratuity=" + gratuity + ", deadline=" + deadline
				+ ", message=" + message + ", goodCountry=" + goodCountry
				+ ", money=" + money + ", goodNumber=" + goodNumber
				+ ", liveCity=" + liveCity + ", addressOtherId="
				+ addressOtherId + ", goodId=" + goodId + ", remark=" + remark
				+ ", delete_flag=" + delete_flag + ", update_time="
				+ update_time + ", status=" + status + "]";
	}

	public int getStatus() {
		if (status == TaskConf.TASK_TAKE_AWAY) {
			return TaskConf.TASK_TAKE_AWAY;
		}
		if((System.currentTimeMillis()/1000 > this.deadline)){
			return TaskConf.TASK_EXPIRED;
		}
		return TaskConf.TASK_WAITING;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}

}
