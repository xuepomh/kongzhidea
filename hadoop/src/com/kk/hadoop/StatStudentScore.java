package com.kk.hadoop;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * 求每个学生的平均成绩
 * 
 * 输入文件为每行:name score
 * 
 * Configured 已经实现了setConf和getConf方法
 * 
 * hadoop采用默认的作业输入方式，每次读取一行，key为行首在文件中的偏移量，value为行字符串内容 然后mapper将value(每行内容)
 * 按照\n分割(按行读取),每行再分为name,score，mapper将key设置为name,value设置为score，然后交给reducer；
 * reducer按照mapper的输出key和输出value，进行统计每个人的平均分，再输出到文件
 * 
 * @author kk
 * 
 */
public class StatStudentScore extends Configured implements Tool {

	public static class MyMapper extends
			Mapper<LongWritable, Text, Text, IntWritable> {

		@Override
		protected void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			System.out.println("mapper:" + key.toString() + ".."
					+ value.toString());// 打log，可以在hadoop平台的web服务上访问/complete/task/tasklogs

			String line = value.toString();
			if (StringUtils.isBlank(line)) {
				return;
			}
			String[] conts = StringUtils.split(line);
			Text name = new Text(conts[0]);
			int score = NumberUtils.toInt(conts[1]);
			context.write(name, new IntWritable(score));
		}

	}

	public static class MyReducer extends
			Reducer<Text, IntWritable, Text, IntWritable> {

		protected void reduce(Text key, Iterable<IntWritable> values,
				Context context) throws IOException, InterruptedException {
			int sum = 0;
			int count = 0;
			for (IntWritable val : values) {
				count++;
				sum += val.get();
			}
			context.write(key, new IntWritable(sum / count));
		}

	}

	public int run(String[] args) throws Exception {
		Job job = new Job();

		job.setJobName(StatStudentScore.class.getName());
		job.setJarByClass(StatStudentScore.class);

		job.setMapperClass(MyMapper.class);
		job.setCombinerClass(MyReducer.class);
		job.setReducerClass(MyReducer.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		boolean ret = job.waitForCompletion(true);
		return ret ? 0 : 1;
	}

	public static void main(String[] args) throws Exception {
		// 通过toolrunner启动
		int ret = ToolRunner.run(new StatStudentScore(), args);
		System.exit(ret);
	}
}
// 以下为输入样例
// kongzhihui 2
// kongzhihui 5
// kongzhihui 1
// kongzhihui 1
// kongzhihui 1
//
// xupo 7
// xupo 1
// xupo 1
// xupo 4
// xupo 1
//
// xienan 2
// xienan 1
// xienan 9

