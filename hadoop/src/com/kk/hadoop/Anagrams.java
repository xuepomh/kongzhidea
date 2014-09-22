package com.kk.hadoop;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * 寻找变位词集合
 * 
 * 给定一本英语单词词典，请找出所有的变位词集。所谓的变位词是指，组成各个单词的字母完全相同，只是字母排列的顺序不同。
 * 
 * 输入:pans pots opt snap stop tops
 * 
 * 输出:[pans,snap] [opt] [pots,stop,tops]
 * 
 * @see http://www.cnblogs.com/biyeymyhjob/archive/2012/08/14/2636962.html
 * @author kk
 * 
 */
public class Anagrams  extends Configured implements Tool{

	public static class MyMapper extends Mapper<LongWritable, Text, Text, Text> {

		@Override
		protected void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			StringTokenizer itr = new StringTokenizer(value.toString());
			while (itr.hasMoreTokens()) {
				String cont = itr.nextToken();
				char a[] = cont.toCharArray();
				Arrays.sort(a);
				String outputKey = String.valueOf(a);
				context.write(new Text(outputKey), new Text(cont));
			}

		}

	}

	public static class MyReducer extends Reducer<Text, Text, Text, Text> {

		protected void reduce(Text key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {
			Iterator<Text> iter = values.iterator();
			HashSet<String> set = new HashSet<String>();
			while (iter.hasNext()) {
				set.add(iter.next().toString());
			}
			context.write(key, new Text(set.toString()));
		}

	}

	public int run(String[] args) throws Exception {
		Job job = new Job();

		job.setJobName(Anagrams.class.getName());
		job.setJarByClass(Anagrams.class);

		job.setMapperClass(MyMapper.class);
		job.setReducerClass(MyReducer.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		boolean ret = job.waitForCompletion(true);
		return ret ? 0 : 1;
	}

	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			System.err.println("Usage: <in>  <out>");
			System.exit(1);
		}

		// 通过toolrunner启动
		int ret = ToolRunner.run(new Anagrams(), args);
		System.exit(ret);
	}

}
