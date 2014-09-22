package com.kk.hadoop;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import com.kk.hadoop.util.HeapMin;

/**
 * 利用MapReduce求前K大数(最小堆)
 * 
 * @author kk
 * 
 */
public class TopKNum extends Configured implements Tool {

	public static class MapClass extends
			Mapper<LongWritable, Text, IntWritable, IntWritable> {
		public static int K;
		HeapMin heap = null;

		@Override
		protected void setup(Context context) throws IOException,
				InterruptedException {
			Configuration mapconf = context.getConfiguration();
			K = mapconf.getInt("K", K); // map读取值
			heap = new HeapMin(K);
		}

		public void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			if (StringUtils.isBlank(value.toString())) {
				return;
			}
			add(Integer.valueOf(value.toString()));
		}

		private void add(int temp) {// 实现插入
			if (heap.size() < K) {
				heap.insert(temp);
			} else {
				if (temp > heap.top()) {
					heap.del();
					heap.insert(temp);
				}
			}
		}

		@Override
		protected void cleanup(Context context) throws IOException,
				InterruptedException {
			while (heap.size() > 0) {
				context.write(new IntWritable(heap.top()),
						new IntWritable(heap.top()));
				heap.del();
			}
		}
	}

	public static class MyReduce extends
			Reducer<IntWritable, IntWritable, IntWritable, IntWritable> {
		public static int K;
		HeapMin heap = null;

		@Override
		protected void setup(Context context) throws IOException,
				InterruptedException {
			Configuration mapconf = context.getConfiguration();
			K = mapconf.getInt("K", K); // 读取值
			heap = new HeapMin(K);
		}

		public void reduce(IntWritable key, Iterable<IntWritable> values,
				Context context) throws IOException, InterruptedException {
			for (IntWritable val : values) {
				add(val.get());
			}
		}

		private void add(int temp) {
			if (heap.size() < K) {
				heap.insert(temp);
			} else {
				if (temp > heap.top()) {
					heap.del();
					heap.insert(temp);
				}
			}
		}

		@Override
		protected void cleanup(Context context) throws IOException,
				InterruptedException {
			while (heap.size() > 0) {
				context.write(new IntWritable(heap.top()),
						new IntWritable(heap.top()));
				heap.del();
			}
		}
	}

	public int run(String[] args) throws Exception {
		Configuration conf = getConf();

		conf.setInt("K", Integer.valueOf(args[2]));// job初始化之前

		Job job = new Job(conf, "TopKNum");
		job.setJarByClass(TopKNum.class);

		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		job.setMapperClass(MapClass.class);
		job.setReducerClass(MyReduce.class);

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		job.setOutputKeyClass(IntWritable.class);
		job.setOutputValueClass(IntWritable.class);
		System.exit(job.waitForCompletion(true) ? 0 : 1);
		return 0;
	}

	public static void main(String[] args) throws Exception {
		if (args.length != 3) {
			System.err.println("Usage: <in> <out> K");
			System.exit(2);
		}
		int res = ToolRunner.run(new TopKNum(), args);
		System.exit(res);
	}
}