package com.renren.shopping.services;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.renren.shopping.conf.KVConf;
import com.renren.shopping.util.RandomUtil;
import com.renren.shopping.util.RedisAPI;
import com.renren.shopping.util.RedisUtil;
import com.renren.smsis.AbsSms;
import com.renren.smsis.CPPDImpl;

@Service
public class PasswordMessageService {

	private final Log logger = LogFactory.getLog(PasswordMessageService.class);

	public static final int verifyCodeLength = 4;

	private static final String smsPassword = "qweASD123zxc";

	private static final int PDID = 10350001;

	private static final int CPID = 1035;

	private static CPPDImpl cppd = new CPPDImpl();

	public boolean sendMessageVerify(String phoneNumber) {
		String verifyCode = RandomUtil
				.randomFixedLengthNumeralString(verifyCodeLength);
		if (logger.isInfoEnabled()) {
			logger.info("PhoneNumber:" + phoneNumber + "&&&&VerifyCode:"
					+ verifyCode);
		}
		// 发送短信验证码
		AbsSms sms = new AbsSms() {
			@Override
			public void setMTID(List<IMsgID> arg0) {
			}

			@Override
			public void setErrMsg(String arg0) {
			}

			@Override
			public long getYMDHM() {
				return 0;
			}

			@Override
			public int getPDID() {
				return PDID;
			}

			@Override
			public String getFrom() {
				return "人人网";
			}

			@Override
			public String getExtNo() {
				return "";
			}

			@Override
			public int getCPID() {
				return CPID;
			}
		};
		sms.addMobile(phoneNumber);
		sms.addSmsPart(verifyCode);

		int count = 1;
		while (!cppd.SendSMS(sms, smsPassword)) {
			cppd.SendSMS(sms, smsPassword);
			count++;
			logger.info("sendSMS failed, count: " + count + ", PhoneNumber:"
					+ phoneNumber + ", VerifyCode:" + verifyCode);
			if (count > 5) {
				logger.error("sendSMS failed, 5 calls, " + ", PhoneNumber:"
						+ phoneNumber + ", VerifyCode:" + verifyCode);
				break;
			}
		}
		// TODO 暂时没有发送失败
		// if (count > 5) {
		// return false;
		// }

		// 存储验证码，到通用cache
		saveVerifiCode(phoneNumber, verifyCode);
		return true;
	}

	private void saveVerifiCode(String phoneNumber, String code) {
		// 设置一分钟内发过短信
		RedisAPI.setex(
				KVConf.getPhonePasswordTimeKey(phoneNumber),
				KVConf.PASSWORD_TIME, KVConf.PHONE_PASSWORD_TIME_VALUE);
		// 存储发送短信验证码
		String codes = RedisAPI.get(
				KVConf.getPhonePasswordKey(phoneNumber));
		if (StringUtils.isBlank(codes)) {
			codes = code;
		} else {
			codes = codes + "," + code;
		}
		RedisAPI.setex(KVConf.getPhonePasswordKey(phoneNumber),
				KVConf.PASSWORD_CODE_TIME, codes);
	}
}
