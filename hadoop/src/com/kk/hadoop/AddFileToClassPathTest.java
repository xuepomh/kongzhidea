package com.kk.hadoop;

import java.io.IOException;
import java.net.URI;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

//import redis.clients.jedis.Jedis;

/**
 * DistributedCache.addFileToClassPath() 将第三方jar包加到classpath中
 * 
 * @author kk
 * 
 */
public class AddFileToClassPathTest {
	// public static class MyMapper extends Mapper<Text, Text, Text, Text> {
	// private static Jedis jedis;
	// private static final String host = "10.3.20.160";
	// private static final int port = 6379;
	// private static final String password = "whoareyou";
	//
	// @Override
	// protected void setup(Context context) throws IOException,
	// InterruptedException {
	// jedis = new Jedis(host, port);
	// jedis.auth(password);
	// }
	//
	// @Override
	// protected void map(Text key, Text value, Context context)
	// throws IOException, InterruptedException {
	// if (StringUtils.isBlank(value.toString())) {
	// return;
	// }
	// String val = jedis.get(value.toString());
	// context.write(new Text(value.toString()), new Text(val));
	// }
	// }
	//
	// public static void main(String[] args) throws IOException,
	// InterruptedException, ClassNotFoundException {
	// Configuration conf = new Configuration();
	// String[] otherArgs = new GenericOptionsParser(conf, args)
	// .getRemainingArgs();
	// if (otherArgs.length != 2) {
	// System.err.println("Usage: <infile> <out>");
	// System.exit(1);
	// }
	// FileSystem fs = FileSystem.get(URI.create(otherArgs[0]), conf);
	//
	// // important!! 将第三方jar包加到classpath中
	// DistributedCache.addFileToClassPath(new Path(
	// "/user/lib/jedis-2.0.0.jar"), conf, fs);
	// Job job = new Job(conf, AddFileToClassPathTest.class.getName());
	//
	// job.setJarByClass(AddFileToClassPathTest.class);
	//
	// // 只设置mapper
	// job.setMapperClass(MyMapper.class);
	//
	// job.setOutputKeyClass(Text.class);
	// job.setOutputValueClass(Text.class);//
	//
	// FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
	// FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
	// System.exit(job.waitForCompletion(true) ? 0 : 1);
	// }
}
