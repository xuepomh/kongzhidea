package com.kk.hadoop.io;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.util.Progressable;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

/**
 * 写操作,复制本地文件到hdfs
 * 
 * FileSystem类有很多种创建文件的方法，最简单的一种是 public FSDataOutputStream create(Path f)
 * throws IOException
 * 
 * 它还有很多重载方法，可以指定是否强制覆盖已存在的文件，文件的重复因子，写缓存的大小，文件的块大小，文件的权限等。
 * 
 * 还可以指定一个回调接口： public interface Progressable { void progress(); }
 * 
 * 和普通文件系统一样，也支持apend操作，写日志时最常用 public FSDataOutputStream append(Path f) throws
 * IOException
 * 
 * 但并非所有hadoop文件系统都支持append，hdfs支持，s3就不支持。
 * 
 * @author kk
 * 
 */
public class FileCopyWithProgress {
	public static void main(String[] args) throws Exception {
		String localSrc = args[0];// /home/kongzhidea/share/zhongwen
		String dst = args[1];// hdfs://localhost:9000/user/hadoop/test2/zhongwen

		InputStream in = new BufferedInputStream(new FileInputStream(localSrc));

		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(dst), conf);

		// create时候 会自动生成所有的父目录,如果存在则会覆盖
		// Progressable 回调函数，每写入64KB即调用一次
		OutputStream out = fs.create(new Path(dst), new Progressable() {

			public void progress() {
				System.out.print(".");
			}
		});

		// 最后一个参数如果是true,则自动close
		IOUtils.copyBytes(in, out, 4096, true);
	}
}