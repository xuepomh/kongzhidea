package com.renren.shopping.model;
/**
 * 订单
 * @author yu.zhuang
 */
import java.util.Date;

public class Orders {
	private int id;
	private long order_number;//订单号
//	private int receiver_id;//收货人id
	private int owner_id;
	private int buyer_id;//买手id
	private long task_id;//任务id
	private double total_money;//订单总价
	private Date create_time;
	private Date update_time;
	private float score;
	private String comments;
	private String leave_message;
	
	private int addressId;
	private String title;
	private double gratuity;
	private String message;
	private double money;
	private int order_status;//订单状态 ,待确认0，代付款1，采购中2，采购完成3，等待买家确认收货4，待评价5，已完成6
	
	public int getAddressId() {
		return addressId;
	}
	public void setAddressId(int addressId) {
		this.addressId = addressId;
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
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public double getMoney() {
		return money;
	}
	public void setMoney(double money) {
		this.money = money;
	}
	public String getLeave_message() {
		return leave_message;
	}
	public void setLeave_message(String leave_message) {
		this.leave_message = leave_message;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public long getOrder_number() {
		return order_number;
	}
	public void setOrder_number(long order_number) {
		this.order_number = order_number;
	}
	public int getBuyer_id() {
		return buyer_id;
	}
	public void setBuyer_id(int buyer_id) {
		this.buyer_id = buyer_id;
	}
	public long getTask_id() {
		return task_id;
	}
	public void setTask_id(long task_id) {
		this.task_id = task_id;
	}
	public double getTotal_money() {
		return total_money;
	}
	public void setTotal_money(double total_money) {
		this.total_money = total_money;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public Date getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}
	public float getScore() {
		return score;
	}
	public void setScore(float score) {
		this.score = score;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public int getOrder_status() {
		return order_status;
	}
	public void setOrder_status(int order_status) {
		this.order_status = order_status;
	}
	public int getOwner_id() {
		return owner_id;
	}
	public void setOwner_id(int owner_id) {
		this.owner_id = owner_id;
	}
	@Override
	public String toString() {
		return "Orders [id=" + id + ", order_number=" + order_number
				+ ", owner_id=" + owner_id + ", buyer_id=" + buyer_id
				+ ", task_id=" + task_id + ", total_money=" + total_money
				+ ", create_time=" + create_time + ", update_time="
				+ update_time + ", score=" + score + ", comments=" + comments
				+ ", leave_message=" + leave_message + ", addressId="
				+ addressId + ", title=" + title + ", gratuity=" + gratuity
				+ ", message=" + message + ", money=" + money
				+ ", order_status=" + order_status + "]";
	}
}
