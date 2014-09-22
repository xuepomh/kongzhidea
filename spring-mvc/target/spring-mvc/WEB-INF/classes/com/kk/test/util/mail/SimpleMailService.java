package com.kk.test.util.mail;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * 纯文本邮件服务类.
 * 
 * @author calvin
 * @create-time 2013-10-30
 * @revision 1.0.0
 * @E_mail yong.cao@renren-inc.com
 */
public class SimpleMailService {

	private static Log logger = LogFactory.getLog(SimpleMailService.class);

	private JavaMailSender mailSender;

	/**
	 * 发送邮件
	 * 
	 * @param message
	 */
	public void sendMail(SimpleMailMessage message) {
		try {
			mailSender.send(message);
			logger.info("纯文本邮件已发送");
		} catch (Exception e) {
			logger.error("发送邮件失败", e);
		}
	}

	/**
	 * Spring的MailSender.
	 */
	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

}
