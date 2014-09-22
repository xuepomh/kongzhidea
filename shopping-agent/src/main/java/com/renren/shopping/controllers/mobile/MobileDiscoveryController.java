package com.renren.shopping.controllers.mobile;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.renren.shopping.conf.ResultConf;
import com.renren.shopping.model.Discovery;
import com.renren.shopping.model.UploadModel;
import com.renren.shopping.services.DiscoveryService;
import com.renren.shopping.services.UploadService;

@Controller
@RequestMapping("/mobile/discovery")
public class MobileDiscoveryController {
	
	@Autowired
	DiscoveryService discoveryService;
	
	public static Log logger = LogFactory.getLog(MobileDiscoveryController.class);
	
	@RequestMapping(value = "/publish" , method = { RequestMethod.POST })
	@ResponseBody
	public Object publishDiscovery(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam("title") String title,
			@RequestParam("buyer_ids") String buyer_ids,
			@RequestParam("article") String article){
		if(title == null || title.equals("")){
			return new JSONObject().put("warn","亲，标题不能为空哦！");
		}
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile main_pic = multipartRequest.getFile("main_pic");
		MultipartFile background_pic = multipartRequest.getFile("background_pic");
		JSONObject validate_main = UploadService.getInstance().validate(main_pic);
		JSONObject validate_bground = UploadService.getInstance().validate(background_pic);
		if(!ResultConf.isSuccess(validate_main)){
			return validate_main;
		}
		if(!ResultConf.isSuccess(validate_bground)){
			return validate_bground;
		}
		UploadModel models_main = UploadService.getInstance().uploadGj(
				main_pic);
		UploadModel models_bground = UploadService.getInstance().uploadGj(
				background_pic);
		if(models_main == null){
			return ResultConf.getResult(161, "亲，发布失败哦");
		}
		if(models_bground == null){
			return ResultConf.getResult(161, "亲，发布失败哦");
		}
		Discovery discovery = new Discovery();
		discovery.setArticle(article);
		discovery.setBackground_pic_url(models_bground.getMain_url());
		discovery.setMain_pic_url(models_main.getMain_url());
		discovery.setBuyer_ids(buyer_ids);
		discovery.setTitle(title);
		discovery.setUpdate_time(new Date());
		discoveryService.addDiscovery(discovery);
		return ResultConf.getResult( 0 , "操作成功！");
	}
}
