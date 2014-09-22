package com.renren.shopping.services;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.multipart.MultipartFile;

import com.renren.shopping.conf.ResultConf;
import com.renren.shopping.conf.UploadConf;
import com.renren.shopping.model.UploadModel;
import com.renren.shopping.util.ImageUtils;
import com.renren.shopping.util.PhotoUploadUtil;

public class UploadService {

	public static final String IMAGE = "image";
	public static final String VOICE = "voice";
	private static final String UPLOADIDPREF = "renren_public2_";
	public static final Log logger = LogFactory.getLog(UploadService.class);

	private static UploadService _instance = new UploadService();

	private UploadService() {
	}

	public static UploadService getInstance() {
		return _instance;
	}

	/**
	 * 上传图片，返回图片不带前缀
	 * 
	 * @param file
	 *            上传的file
	 * @param uploadType
	 *            上传类型
	 * @param saveUrl
	 *            保存那种类型的url
	 * @return
	 * @throws Exception
	 * @throws PublicException
	 */
	public UploadModel upload(MultipartFile file, String uploadType)
			throws Exception {
		String fileName = file.getOriginalFilename();
		String tick = null;
		String pageType = null;
		if (VOICE.equalsIgnoreCase(uploadType)) {
			tick = PhotoUploadUtil.VOICE_TICK_VALUE;
			pageType = PhotoUploadUtil.voicePageType;
		} else {
			pageType = PhotoUploadUtil.imagePageType;
		}
		byte[] fileBlob = null;
		try {
			fileBlob = file.getBytes();
		} catch (IOException e) {
			throw new Exception("上传的文件不能为空文件。");
		}
		JSONObject jsonObject = PhotoUploadUtil.getInstance().upload(fileBlob,
				fileName, tick, UPLOADIDPREF, pageType);
		if(!jsonObject.containsKey("code")) {
			throw new Exception("上传图片失败，error info: " + jsonObject.toString());
		}
		//测试
		logger.info("[-----jsonObject-----]:"+jsonObject);
		UploadModel uploadModel = new UploadModel();
		uploadModel.setName(fileName);
		if (0 == jsonObject.getInt("code")) {
			JSONArray jsonArray = jsonObject.getJSONArray("files");
			JSONObject urlJsonObject = jsonArray.getJSONObject(0);
			if (VOICE.equalsIgnoreCase(uploadType)) {
				uploadModel.setType("voice");
				uploadModel.setOriginal_url(urlJsonObject.getString("url"));
			} else {
				uploadModel.setType("photo");
				JSONArray photoJsonArray = urlJsonObject.getJSONArray("images");
				// 设置大图和小图
				for (int i = 4; i >= 0; i--) {
					String url = photoJsonArray.getJSONObject(i).getString(
							"url");
					if (!StringUtils.isBlank(url)) {
						uploadModel.setOriginal_url(url);
						break;
					}
				}
				uploadModel.setMain_url(getImgUrl(photoJsonArray, "main"));
				uploadModel.setHead_url(getImgUrl(photoJsonArray, "head"));
				uploadModel.setTiny_url(getImgUrl(photoJsonArray, "tiny"));
			}
		} else {
			throw new Exception("上传的文件失败。");
		}

		return uploadModel; // 处理成对象
	}

	private String getImgUrl(JSONArray photoJsonArray, String type) {
		for (int i = 0; i < 5; i++) {
			if (photoJsonArray.getJSONObject(i).getString("type").indexOf(type) != -1) {
				return photoJsonArray.getJSONObject(i).getString("url");
			}
		}
		return null;
	}

