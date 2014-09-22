package com.kk.hadoop;

import java.io.IOException;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

/**
 * 输入文件 每行一个数字
 * 
 * 输出文件:最大值
 * 
 * @author kk
 * 
 */
public class MaxNumber {
	public static class MyMapper extends
			Mapper<Object, Text, Text, IntWritable> {

		private static final Text tKey = new Text("line");

		@Override
		protected void map(Object key, Text value, Context context)
				throws IOException, InterruptedException {
			System.out.println("mapper:" + value.toString());
			int val = Integer.valueOf(value.toString());
			context.write(tKey, new IntWritable(val));
		}

	}

	public static class MyConbine extends
			Reducer<Text, IntWritable, Text, IntWritable> {
		private static final Text tKey = new Text("line");

		protected void reduce(Text key, Iterable<IntWritable> values,
				Context context) throws IOException, InterruptedException {
			System.out.println("combine");
			int max = Integer.MIN_VALUE;
			for (IntWritable val : values) {
				max = Math.max(max, val.get());
			}
			context.write(tKey, new IntWritable(max));
		}

	}

	public static class MyReducer extends
			Reducer<Text, IntWritable, Text, IntWritable> {

		protected void reduce(Text key, Iterable<IntWritable> values,
				Context context) throws IOException, InterruptedException {
			System.out.println("recuder");
			int max = Integer.MIN_VALUE;
			for (IntWritable val : values) {
				max = Math.max(max, val.get());
			}
			context.write(new Text(), new IntWritable(max));
		}

	}

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args)
				.getRemainingArgs();
		if (otherArgs.length != 2) {
			System.err.println("Usage: <in> <out>");
			System.exit(2);
		}
		Job job = new Job(conf, MaxNumber.class.getName());

		job.setJarByClass(MaxNumber.class);

		job.setMapperClass(MyMapper.class);
		job.setCombinerClass(MyConbine.class);
		job.setReducerClass(MyReducer.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);//

		FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
