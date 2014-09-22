package com.renren.shopping.model;

import java.util.Date;

public class TaskIndex {
	private long id;
	private long taskId;
	private String title;
	private String description;
	private Date update_time;
	private double score;

	public TaskIndex() {
		super();
	}

	public TaskIndex(long id, long taskId, String title, String description,
			Date update_time, double score) {
		super();
		this.id = id;
		this.taskId = taskId;
		this.title = title;
		this.description = description;
		this.update_time = update_time;
		this.score = score;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getTaskId() {
		return taskId;
	}

	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	@Override
	public String toString() {
		return "TaskIndex [id=" + id + ", taskId=" + taskId + ", title="
				+ title + ", description=" + description + ", update_time="
				+ update_time + ", score=" + score + "]";
	}

}
