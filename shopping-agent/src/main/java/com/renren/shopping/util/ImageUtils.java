package com.renren.shopping.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import net.sf.json.JSONObject;

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
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.multipart.MultipartFile;

/**
 * 上传图片的API封装，提供了直接输入URL，完成上传。以及输入file，完成上传，两类接口。
 * 
 * @author xiejun 2011-10-26 上午11:56:58
 * @version 1.0.0
 */

public class ImageUtils {

    private static final Log LOG = LogFactory.getLog(ImageUtils.class);

    private static final String URL_UPLOAD_IMG = "http://upload.renren.com/uploadservice.fcgi?pagetype=addShop&hostid=0";

    private static final String URL_UPLOAD_IMG_NEW = "http://upload.renren.com/upload.fcgi?pagetype=guangjieiframe"
            + "&tick=ugc.renren.comiej_sd239928ddd34&hostid=0&uploadid=";

    public static final String URL_PRE = "http://fmn.rrimg.com/";

    private static int WIDTH = 70;

    private static int HEIGH = 70;

    private static int S_WIDTH = 60;

    private static int S_HEIGH = 60;

    public static final String M2_W200_H = "p/m2w200hq92l1t_";

    public static final String M2_W230_H = "p/m2w230hq92l1t_";
    
    public static final String M2_W230_H_2 = "p/m2w230hq92l2t_";

    public static final String M3_W212_H212 = "p/m3w212h212q92lt_";

    public static final String M3_W60_H60 = "p/m3w60h60q85lt_";
    
    public static final String M3_W100_H100 = "p/m3w100h100q85lt_";

    public static final String M3_W50_H50 = "p/m3w50h50q85lt_";

    /**
     * 图片上传接口，上传失败则返回null，成功返回json对象 注意返回json串并不代表完全成功，
     * 只是表明和服务器的交互结果正常。需要对json串分析后才能确认 是否完成上传
     * 
     * @param file
     * @return JSONObject
     * @exception
     */
    public static JSONObject upload1(MultipartFile file) {
        byte[] bytes = getPhotoStream(file);
        if (bytes.length > 0) {
            Part[] parts = {
                    new StringPart("encry", "EYZ@QpJr23)(jfxX_"),
                    new FilePart("file", new ByteArrayPartSource(file.getOriginalFilename(), bytes)) };
            try {
                return JSONObject.fromObject(postImage(parts));
            } catch (Exception e) {
                LOG.error(e);
            }
        }
        return null;
    }

    /**
     * 图片上传接口，上传失败则返回null，成功返回json串结果 注意返回json串并不代表完全成功，
     * 只是表明和服务器的交互结果正常。需要对json串分析后才能确认 是否完成上传
     * 
     * @param file
     * @return String
     * @exception
     */
    public static String upload(MultipartFile file) {
        JSONObject result = upload1(file);
        if (result != null) {
            return result.toString();
        } else {
            return null;
        }
    }

    private static byte[] getPhotoStream(MultipartFile file) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        InputStream in = null;
        try {
            in = file.getInputStream();
            byte[] bytes = new byte[4096];
            int len = 0;
            while ((len = in.read(bytes, 0, bytes.length)) > 0) {
                out.write(bytes, 0, len);
            }
            return out.toByteArray();
        } catch (IOException e1) {
            e1.printStackTrace();
            LOG.error("upload image failed.", e1);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new byte[] {};
    }

    private static byte[] getPhotoStream(String photoUrl) {
        HttpClient httpClient = new HttpClient();
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(15000);
        GetMethod method = new GetMethod(photoUrl);
        try {
            int statusCode = httpClient.executeMethod(method);
            if (statusCode != HttpStatus.SC_OK) {
                return new byte[] {};
            }
            return method.getResponseBody();
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            method.releaseConnection();
        }
        return new byte[] {};
    }

    private static String postImage(Part[] parts) throws ImageUploadExcetion, HttpException,
            IOException {

        HttpClient client = new HttpClient();
        client.getHttpConnectionManager().getParams().setConnectionTimeout(8000);
        client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
        PostMethod post = new PostMethod(URL_UPLOAD_IMG);
        post.setRequestEntity(new MultipartRequestEntity(parts, post.getParams()));
        try {
            int status = client.executeMethod(post);
            if (status == HttpStatus.SC_OK) {
                return post.getResponseBodyAsString();
            } else {
                return null;
            }
        } finally {
            post.releaseConnection();
        }
    }

