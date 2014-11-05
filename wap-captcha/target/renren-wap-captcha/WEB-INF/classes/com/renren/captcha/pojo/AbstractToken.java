package com.renren.captcha.pojo;

public abstract class AbstractToken {
	
	protected String token;

	public boolean isValid(){
		
		if (token==null || "".equals(token)) {
			return false;
		}
		return true;
	}
	
	public String getToken() {
		return token;
	}


}
