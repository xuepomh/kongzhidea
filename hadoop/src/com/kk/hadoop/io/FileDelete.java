package com.kk.hadoop.io;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import java.net.URI;

/**
 * 删除文件
 * 
 * @author kk
 * 
 */
public class FileDelete {
	public static void main(String[] args) throws Exception {
		String dest = args[0];//

		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(dest), conf);

		// 只有 recursive=true时候，非空文件或目录才会被删除
		// 如果传入的paht为空文件或者空目录，那么recursive则会被忽略
		fs.delete(new Path(dest), true);

	}
}
