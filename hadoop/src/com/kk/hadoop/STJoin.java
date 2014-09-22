package com.kk.hadoop;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

/**
 * 单表关联  inner join
 * 
 * 输入每行两列，child,parent， 输出child,grantParent
 * 
 * 解法：将 parent作为切入点，再建立一张表，parent child， 添加到元数据中，key为第一列，value为 sign+行内容
 * reduce中求笛卡尔乘积
 * 
 * @author kk
 * 
 */
public class STJoin {
	public static final int ORI = 0;// 源数据的sign
	public static final int CHR = 1;// 变换后的数据的sign

	public static final String DELIM = ":|:";

	public static class MyMapper extends Mapper<Object, Text, Text, Text> {

		@Override
		protected void map(Object key, Text value, Context context)
				throws IOException, InterruptedException {
			if (StringUtils.isBlank(value.toString())) {
				return;
			}
			String[] conts = StringUtils.split(value.toString().trim());

			// log
			System.out.println(ORI + DELIM + conts[0] + DELIM + conts[1]);
			System.out.println(CHR + DELIM + conts[0] + DELIM + conts[1]);

			// 源数据
			context.write(new Text(conts[0]), new Text(ORI + DELIM + conts[0]
					+ DELIM + conts[1]));
			// 变换后的数据,通过SIGN来标识
			context.write(new Text(conts[1]), new Text(CHR + DELIM + conts[0]
					+ DELIM + conts[1]));
		}
	}

	public static class MyReducer extends Reducer<Text, Text, Text, Text> {

		protected void reduce(Text key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {
			List<String> childs = new ArrayList<String>();
			List<String> grants = new ArrayList<String>();
			Iterator<Text> it = values.iterator();
			while (it.hasNext()) {
				String line = it.next().toString();
				// log
				System.out.println(line);

				String[] conts = StringUtils.split(line.trim(), DELIM);
				if (NumberUtils.toInt(conts[0]) == 0) {// 源数据,key为child
					grants.add(conts[2]);// grant parent
				} else {// 变换后的数据，key为parent
					childs.add(conts[1]);// children
				}
			}
			// 求 笛卡尔乘积，child grantParent
			for (String child : childs) {
				for (String grant : grants) {
					context.write(new Text(child), new Text(grant));
				}
			}
		}

	}

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args)
				.getRemainingArgs();
		if (otherArgs.length != 2) {
			System.err.println("Usage: <in> <out>");
			System.exit(1);
		}
		Job job = new Job(conf, STJoin.class.getName());

		job.setJarByClass(STJoin.class);

		//不设置combine函数
		job.setMapperClass(MyMapper.class);

		job.setReducerClass(MyReducer.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);//

		FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
// 输入
// a b
// a c
// c d
// c e
// 输出
// a d
// a e