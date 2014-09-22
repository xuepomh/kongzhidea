package com.kk.hadoop.io;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.net.URI;

/**
 * File patterns
 * 当需要很多文件时，一个个列出路径是很不便捷的，hdfs提供了一个通配符列出文件的方法，通过FileSystem的globStatus方法提供了这个便捷
 * ，globStatus也有重载的方法，使用PathFilter过滤，那么我们结合两个来实现一下
 */
public class GlobStatus {
	public static void main(String[] args) throws IOException {
		// uri hdfs://localhost:9000/user/hadoop/test/*
		String uri = args[0];
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(uri), conf);

		FileStatus[] status = fs.globStatus(new Path(uri),
				new RegexExludePathFilter("file.*"));
		Path[] listedPaths = FileUtil.stat2Paths(status);
		for (Path p : listedPaths) {
			System.out.println(p);
		}
	}
}