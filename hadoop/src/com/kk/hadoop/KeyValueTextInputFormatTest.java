package com.kk.hadoop;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

/**
 * 修改hadoop默认数据方式
 * 
 * TextInputFormat是默认的InputFormat，一行为一个record，key是该行在文件中的偏移量，value是该行的内容。
 * 
 * 设置输入文件格式KeyValueTextInputFormat,默认按照\t分割
 * 
 * @author kk
 * 
 */
public class KeyValueTextInputFormatTest {
	public static class MyMapper extends Mapper<Text, Text, Text, Text> {

		@Override
		protected void map(Text key, Text value, Context context)
				throws IOException, InterruptedException {
			if (StringUtils.isBlank(value.toString())) {
				return;
			}
			System.out.println(key.toString() + ".." + value.toString());
			context.write(new Text(key), new Text(value));
		}
	}

	public static void main(String[] args) throws IOException,
			InterruptedException, ClassNotFoundException {
		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args)
				.getRemainingArgs();
		if (otherArgs.length != 2) {
			System.err.println("Usage: <infile> <out>");
			System.exit(1);
		}
		// KeyValueTextInputFormat中 下面配置将 分隔符改成逗号,放到job初始化之前
		// 如果某行中 没有分隔符，则mapper会忽略该行
		// 如果有多个分隔符，则使用第一个分隔符
		conf.set(
				"mapreduce.input.keyvaluelinerecordreader.key.value.separator",
				",");

		Job job = new Job(conf, KeyValueTextInputFormatTest.class.getName());

		job.setJarByClass(KeyValueTextInputFormatTest.class);

		// TextInputFormat是默认的InputFormat，一行为一个record，key是该行在文件中的偏移量，value是该行的内容。
		// 设置输入文件格式KeyValueTextInputFormat,默认按照\t分割
		job.setInputFormatClass(KeyValueTextInputFormat.class);
		// 只设置mapper
		job.setMapperClass(MyMapper.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);//

		FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
