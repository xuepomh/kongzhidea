package com.kk.hadoop.io;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import java.io.InputStream;
import java.net.URI;

/**
 * 通过FileSystem打开流返回的对象是个FSDataInputStream对象，该类实现了Seekable接口，
 * 
 * public interface Seekable {
	    void seek(long l) throws java.io.IOException;
	    long getPos() throws java.io.IOException;
	    boolean seekToNewSource(long l) throws java.io.IOException;
   }
   seek方法可跳到文件中的任意位置，我们这里跳到文件的初始位置再重新读一次
   
   seek是一个搞开销的操作
   
 * @author kk
 * 
 */
public class FileSystemDoubleCat {
	public static void main(String[] args) throws Exception {
		String uri = args[0];
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(uri), conf);
		FSDataInputStream in = null;
		try {
			in = fs.open(new Path(uri));
			IOUtils.copyBytes(in, System.out, 4096, false);
			in.seek(0);
			IOUtils.copyBytes(in, System.out, 4096, false);
		} finally {
			IOUtils.closeStream(in);
		}
	}
}