package com.renren.shopping.conf;

import java.util.ArrayList;
import java.util.List;

public class UploadConf {
	
	public static String uploadPrefix = "http://fmnp.rrimg.com/";
	public static String defaultSaveType = "main";
	
	public static List<String> uploadCategory = new ArrayList<String>();
	
	public static List<String> imageTypes = new ArrayList<String>();
	
	public static List<String> voiceTypes = new ArrayList<String>();
	
	static {
		initUploadCategory();
		initImageTypes();
		initVoiceTypes();
	}

	private static void initUploadCategory() {
		uploadCategory.add("tiny");
		uploadCategory.add("head");
		uploadCategory.add("main");
		uploadCategory.add("large");
		uploadCategory.add("xlarge");
		uploadCategory.add("original");
	}
	
	private static void initImageTypes(){
		imageTypes.add("jpg");
		imageTypes.add("jpeg");
		imageTypes.add("bmp");
		imageTypes.add("gif");
		imageTypes.add("png");
	}
	
	private static void initVoiceTypes(){
		voiceTypes.add("mp3");
//		voiceTypes.add("ogg");
	}
}