    private static String postImageNew(Part[] parts) throws Exception {
        String result = null;
        HttpClient client = new HttpClient();
        client.getHttpConnectionManager().getParams().setConnectionTimeout(8000);
        client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
        PostMethod post = new PostMethod(URL_UPLOAD_IMG_NEW + getDateTimeFormat());
        post.setRequestEntity(new MultipartRequestEntity(parts, post.getParams()));
        int status = client.executeMethod(post);
        if (status == HttpStatus.SC_OK) {
            result = post.getResponseBodyAsString();
            int start = result.indexOf("\"images\":");
            start = result.indexOf("{", start);
            int end = result.indexOf("}", start) + 1;
            return result.substring(start, end);
        } else {
            return null;
        }
    }

    /*
    private static String getImageHeaderInfo(Part[] parts) throws Exception {
        String result = null;
        HttpClient client = new HttpClient();
        client.getHttpConnectionManager().getParams()
                .setConnectionTimeout(8000);
        client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,
                "utf-8");
        PostMethod post = new PostMethod(URL_UPLOAD_IMG_NEW
                + getDateTimeFormat());
        post.setRequestEntity(new MultipartRequestEntity(parts, post
                .getParams()));
        int status = client.executeMethod(post);
        if (status == HttpStatus.SC_OK) {
            result = post.getResponseBodyAsString();
            int start = result.indexOf("(")+1;
           // int start = result.indexOf("\"images\":");
            //start = result.indexOf("{", start);
            //int end = result.indexOf("}", start) + 1;
            int end = result.lastIndexOf(")");
            return result.substring(start, end);
        } else {
            return null;
        }
    }
    */
    public static byte[] getByteFromPhotoUrl(String photoUrl) {
        return getPhotoStream(photoUrl);
    }

    public static String getDateTimeFormat() {
        SimpleDateFormat dateformat1 = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateformat1.format(new Date());
    }

    public static String combine9Picture(List<String> picUrlList) throws IOException {
        for (int i = 0; i < picUrlList.size(); i++) {
            String tmp = picUrlList.get(i);
            int findex = tmp.indexOf('?');
            if (findex != -1) {
                tmp = tmp.substring(0, findex);
                picUrlList.set(i, tmp);
            }
        }
        BufferedImage ImageNew = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
        Graphics g = ImageNew.getGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, S_WIDTH * 3 + 20, S_HEIGH * 3 + 20);

        try {
            for (int i = 0; i < picUrlList.size(); i++) {
                String pic = picUrlList.get(i);
                URL url = new URL(pic);
                BufferedImage ImageOne = ImageIO.read(url);
                int width = ImageOne.getWidth();// 图片宽度
                int height = ImageOne.getHeight();// 图片高度

                int[] ImageArrayOne = new int[width * height];
                ImageArrayOne = ImageOne.getRGB(0, 0, width, height, ImageArrayOne, 0, width);
                int w = i % 3 * (S_WIDTH + 10);
                int h = i / 3 * (S_WIDTH + 10);
                ImageNew.setRGB(w, h, S_WIDTH, S_HEIGH, ImageArrayOne, 0, S_WIDTH);
            }
        } catch (Exception e) {
            LOG.error("combine image9url error", e);
        }

