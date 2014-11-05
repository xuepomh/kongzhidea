package com.renren.captcha.manager;

import com.renren.captcha.pojo.AbstractCaptcha;

/**
 * author:dapeng.zhou@renren-inc.com <br/>
 * date:2014-6-10 下午2:35:33
 */
public interface CaptchaManager {

    /**
     * 生成并缓存验证码
     * 
     * @param token
     * @param valicode
     * @return
     */
    AbstractCaptcha generateAndCacheCaptcha(String token);

    /**
     * 缓存验证码
     * 
     * @param token
     * @param valicode
     * @return
     */
    boolean cacheCaptcha(String token, String code);

    /**
     * 校验验证码
     * 
     * @param token
     * @param valicode
     * @return
     */
    boolean checkValicode(String token, String valicode);
}
