package com.kk.hadoop;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

/**
 * 建立倒排索引
 * 
 * 执行顺序 mapper-partition-combine-reduce
 * 
 * @author kk
 * 
 */
public class InvertedIndex {

	public static class Map extends Mapper<Object, Text, Text, Text> {

		private Text keyInfo = new Text(); // 存储单词和URL组合
		private Text valueInfo = new Text(); // 存储词频
		private FileSplit split; // 存储Split对象

		// 实现map函数
		public void map(Object key, Text value, Context context)
				throws IOException, InterruptedException {
			split = (FileSplit) context.getInputSplit();
			StringTokenizer itr = new StringTokenizer(value.toString());

			while (itr.hasMoreTokens()) {
				// key值由单词和URL组成，如"MapReduce：file1.txt"
				String filename = split.getPath().getName();
				keyInfo.set(itr.nextToken() + ":" + filename);

				// 词频初始化为1
				valueInfo.set("1");
				context.write(keyInfo, valueInfo);
			}
		}
	}

	/**
	 * Combine, mapper函数之后运行
	 * 
	 * @author kk
	 * 
	 */
	public static class Combine extends Reducer<Text, Text, Text, Text> {
		private Text info = new Text();

		public void reduce(Text key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {
			System.out.println("Combine:" + key.toString());
			// 统计词频
			int sum = 0;
			for (Text value : values) {
				sum += Integer.parseInt(value.toString());
			}
			String[] conts = StringUtils.split(key.toString(), ":");
			// 重新设置value值由URL和词频组成
			info.set(conts[1] + ":" + sum);
			// 重新设置key值为单词
			key.set(conts[0]);
			context.write(key, info);
		}
	}

	public static class Reduce extends Reducer<Text, Text, Text, Text> {
		private Text result = new Text();

		// 实现reduce函数
		public void reduce(Text key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {
			System.out.println("Reduce:" + key.toString());

			// 生成文档列表
			StringBuilder sb = new StringBuilder();
			for (Text value : values) {
				sb.append(value.toString() + ";");
			}
			result.set(sb.toString());
			context.write(key, result);
		}
	}

	/**
	 * 设置combine，否则分发的时候按照key:file来分发，当有多个reduce的时候会出现问题
	 * 
	 * @author kk
	 * 
	 */
	public static class Partion extends Partitioner<Text, Text> {

		@Override
		public int getPartition(Text key, Text value, int numPartitions) {
			// Partion:5..MapReduce:inver2..1
			System.out.println("Partion:" + numPartitions + ".."
					+ key.toString() + ".." + value.toString());
			String ikey = StringUtils.split(key.toString(), ":")[0];
			int p = ikey.hashCode() % numPartitions;
			return Math.abs(p);
		}

	}

	public static void main(String[] args) throws Exception {

		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args)
				.getRemainingArgs();
		if (otherArgs.length != 2) {
			System.err.println("Usage: <inpath>  <out>");
			System.exit(1);
		}

		Job job = new Job(conf, InvertedIndex.class.getName());

		job.setJarByClass(InvertedIndex.class);

		// 设置Map、Combine和Reduce处理类
		job.setMapperClass(Map.class);
		job.setPartitionerClass(Partion.class);
		job.setCombinerClass(Combine.class);
		job.setReducerClass(Reduce.class);

		// 设置Map输出类型
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);

		// 设置Reduce输出类型
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		// 设置输入和输出 目录
		FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));

		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}

// 1）file1：
// MapReduce is simple
// 2）file2：
// MapReduce is powerful is simple
// 3）file3：
// Hello MapReduce bye MapReduce
//
// 样例输出如下所示。
// MapReduce file1.txt:1;file2.txt:1;file3.txt:2;
// is 　　　　file1.txt:1;file2.txt:2;
// simple 　 file1.txt:1;file2.txt:1;
// powerful 　　 file2.txt:1;
// Hello 　　 file3.txt:1;
// bye 　　 file3.txt:1;