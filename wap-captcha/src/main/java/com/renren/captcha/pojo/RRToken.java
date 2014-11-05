package com.renren.captcha.pojo;

import javax.servlet.ServletRequest;

/**
 * 从requst中得到token
 * 
 * @author zhihui.kong@renren-inc.com
 * 
 */
public class RRToken extends AbstractToken {

	public RRToken() {
		super();
	}

	public RRToken(String tokenStr) {
		super();
		token = tokenStr;
	}

	public RRToken(ServletRequest req) {
		super();
		// 解析token,参数为post。 目前触屏的有一个post参数，模式为 "_PASSWORD_7895642122"
		String param = req.getParameter("post");
		if (param != null) {
			if (param.indexOf("_") != -1) {
				String[] str = param.split("_");
				token = str[str.length - 1];
			} else {
				token = param;
			}
		}
	}

}
