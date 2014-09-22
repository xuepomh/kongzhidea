package com.renren.shopping.model;

import java.util.Date;

public class SearchHotWord {
	private long id;
	private String word;
	private int searchNum;
	private Date update_time;

	public SearchHotWord() {
	}

	public SearchHotWord(String word, int searchNum) {
		this.word = word;
		this.searchNum = searchNum;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public int getSearchNum() {
		return searchNum;
	}

	public void setSearchNum(int searchNum) {
		this.searchNum = searchNum;
	}

	public Date getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}

	@Override
	public String toString() {
		return "SearchHotWord [id=" + id + ", word=" + word + ", searchNum="
				+ searchNum + ", update_time=" + update_time + "]";
	}

}
