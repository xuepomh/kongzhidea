package com.renren.shopping.controllers.test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.renren.shopping.conf.ResultConf;
import com.renren.shopping.services.UploadService;
import com.renren.shopping.util.ImageUtils;

@Controller
@RequestMapping("/mobile/test/")
public class UploadPicController {
	
	public static Log logger = LogFactory.getLog(UploadPicController.class);
	
	@RequestMapping(value = "/pic" , method = { RequestMethod.POST })
	@ResponseBody
	public Object testPics(HttpServletRequest request,
			HttpServletResponse response){
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile main_pic = multipartRequest.getFile("main_pic");
		MultipartFile background_pic = multipartRequest.getFile("background_pic");
		MultipartFile test_pic = multipartRequest.getFile("test_pic");
		MultipartFile test_pic1 = multipartRequest.getFile("test_pic1");
		JSONObject validate_main = UploadService.getInstance().validate(main_pic);
		JSONObject validate_bground = UploadService.getInstance().validate(background_pic);
		if(!ResultConf.isSuccess(validate_main)){
			return validate_main;
		}
		if(!ResultConf.isSuccess(validate_bground)){
			return validate_bground;
		}
//		UploadModel models_main = UploadService.getInstance().upload(
//				main_pic);
//		UploadModel models_bground = UploadService.getInstance().upload(
//				background_pic);
		logger.info("[---ImageUtils.upload.main_pic---]:" + ImageUtils.upload(main_pic));
		logger.info("[---ImageUtils.upload.back_pic---]:" + ImageUtils.upload(background_pic));
		JSONObject json = new JSONObject();
		json.put("main_pic", ImageUtils.upload(main_pic));
		json.put("back_pic", ImageUtils.upload(background_pic));
		json.put("test_pic", ImageUtils.upload(test_pic));
		json.put("test1_pic", ImageUtils.upload(test_pic1));
		return ResultConf.getSuccessResult(json);
	}
}
