package com.kk.hadoop.io;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.net.URI;

/**
 * FileStatus 封装了hdfs文件和目录的元数据，包括文件的长度，块大小，重复数，修改时间，所有者，权限等信息，
 * FileSystem的getFileStatus可以获得这些信息，
 * 
 * @author kk
 * 
 */
public class ShowFileStatus {

	public static void main(String[] args) throws IOException {
		Path path = new Path(args[0]);
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(args[0]), conf);
		FileStatus status = fs.getFileStatus(path);
		System.out.println("path = " + status.getPath());
		System.out.println("owner = " + status.getOwner());
		System.out.println("block size = " + status.getBlockSize());
		System.out.println("permission = " + status.getPermission());
		System.out.println("replication = " + status.getReplication());
	}
}
// 输出结果样例
// path = hdfs://localhost:9000/user/hadoop/test/file1
// owner = kongzhidea
// block size = 67108864
// permission = rw-r--r--
// replication = 2
