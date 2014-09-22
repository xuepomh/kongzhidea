package com.kk.hadoop.exm1;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * 输入文件格式如下：
 * 
 * name1 2
 * 
 * name3 4
 * 
 * name1 6
 * 
 * name1 1
 * 
 * name3 3
 * 
 * name1 0
 * 
 * 要求输出的文件格式如下：
 * 
 * name1 0，1，2，6
 * 
 * name3 3，4
 * 
 * 要求是按照第一列分组，name1与name3也是按照顺序排列的，组内升序排序。
 * 
 * 
 * 使用组合Key，需要注意4点:自定义类型实现WritableComparable接口，定义分区规则，定义分组规则，定义组内排序规则
 * 
 * @author kk
 * 
 * 
 * 
 */
public class SortMain extends Configured implements Tool {
	// 这里设置输入文格式为KeyValueTextInputFormat
	// name1 5
	// 默认输入格式都是Text，Text
	public static class GroupMapper extends
			Mapper<Text, Text, TextInt, IntWritable> {
		public IntWritable second = new IntWritable();
		public TextInt tx = new TextInt();

		@Override
		protected void map(Text key, Text value, Context context)
				throws IOException, InterruptedException {
			String lineKey = key.toString();
			String lineValue = value.toString();
			int lineInt = Integer.parseInt(lineValue);
			tx.setFirstKey(lineKey);
			tx.setSecondKey(lineInt);
			second.set(lineInt);
			context.write(tx, second);
		}
	}

	// 设置reduce
	public static class GroupReduce extends
			Reducer<TextInt, IntWritable, Text, Text> {
		@Override
		protected void reduce(TextInt key, Iterable<IntWritable> values,
				Context context) throws IOException, InterruptedException {
			StringBuffer sb = new StringBuffer();
			for (IntWritable val : values) {
				sb.append(val + ",");
			}
			if (sb.length() > 0) {
				sb.deleteCharAt(sb.length() - 1);
			}
			context.write(new Text(key.getFirstKey()), new Text(sb.toString()));
		}
	}

	public int run(String[] args) throws Exception {
		Configuration conf = getConf();
		Job job = new Job(conf, "SecondarySort");
		job.setJarByClass(SortMain.class);
		// 设置输入文件的路径，已经上传在HDFS
		FileInputFormat.addInputPath(job, new Path(args[0]));
		// 设置输出文件的路径，输出文件也存在HDFS中，但是输出目录不能已经存在
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		job.setMapperClass(GroupMapper.class);
		job.setReducerClass(GroupReduce.class);
		// 设置分区方法
		job.setPartitionerClass(KeyPartitioner.class);

		// 下面这两个都是针对map端的 以下两个排序都是基于key的
		/** 设置分组的策略，哪些key可以放置到一组中 important */
		job.setGroupingComparatorClass(TextComparator.class);
		// 设置key如何进行排序在传递给reducer之前.
		/************* 设置对组内如何排序 important **********/
		job.setSortComparatorClass(TextIntComparator.class);

		// 设置输入文件格式 mapper中的输入按照默认tab隔开分成key和value
		job.setInputFormatClass(KeyValueTextInputFormat.class);

		// 设置map的输出key和value类型 important
		job.setMapOutputKeyClass(TextInt.class);
		job.setMapOutputValueClass(IntWritable.class);

		// 设置reduce的输出key和value类型
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		job.waitForCompletion(true);
		int exitCode = job.isSuccessful() ? 0 : 1;
		return exitCode;
	}

	public static void main(String[] args) throws Exception {
		int exitCode = ToolRunner.run(new SortMain(), args);
		System.exit(exitCode);
	}
}