        String finename = picUrlList.get(0);
        finename = finename.substring(finename.lastIndexOf("/") + 1);
        Part[] parts = { new StringPart("encry", "EYZ@QpJr23)(jfxX_"),
                new FilePart(finename, new ByteArrayPartSource(finename, getPhotoStream(ImageNew))) };
        try {
            JSONObject json = JSONObject.fromObject(postImageNew(parts));
            json.put("url", URL_PRE + json.getString("url"));
            return json.toString();
        } catch (Exception e) {
            LOG.error(e.getStackTrace(), e);
        }
        return null;
    }

    public static String combine4Picture(List<String> picUrlList) throws IOException {
        for (int i = 0; i < picUrlList.size(); i++) {
            String tmp = picUrlList.get(i);
            int findex = tmp.indexOf('?');
            if (findex != -1) {
                tmp = tmp.substring(0, findex);
                picUrlList.set(i, tmp);
            }
        }
        BufferedImage ImageNew = new BufferedImage(212, 283, BufferedImage.TYPE_INT_RGB);
        Graphics g = ImageNew.getGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, WIDTH * 3 + 2, HEIGH * 4 + 3);

        URL url = new URL(picUrlList.get(0));
        BufferedImage ImageOne = ImageIO.read(url);
        int width = ImageOne.getWidth();// 图片宽度
        int height = ImageOne.getHeight();// 图片高度

        int[] ImageArrayOne = new int[width * height];
        ImageArrayOne = ImageOne.getRGB(0, 0, width, height, ImageArrayOne, 0, width);
        ImageNew.setRGB(0, 0, 212, 212, ImageArrayOne, 0, 212);

        int k = 0;
        for (int i = 1; i < picUrlList.size(); i++, k++) {
            String pic = picUrlList.get(i);
            url = new URL(pic);
            ImageOne = ImageIO.read(url);
            width = ImageOne.getWidth();// 图片宽度
            height = ImageOne.getHeight();// 图片高度

            ImageArrayOne = new int[width * height];
            ImageArrayOne = ImageOne.getRGB(0, 0, width, height, ImageArrayOne, 0, width);
            ImageNew.setRGB(k * WIDTH + k, 213, WIDTH, HEIGH, ImageArrayOne, 0, WIDTH);
        }
        String finename = picUrlList.get(0);
        finename = finename.substring(finename.lastIndexOf("/") + 1);
        Part[] parts = { new StringPart("encry", "EYZ@QpJr23)(jfxX_"),
                new FilePart(finename, new ByteArrayPartSource(finename, getPhotoStream(ImageNew))) };
        try {
            JSONObject json = JSONObject.fromObject(postImageNew(parts));
            json.put("url", URL_PRE + json.getString("url"));
            return json.toString();
        } catch (Exception e) {
            LOG.error(e.getStackTrace(), e);
        }
        return null;
    }

    private static byte[] getPhotoStream(BufferedImage file) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            ImageIO.write(file, "jpg", out);
            return out.toByteArray();
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new byte[] {};
    }

    public static String parseImageUrl(String picUrl, String type) {
        if (picUrl == null) {
            return "";
        }
        try {
            int index = picUrl.lastIndexOf("/");
            String pre = picUrl.substring(0, index + 1);
            String suf = picUrl.substring(index + 1, picUrl.length());
            String ret = pre + type + suf;
            return ret;
        } catch (Exception e) {
            LOG.error("parseImageUrl error", e);
            return picUrl;
        }
    }

    public static int parseImage200Height(int width, int height) {
        if (width != 200 && width > 0) {
            height = (200 * height) / width;
        }
        return height;
    }

    public static JSONObject getImageInfo(String pic) throws IOException {
    	JSONObject json = new JSONObject();
    	URL url = new URL(pic);
    	BufferedImage ImageOne = ImageIO.read(url);
    	int height = ImageOne.getHeight();
    	int width = ImageOne.getWidth();
    	json.put("w", width);
    	json.put("h", height);
    	return json;
    }
    
    
	public static void main(String[] args) throws IOException {
		List<String> picList = new ArrayList<String>();
		picList.add("http://fmn.rrimg.com/fmn057/gouwu/20120716/1040/p/m3w60h60q85lt_large_9iAe_5a2300000cab125e.jpg");
		picList.add("http://fmn.rrimg.com/fmn057/gouwu/20120716/1040/p/m3w60h60q85lt_large_9iAe_5a2300000cab125e.jpg");
		picList.add("http://fmn.rrimg.com/fmn057/gouwu/20120716/1040/p/m3w60h60q85lt_large_9iAe_5a2300000cab125e.jpg");
		picList.add("http://fmn.rrimg.com/fmn057/gouwu/20120716/1040/p/m3w60h60q85lt_large_9iAe_5a2300000cab125e.jpg");
		picList.add("http://fmn.rrimg.com/fmn057/gouwu/20120716/1040/p/m3w60h60q85lt_large_9iAe_5a2300000cab125e.jpg");
		picList.add("http://fmn.rrimg.com/fmn057/gouwu/20120716/1040/p/m3w60h60q85lt_large_9iAe_5a2300000cab125e.jpg");
		picList.add("http://fmn.rrimg.com/fmn057/gouwu/20120716/1040/p/m3w60h60q85lt_large_9iAe_5a2300000cab125e.jpg");
		picList.add("http://fmn.rrimg.com/fmn057/gouwu/20120716/1040/p/m3w60h60q85lt_large_9iAe_5a2300000cab125e.jpg");
		picList.add("http://fmn.rrimg.com/fmn057/gouwu/20120716/1040/p/m3w60h60q85lt_large_9iAe_5a2300000cab125e.jpg");
		// System.out.println(combine4Picture(picList));
		JSONObject tmp4pic = JSONObject.fromObject(combine9Picture(picList));
		System.out.println("----" + tmp4pic);
		System.out.println(tmp4pic.getString("url"));
	}

}
