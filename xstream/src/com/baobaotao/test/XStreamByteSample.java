package com.baobaotao.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.mapper.MapperWrapper;

public class XStreamByteSample {
	private static XStream xstream;
	static {
		xstream = new XStream() {

			@Override
			protected MapperWrapper wrapMapper(MapperWrapper next) {
				return new MapperWrapper(next) {

					@Override
					public boolean shouldSerializeMember(Class definedIn,
							String fieldName) {
						if (definedIn == Object.class) {
							try {
								return this.realClass(fieldName) != null;
							} catch (Exception e) {
								return false;
							}
						} else {
							return super.shouldSerializeMember(definedIn,
									fieldName);
						}
					}

				};
			}
		};
	}
	private static byte[] b;

	public static Book getBook() {
		List<Student> stus = new ArrayList<Student>();
		stus.add(new Student());
		Book book = new Book();
		book.setId(1);
		book.setName("kk");
		book.setStus(stus);
		return book;
	}

	public static Book objectToXml() throws Exception {
		Book book = getBook();
//		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		String ret = xstream.toXML(book);
		System.out.println(ret);
//		b = outStream.toByteArray();
		return book;
	}

	public static Book xmlToObject() throws Exception {
		String str = "<com.baobaotao.test.Book>  <age>1</age> <id>1</id>   <name>kk</name>   <stus>     <com.baobaotao.test.Student>       <age>1</age>     </com.baobaotao.test.Student>   </stus> </com.baobaotao.test.Book>";
		b = str.getBytes("utf-8");
		InputStream fis = new ByteArrayInputStream(b, 0, b.length);
		Book u = (Book) xstream.fromXML(fis);
		return u;
	}

	public static void main(String[] args) throws Exception {
		 objectToXml();
//		 System.out.println(new String(b, "utf-8"));
//		Book book = xmlToObject();
//		System.out.println(book);
	}
}
