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
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

/**
 * 多表关联
 * 
 * 与单表关联类似，但是分成了两张表,求左表和右表的inner join
 * 
 * @author kk
 * 
 */
public class MTJoin {
	public static final int ORI = 0;// 源数据的sign
	public static final int CHR = 1;// 变换后的数据的sign

	public static final String DELIM = ":|:";

	/**
	 * 两个输入文件，两个mapper
	 * 
	 * @author kk
	 * 
	 */
	public static class MyMapper extends Mapper<Object, Text, Text, Text> {

		@Override
		protected void map(Object key, Text value, Context context)
				throws IOException, InterruptedException {
			if (StringUtils.isBlank(value.toString())) {
				return;
			}
			String[] conts = StringUtils.split(value.toString().trim());

			// log
			FileSplit split = (FileSplit) context.getInputSplit();
			// splitFile:hdfs://localhost:9000/user/hadoop/test/file1...file1
			System.out.println("splitFile:" + split.getPath().toString()
					+ "..." + split.getPath().getName());

			System.out.println(ORI + DELIM + conts[0] + DELIM + conts[1]);
			System.out.println(CHR + DELIM + conts[0] + DELIM + conts[1]);

			// 源数据
			if (split.getPath().getName().equals("file2")) {
				context.write(new Text(conts[0]), new Text(ORI + DELIM
						+ conts[0] + DELIM + conts[1]));
			}
			
			if (split.getPath().getName().equals("file1")) {
				// 变换后的数据,通过SIGN来标识
				context.write(new Text(conts[1]), new Text(CHR + DELIM
						+ conts[0] + DELIM + conts[1]));
			}
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
			System.err.println("Usage: <inpath>  <out>");
			System.exit(1);
		}
		Job job = new Job(conf, MTJoin.class.getName());

		job.setJarByClass(MTJoin.class);

		// 不设置combine函数
		job.setMapperClass(MyMapper.class);

		job.setReducerClass(MyReducer.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);//

		// 可以添加文件，也可以添加目录(自动加载目录下所有文件),也可以使用*通配符
		FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
// 输入
// file1
// a b
// a c
// file2
// c d
// c e
// 输出
// a d
// a e