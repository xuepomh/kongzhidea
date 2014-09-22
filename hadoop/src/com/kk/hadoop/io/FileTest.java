package com.kk.hadoop.io;

import java.io.IOException;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class FileTest {
	/**
	 * 上传本地文件到文件系统 即使hdfs上文件存在也会覆盖
	 * 
	 * @param src
	 * @param dest
	 * @throws IOException
	 */
	public static void uploadLocalFile2HDFS(String src0, String dest0)
			throws IOException {
		Configuration config = new Configuration();
		// FileSystem hdfs = FileSystem.get(config); //这样写不行
		FileSystem hdfs = FileSystem.get(URI.create(dest0), config); // yes
		Path src = new Path(src0);
		Path dst = new Path(dest0);
		hdfs.copyFromLocalFile(src, dst);
		hdfs.close();
	}

	/**
	 * 创建新文件，并写入 即使hdfs上文件存在也会覆盖
	 * 
	 * @param dest0
	 * @param content
	 * @throws IOException
	 */
	public static void createNewHDFSFile(String dest0, String content)
			throws IOException {
		Configuration config = new Configuration();
		FileSystem hdfs = FileSystem.get(URI.create(dest0), config);
		FSDataOutputStream os = hdfs.create(new Path(dest0));
		os.write(content.getBytes("UTF-8"));
		os.close();
		hdfs.close();
	}

	/**
	 * 删除文件或目录
	 * 
	 * @param dst
	 * @return 如果文件不存在则返回false
	 * @throws IOException
	 */
	public static boolean deleteHDFSFile(String dst) throws IOException {
		Configuration config = new Configuration();
		FileSystem hdfs = FileSystem.get(URI.create(dst), config);

		Path path = new Path(dst);
		// 只有 recursive=true时候，非空文件或目录才会被删除
		// 如果传入的paht为空文件或者空目录，那么recursive则会被忽略
		boolean isDeleted = hdfs.delete(path, true);

		hdfs.close();

		return isDeleted;
	}

	/**
	 * 读取文件
	 * 
	 * @param dst
	 * @return
	 * @throws Exception
	 */
	public static byte[] readHDFSFile(String dst) throws Exception {
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(dst), conf);

		// check if the file exists
		Path path = new Path(dst);
		if (fs.exists(path)) {
			FSDataInputStream is = fs.open(path);
			// get the file info to create the buffer
			FileStatus stat = fs.getFileStatus(path);

			// create the buffer
			byte[] buffer = new byte[(int) stat.getLen()];
			is.readFully(0, buffer);

			is.close();
			fs.close();

			return buffer;
		} else {
			throw new Exception("the file is not found .");
		}
	}

	/**
	 * 创建目录 如果存在则无影响
	 * 
	 * @param dir
	 * @throws IOException
	 */
	public static void mkdir(String dir) throws IOException {
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(dir), conf);

		fs.mkdirs(new Path(dir));

		fs.close();
	}

	/**
	 * 列举目录下所有文件和目录
	 * 
	 * @param dir
	 * @throws IOException
	 */
	public static void listAll(String dir) throws IOException {
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(dir), conf);

		FileStatus[] stats = fs.listStatus(new Path(dir));

		for (int i = 0; i < stats.length; ++i) {
			System.out.println(stats[i].getPath().toString());
		}
		fs.close();
	}

	/**
	 * 重命名
	 * 
	 * @param src0
	 * @param dest0
	 * @return
	 * @throws IOException
	 */
	public static boolean rename(String src0, String dest0) throws IOException {
		Configuration conf = new Configuration();
		FileSystem hdfs = FileSystem.get(URI.create(dest0), conf);

		Path frpaht = new Path(src0); // 旧的文件名
		Path topath = new Path(dest0); // 新的文件名

		boolean isRename = hdfs.rename(frpaht, topath);
		return isRename;
	}

	/**
	 * 判断文件是否存在
	 * 
	 * @param src0
	 * @return
	 * @throws IOException
	 */
	public static boolean exists(String src0) throws IOException {
		Configuration conf = new Configuration();
		FileSystem hdfs = FileSystem.get(URI.create(src0), conf);
		Path findf = new Path(src0);
		boolean isExists = hdfs.exists(findf);
		return isExists;
	}

	/**
	 * 查看HDFS文件的最后修改时间,如果文件不存在则抛出FileNotFoundException
	 * 
	 * @param src0
	 * @return
	 * @throws IOException
	 */
	public static long getLastModify(String src0) throws IOException {
		Configuration conf = new Configuration();
		FileSystem hdfs = FileSystem.get(URI.create(src0), conf);

		Path fpath = new Path(src0);

		FileStatus fileStatus = hdfs.getFileStatus(fpath);
		long modiTime = fileStatus.getModificationTime();
		return modiTime;
	}

	/**
	 * 如果出现 ChecksumException ，删除本地.crc文件即可
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		String src0 = "/home/kongzhidea/share/input";
		String dest0 = "hdfs://localhost:9000/user/kongzhidea/test/input";
		String dest1 = "hdfs://localhost:9000/user/kongzhidea/test2";
		String dest2 = "hdfs://localhost:9000/user/hadoop/test";
		// uploadLocalFile2HDFS(src0, dest0);

		// createNewHDFSFile(dest0, "test\n");

		// System.out.println(deleteHDFSFile(dest0));

		// System.out.println(new String(readHDFSFile(dest0), "utf-8"));

		// deleteHDFSFile(dest1);
		// mkdir(dest1);

		// listAll(dest2);

		// mkdir(dest1);
		// rename(dest1, dest1 + "_tmp");

		// System.out.println(exists(dest0));

//		System.out.println(getLastModify(dest1));
	}
}
