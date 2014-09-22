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
 * 输出文件 每行两列，数字的排名(从1开始) 数字本身
 * 
 * 要保证每个reduce中的数字的key是连续的，需要自定义分片规则，将同在一个区间段的数字都放到一个reducer中
 * 输出:有个reduce就输出几个文件，再对文件做处理即可
 * 
 * @author kk
 * 
 */
public class NumberSort {
	public static class MyMapper extends
			Mapper<Object, Text, IntWritable, IntWritable> {

		private static IntWritable num = new IntWritable();
		private static IntWritable ONE = new IntWritable(1);

		@Override
		protected void map(Object key, Text value, Context context)
				throws IOException, InterruptedException {
			System.out.println("mapper:" + key.toString() + ".."
					+ value.toString());
			num.set(NumberUtils.toInt(value.toString()));
			context.write(num, ONE);
		}

	}

	public static class MyReducer extends
			Reducer<IntWritable, IntWritable, IntWritable, IntWritable> {

		public static IntWritable linenum = new IntWritable(1);

		protected void reduce(IntWritable key, Iterable<IntWritable> values,
				Context context) throws IOException, InterruptedException {
			System.out.println("reducer:" + key.toString());
			for (IntWritable value : values) {
				context.write(linenum, key);
				linenum.set(linenum.get() + 1);
			}
		}

	}

	/**
	 * 自定义partition函数; Map的结果，会通过partition分发到Reducer上 分片从0开始。
	 * 
	 * 输入是Map的结果对<key,
	 * value>和Reducer的数目，输出则是分配的Reducer（整数编号）。就是指定Mappr输出的键值对到哪一个reducer上去
	 * 。系统缺省的Partitioner是HashPartitioner
	 * ，它以key的Hash值对Reducer的数目取模，得到对应的Reducer。这样保证如果有相同的key值
	 * ，肯定被分配到同一个reducre上。如果有N个reducer，编号就为0,1,2,3……(N-1)。
	 * 
	 * @author kk
	 * 
	 */
	public static class Partion extends Partitioner<IntWritable, IntWritable> {

		@Override
		public int getPartition(IntWritable key, IntWritable value,
				int numPartitions) {
			int max = 65536;
			int bound = max / numPartitions + 1;
			int keyNum = key.get();
			for (int i = 0; i < numPartitions; i++) {
				if (keyNum < bound * (i + 1) && keyNum > bound * i) {
					return i;
				}
			}
			return -1;
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
		Job job = new Job(conf, NumberSort.class.getName());

		// 设置reducer数量 可以在hadoop jar *.jar className -Dmapred.reduce.tasks=5
		// 这样来指定reduce数量
		job.setNumReduceTasks(3);

		job.setJarByClass(NumberSort.class);

		// 这里不设置combine函数
		job.setMapperClass(MyMapper.class);

		// 设置分片规则
		job.setPartitionerClass(Partion.class);

		job.setReducerClass(MyReducer.class);

		job.setOutputKeyClass(IntWritable.class);
		job.setOutputValueClass(IntWritable.class);//

		FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
// 输入
// 14
// 56
// 67
// 78
// 563
// 7634
// 12334
// 13266
// 17536
// 输出
// 1 4
// 2 14
// 3 56
// 4 67
// 5 78
// 6 563
// 7 7634
// 8 12334
// 9 13266
// 10 17536