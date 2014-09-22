package com.kk.hadoop.io;

import java.io.InputStream;
import java.net.URL;

import org.apache.hadoop.fs.FsUrlStreamHandlerFactory;
import org.apache.hadoop.io.IOUtils;

/**
 * 使用Hapoop URL 读取数据
 * 
 * 比较简单的读取hdfs数据的方法就是通过java.net.URL打开一个流，
 * 
 * 不过在这之前先要预先调用它的setURLStreamHandlerFactory方法设置为FsUrlStreamHandlerFactory
 * （由此工厂取解析hdfs协议
 * ），这个方法只能调用一次，所以要写在静态块中。然后调用IOUtils类的copyBytes将hdfs数据流拷贝到标准输出流System
 * .out中，copyBytes前两个参数好理解
 * ，一个输入，一个输出，第三个是缓存大小，第四个指定拷贝完毕后是否关闭流。我们这里要设置为false，标准输出流不关闭，我们要手动关闭输入流。
 * 
 * @author kk
 * 
 */
public class URLCat {
	static {
		URL.setURLStreamHandlerFactory(new FsUrlStreamHandlerFactory());
	}

	public static void main(String[] args) {
		// 这样在本机上执行
		String input = "hdfs://localhost:9000/user/hadoop/test/input";
		if (args.length > 0) {
			input = args[0];
		}
		InputStream in = null;
		try {
			in = new URL(input).openStream();
			IOUtils.copyBytes(in, System.out, 4096, false);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeStream(in);
		}
	}
}