	/**
	 * 上传图片 返回图片带url前缀，返回原图和tiny图
	 * 
	 * @param file
	 * @return
	 */
	public UploadModel upload(MultipartFile file) {
		UploadModel model = null;
		try {
			model = upload(file, IMAGE);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
		if (!StringUtils.isBlank(model.getOriginal_url())) {
			model.setOriginal_url(UploadConf.uploadPrefix
					+ model.getOriginal_url());
		}
		if (!StringUtils.isBlank(model.getMain_url())) {
			model.setMain_url(UploadConf.uploadPrefix + model.getMain_url());
		}
		if (!StringUtils.isBlank(model.getHead_url())) {
			model.setHead_url(UploadConf.uploadPrefix + model.getHead_url());
		}
		if (!StringUtils.isBlank(model.getTiny_url())) {
			model.setTiny_url(UploadConf.uploadPrefix + model.getTiny_url());
		}

		return model;
	}

	/**
	 * 上传图片 返回图片带url前缀，返回原图和tiny图
	 * 
	 * @param files
	 * @return
	 * @throws Exception
	 */
	public List<UploadModel> upload(List<MultipartFile> files) {
		List<UploadModel> list = new ArrayList<UploadModel>();
		for (MultipartFile file : files) {
			if (file == null) {
				continue;
			}
			try {
				UploadModel model = upload(file);
				if (model == null) {
					continue;
				}
				list.add(model);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		return list;
	}

	/**
	 * 上传验证
	 * 
	 * @return
	 */
	public JSONObject validate(MultipartFile file) {
		String imgHW = "";
		// 获取图片宽高
		BufferedImage sourceImg = null;
		try {
			sourceImg = ImageIO.read(file.getInputStream());
		} catch (Exception e) {
			return ResultConf.getResult(1, "文件解析存在问题");
		}
		if (file.isEmpty()) {
			return ResultConf.getResult(2, "文件为空");
		}
		// 限制大小
		int limitSize = 20 * 1024 * 1024;
		if (file.getSize() > limitSize) {
			return ResultConf.getResult(3, "文件超过20M");
		}
		String fileName = file.getOriginalFilename();
		String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
		if (!UploadConf.imageTypes.contains(fileType.toLowerCase())) {
			return ResultConf.getResult(4, "上传文件类型不合法");
		}
		return ResultConf.SUCCESSRET;
	}

	/**
	 * 上传验证
	 * 
	 * @return
	 */
	public JSONObject validate(List<MultipartFile> files) {
		if (files.size() > 10) {
			return ResultConf.getResult(11, "文件解析存在问题");
		}
		for (int i = 0; i < files.size(); i++) {
			MultipartFile file = files.get(i);
			JSONObject obj = validate(file);
			if (!ResultConf.isSuccess(obj)) {
				obj.put("error_position", i + 1);// 从1开始
				return obj;
			}
		}
		return ResultConf.SUCCESSRET;
	}
	
	/**
	 * 调用逛街项目上传图片接口
	 * @param files
	 * @return
	 */
	public List<UploadModel> uploadGj(List<MultipartFile> files) {
		List<UploadModel> list = new ArrayList<UploadModel>();
		for (MultipartFile file : files) {
			if (file == null) {
				continue;
			}
			try {
				UploadModel model = new UploadModel();
				String str = ImageUtils.upload(file);
				if (str == null || str.isEmpty()) {
					continue;
				}
				JSONObject json = JSONObject.fromObject(str);
				model.setOriginal_url(json.getString("url0"));
				model.setMain_url(json.getString("url1"));
				model.setTiny_url(json.getString("url3"));
				model.setHead_url(json.getString("url2"));
				list.add(model);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		return list;
	}
	/**
	 * 调用逛街项目上传图片接口
	 * @param files
	 * @return
	 */
	public UploadModel uploadGj(MultipartFile file) {
		if (file == null) {
			return null;
		}
		UploadModel model = new UploadModel();
		try {
			String str = ImageUtils.upload(file);
			if (str == null || str.isEmpty()) {
				return null;
			}
			JSONObject json = JSONObject.fromObject(str);
			if(1 == json.getInt("code")){
				model.setOriginal_url(json.getString("url0"));
				model.setMain_url(json.getString("url1"));
				model.setTiny_url(json.getString("url3"));
				model.setHead_url(json.getString("url2"));
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return model;
	}
}
