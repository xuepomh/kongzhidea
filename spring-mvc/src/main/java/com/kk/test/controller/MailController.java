package com.kk.test.controller;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.icegreen.greenmail.util.GreenMail;
import com.kk.test.util.mail.MimeMailService;
import com.kk.test.util.mail.SimpleMailService;

@Controller
@RequestMapping("/mail")
public class MailController {

	/**
	 * 邮件发送地址 和配置文件中的username相同
	 */
	private static final String MAIL_MESSAGE_FROM = "bloghotwordrr@163.com";

	/**
	 * 邮件接收地址
	 */
	private static final String MAIL_MESSAGE_TO = "zhihui.kong@renren-inc.com";

	/**
	 * 邮件标题
	 */
	private static final String MAIL_MESSAGE_SUBJECT = "test subject";

	/**
	 * 邮件内容
	 */
	private static final String MAIL_MESSAGE_CONTEXT = "test context";

	/**
	 * 附件文件名
	 */
	private static final String EMAIL_ATTACHMENT = "/email/mailAttachment.txt";

	@Autowired
	SimpleMailService simpleMailService;

	@Autowired
	MimeMailService mimeMailService;

	private GreenMail greenMail;

	/**
	 * 构造SimpleMessage
	 * 
	 * @return
	 */
	private SimpleMailMessage buildSimpleMessage() {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(MAIL_MESSAGE_FROM);
		message.setTo(MAIL_MESSAGE_TO);
		message.setSubject(MAIL_MESSAGE_SUBJECT);
		message.setText(MAIL_MESSAGE_CONTEXT);
		return message;
	}

	/**
	 * Message格式简单文本邮件发送
	 * 
	 * @return
	 */
	@RequestMapping("/simpleMailMessage")
	public ModelAndView simpleMailMessage() {
		ModelAndView mav = new ModelAndView("jsonView");

		SimpleMailMessage message = buildSimpleMessage();
		simpleMailService.sendMail(message);

		return mav;
	}

	/**
	 * MimeMessage格式邮件发送
	 * 
	 * @return
	 */
	@RequestMapping("/mimeMailMessage")
	public ModelAndView mimeMailMessage() {
		ModelAndView mav = new ModelAndView("jsonView");
		SimpleMailMessage message = buildSimpleMessage();
		mimeMailService.sendMail(message);
		return mav;
	}

	/**
	 * 邮件模板测试，不含附件
	 * 
	 * @return
	 */
	@RequestMapping("/mimeTemplateMailMessage")
	public ModelAndView mimeTemplateMailMessage() {
		ModelAndView mav = new ModelAndView("jsonView");

		SimpleMailMessage message = buildSimpleMessage();
		String filename = "mailTemplate.ftl";// pom.xml中要将.ftl引入到class中
		Map<String, String> params = Collections.singletonMap("userName",
				MAIL_MESSAGE_TO);
		mimeMailService.sendMailTemplate(message, filename, params);

		return mav;
	}

	/**
	 * 一个附件的邮件测试
	 * 
	 * @return
	 */
	@RequestMapping("/mimeAttachMailMessage")
	public ModelAndView mimeAttachMailMessage() {
		ModelAndView mav = new ModelAndView("jsonView");

		SimpleMailMessage message = buildSimpleMessage();
		String[] files = new String[] { EMAIL_ATTACHMENT };
		mimeMailService.sendMailFiles(message, files);

		return mav;
	}

	/**
	 * 多个附件的邮件测试
	 * 
	 * @return
	 */
	@RequestMapping("/mimeMulAttachMailMessage")
	public ModelAndView mimeMulAttachMailMessage() {
		ModelAndView mav = new ModelAndView("jsonView");

		SimpleMailMessage message = buildSimpleMessage();
		String[] files = new String[] { EMAIL_ATTACHMENT, EMAIL_ATTACHMENT };
		mimeMailService.sendMailFiles(message, files);

		return mav;
	}

}
