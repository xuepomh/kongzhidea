package com.renren.captcha.manager;

import com.renren.captcha.cache.CacheService;
import com.renren.captcha.pojo.AbstractCaptcha;
import com.renren.captcha.util.CaptchaUtil;

/**
 * author:dapeng.zhou@renren-inc.com <br/>
 * date:2014-6-10 下午2:36:33
 */
public class CaptchaManagerImpl implements CaptchaManager {

    private CacheService cacheService;

    public void setCacheService(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @Override
    public boolean checkValicode(String token, String valicode) {
        return cacheService.checkCaptcha(token, valicode);
    }

    @Override
    public AbstractCaptcha generateAndCacheCaptcha(String token) {
        // 生成验证码
        AbstractCaptcha captcha = CaptchaUtil.generateCaptcha();
        // 缓存验证码
        cacheService.cacheCaptcha(token, captcha.getCode());
        return captcha;
    }

    @Override
    public boolean cacheCaptcha(String token, String code) {
        // 缓存验证码
        cacheService.cacheCaptcha(token, code);
        return false;
    }

}
