package com.renren.shopping.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeFormatUtil {
	public static String format(long time, String formatter) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatter);
		Date date = new Date(time);
		return simpleDateFormat.format(date);
	}
}
