package com.baobaotao.oxm.xstream.converters;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class DateConverter implements Converter {
	private Locale locale;
	public static final String DATEFORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
			DATEFORMAT_YYYY_MM_DD_HH_MM_SS);

	public DateConverter(Locale locale) {
		super();
		this.locale = locale;
	}

	/**
	 * ʵ�ָ÷������ж�Ҫת��������
	 */
	public boolean canConvert(Class clazz) {
		return Date.class.isAssignableFrom(clazz);
	}

	/**
	 * ʵ�ָ÷�������дJAVA����XMLת���߼�
	 */
	public void marshal(Object value, HierarchicalStreamWriter writer,
			MarshallingContext context) {
//		 DateFormat formatter = DateFormat.getDateInstance(DateFormat.FULL,
//		 this.locale);
//		 System.out.println(formatter.format(value));
//		 writer.setValue(formatter.format(value));
		try {
			System.out.println(value);
			writer.setValue(DATE_FORMAT.format(value));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * ʵ�ָ÷�������дXML��JAVA����ת���߼�
	 */
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {
//		 GregorianCalendar calendar = new GregorianCalendar();
//		 DateFormat formatter = DateFormat.getDateInstance(DateFormat.FULL,
//		 this.locale);
//		 try {
//		 calendar.setTime(formatter.parse(reader.getValue()));
//		 } catch (ParseException e) {
//		 throw new ConversionException(e.getMessage(), e);
//		 }
//		 return calendar.getGregorianChange();
		try {
			return DATE_FORMAT.parseObject(reader.getValue());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
