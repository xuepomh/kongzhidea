package com.renren.captcha.service;

import org.apache.commons.lang.StringUtils;


import com.renren.captcha.manager.CaptchaManager;
import com.renren.captcha.pojo.AbstractToken;
import com.renren.captcha.pojo.RRToken;

/**
 * author:dapeng.zhou@renren-inc.com <br/>
 * date:2014-6-10 下午2:24:00
 */
public class CaptchaServiceImpl {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	private CaptchaManager captchaManager;

	public boolean checkValicode(String token, String valicode) {
		AbstractToken rrToken = new RRToken(token);
		if (!rrToken.isValid()) {
			return false;
		}
		return captchaManager.checkValicode(rrToken.getToken(), valicode);
	}

	public void setCaptchaManager(CaptchaManager captchaManager) {
		this.captchaManager = captchaManager;
	}

}
