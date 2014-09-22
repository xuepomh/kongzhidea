package com.kk.hadoop;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.net.URI;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.db.DBConfiguration;
import org.apache.hadoop.mapreduce.lib.db.DBInputFormat;
import org.apache.hadoop.mapreduce.lib.db.DBWritable;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

/**
 * 读数据库，写入到文件
 * 
 * @author kk
 * 
 */
public class ConnMysql {

	public static class TblsRecord implements Writable, DBWritable {
		int id;
		String name;
		String sex;
		int age;

		public TblsRecord() {

		}

		public void write(PreparedStatement statement) throws SQLException {
			System.out.println("write statement");
			statement.setInt(1, this.id);
			statement.setString(2, this.name);
			statement.setString(3, this.sex);
			statement.setInt(4, this.age);
		}

		public void readFields(ResultSet resultSet) throws SQLException {
			System.out.println("readFields resultSet");// this 读数据库中数据
			this.id = resultSet.getInt(1);
			this.name = resultSet.getString(2);
			this.sex = resultSet.getString(3);
			this.age = resultSet.getInt(4);
		}

		//序列化和反序列化使用
		public void write(DataOutput out) throws IOException {
			System.out.println("write DataOutput");
			Text.writeString(out, this.id + "");
			Text.writeString(out, this.name);
			Text.writeString(out, this.sex);
			Text.writeString(out, this.age + "");
		}

		public void readFields(DataInput in) throws IOException {
			System.out.println("readFields DataInput");
			this.id = Integer.parseInt(Text.readString(in));
			this.name = Text.readString(in);
			this.sex = Text.readString(in);
			this.age = Integer.parseInt(Text.readString(in));
		}

		@Override
		public String toString() {
			return "TblsRecord [id=" + id + ", name=" + name + ", sex=" + sex
					+ ", age=" + age + "]";
		}
	}

	public static class ConnMysqlMapper extends
			Mapper<LongWritable, TblsRecord, Text, Text> {
		public void map(LongWritable key, TblsRecord values, Context context)
				throws IOException, InterruptedException {
			context.write(new Text(values.id + ".." + values.name + ".."
					+ values.sex + ".." + values.age), new Text());
		}
	}

	public static class ConnMysqlReducer extends
			Reducer<Text, Text, Text, Text> {
		public void reduce(Text key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {
			for (Iterator<Text> itr = values.iterator(); itr.hasNext();) {
				context.write(key, itr.next());
			}
		}
	}

	public static void main(String[] args) throws Exception {

		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args)
				.getRemainingArgs();

		// mysql的jdbc驱动
		// DistributedCache
		// .addFileToClassPath(
		// new Path(
		// "hdfs://hd022-test.nh.sdo.com/user/liuxiaowen/mysql-connector-java-5.1.13-bin.jar"),
		// conf);

		DBConfiguration
				.configureDB(
						conf,
						"com.mysql.jdbc.Driver",
						"jdbc:mysql://10.2.45.39:3306/test?useUnicode=true&characterEncoding=utf-8",
						"purchase", "agent");//

		Job job = new Job(conf, ConnMysql.class.getName());
		job.setJarByClass(ConnMysql.class);

		job.setMapperClass(ConnMysqlMapper.class);
		job.setReducerClass(ConnMysqlReducer.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		job.setInputFormatClass(DBInputFormat.class);// 设置数据input类型
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[0]));

		// 列名
		String[] fields = { "id", "name", "sex", "age" };
		// 六个参数分别为：
		// 1.Job;2.Class<? extends DBWritable>
		// 3.表名;4.where条件
		// 5.order by语句;6.列名
		DBInputFormat.setInput(job, TblsRecord.class, "student", "id > 0",
				"id", fields);//

		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}

}