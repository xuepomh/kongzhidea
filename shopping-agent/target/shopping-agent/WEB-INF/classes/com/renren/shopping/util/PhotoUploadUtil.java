package com.renren.shopping.util;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.ByteArrayPartSource;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import net.sf.json.JSONObject;

public class PhotoUploadUtil {
	
	private static Log logger = LogFactory.getLog(PhotoUploadUtil.class);

    private static PhotoUploadUtil instance = new PhotoUploadUtil();

    public static PhotoUploadUtil getInstance() {
        return instance;
    }

    private PhotoUploadUtil() {
    }
    
    public static String staticsPref = "http://fmn.rrimg.com/";
    
	public static String uploadUrl = "http://upload.d.xiaonei.com/upload.fcgi?pagetype="; // 上传的URL
	
	public static String imagePageType = "publicplatform"; //照片类型的上传类型
	
	public static String voicePageType = "publicplatformvoice"; //语音类型的上传类型

	private final static String ENCRYPTION_KEY = "EYZAQpJr2DFjfxXH";// 用于上传的key，保证两端服务的安全

	public static String FIELD_FILE = "file";

	public static String FIELD_KEY = "key";

	public static String FIELD_ENCRYPTION = "encryption";

	public static String TICK_KEY = "tick"; // 内网访问使用固定票

	public static String PHOTO_TICK_VALUE = "ugc.renren.com_6e0593cfbcecdabae831"; // 固定票的值

	public static String VOICE_TICK_VALUE = "ugc.renren.com_325b06e285f4f0c1fade"; // 固定票的值
	
	public static String HOSTID = "hostid";

	public static String UPLOADID = "uploadid";

	public static int TIME_OUT = 10000;

	/**
	 * @param blob 文件的二进制
	 * @param fileName 文件的原始名称
	 * @param tick 上传使用的票
	 * @param uploadId 上传到额Id "renren_public2_" + hostId
	 * @param pageType 上传到额类型
	 * @return 上传成功的Json串
	 */
	public JSONObject upload(byte[] blob, String fileName,
			String tick, String uploadId, String pageType) {

		Map<String, String> uploadParams = new TreeMap<String, String>();

		uploadParams.put(PhotoUploadUtil.FIELD_KEY, ENCRYPTION_KEY);
		uploadParams.put(PhotoUploadUtil.FIELD_ENCRYPTION, ENCRYPTION_KEY);
		if (tick != null && tick.trim().length() != 0) {
			uploadParams.put(PhotoUploadUtil.TICK_KEY, tick);
		} else {
			uploadParams.put(PhotoUploadUtil.TICK_KEY,
					PhotoUploadUtil.PHOTO_TICK_VALUE);
		}
		uploadParams.put(PhotoUploadUtil.HOSTID, String.valueOf(260011478));
		uploadParams.put(PhotoUploadUtil.UPLOADID, uploadId + "_260011478");

		Part[] parts = new Part[uploadParams.size() + 1];
		int i = 0;
		for (Map.Entry<String, String> param : uploadParams.entrySet()) {
			String key = param.getKey();
			String value = param.getValue();
			StringPart stringPart = new StringPart(key, value);
			// charset必须设置,否则默认就是US_ASCII
			stringPart.setCharSet("UTF-8");
			parts[i++] = stringPart;
		}
		parts[i] = new FilePart(FIELD_FILE, new ByteArrayPartSource(fileName,
				blob));

		JSONObject result = uploadPhotoCppProcess(uploadUrl + pageType, parts);

		return result;
	}
	
	private JSONObject uploadPhotoCppProcess(String uploadUrl, Part[] parts) {
        HttpClient client = new HttpClient();
        client.getHttpConnectionManager().getParams().setConnectionTimeout(TIME_OUT);
        PostMethod post = new PostMethod(uploadUrl);

        post.setRequestEntity(new MultipartRequestEntity(parts, post.getParams()));

        if (logger.isDebugEnabled()) {
            logger.debug("post data to server: " + post.toString());
        }

        JSONObject jsonObject = new JSONObject();
        try {
            int status = client.executeMethod(post);
            byte[] body = post.getResponseBody();
            String strBody = new String(body);
            if (status == HttpStatus.SC_OK) {
                jsonObject = JSONObject.fromObject(strBody);
            } else {
                logger.error("Upload photo failed. HTTP Response status code is: " + status
                        + "response body is:" + strBody);
            }
        } catch (Exception e) {
            logger.error("Upload photo faild." + e.getMessage(), e);
        }
        return jsonObject;
    }
	

}
