/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kk.hadoop;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.RawComparator;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.util.GenericOptionsParser;

/**
 * 每行2个数字，先排left，当left相同时候排第二个
 * 
 * 需要自定义结构，并设置compare和equals函数,设置分区函数,按照left来分区
 * 
 * 本例中不需要设置left相同的放到一个reduce的key中，
 * 如果需要还需要自定义setGroupingComparatorClass(按照left来聚合
 * )和setSortComparatorClass(values中的排序规则)
 * 
 * @author kk
 * 
 */
public class SecondarySort {

	/**
	 * Define a pair of integers that are writable. They are serialized in a
	 * byte comparable format.
	 */
	public static class IntPair implements WritableComparable<IntPair> {
		private int first = 0;
		private int second = 0;

		/**
		 * Set the left and right values.
		 */
		public void set(int left, int right) {
			first = left;
			second = right;
		}

		public int getFirst() {
			return first;
		}

		public int getSecond() {
			return second;
		}

		public void readFields(DataInput in) throws IOException {
			first = in.readInt();
			second = in.readInt();
		}

		public void write(DataOutput out) throws IOException {
			out.writeInt(first);
			out.writeInt(second);
		}

		@Override
		public int hashCode() {
			return first * 157 + second;
		}

		@Override
		public boolean equals(Object right) {
			if (right instanceof IntPair) {
				IntPair r = (IntPair) right;
				return r.first == first && r.second == second;
			} else {
				return false;
			}
		}

		/**
		 * mapper中key的排序
		 */
		public int compareTo(IntPair o) {
			if (first != o.first) {
				return first < o.first ? -1 : 1;
			} else if (second != o.second) {
				return second < o.second ? -1 : 1;
			} else {
				return 0;
			}
		}
	}

	/**
	 * Partition based on the first part of the pair.
	 */
	public static class FirstPartitioner extends
			Partitioner<IntPair, IntWritable> {
		@Override
		public int getPartition(IntPair key, IntWritable value,
				int numPartitions) {
			int max = 800;
			int bound = max / numPartitions + 1;
			int keyNum = key.getFirst();
			for (int i = 0; i < numPartitions; i++) {
				if (keyNum < bound * (i + 1) && keyNum > bound * i) {
					return i;
				}
			}
			return -1;
		}
	}

	/**
	 * Read two integers from each line and generate a key, value pair as
	 * ((left, right), right).
	 */
	public static class MapClass extends
			Mapper<LongWritable, Text, IntPair, IntWritable> {

		private final IntPair key = new IntPair();
		private final IntWritable value = new IntWritable();

		@Override
		public void map(LongWritable inKey, Text inValue, Context context)
				throws IOException, InterruptedException {
			StringTokenizer itr = new StringTokenizer(inValue.toString());
			int left = 0;
			int right = 0;
			if (itr.hasMoreTokens()) {
				left = Integer.parseInt(itr.nextToken());
				if (itr.hasMoreTokens()) {
					right = Integer.parseInt(itr.nextToken());
				}
				key.set(left, right);
				value.set(right);
				context.write(key, value);
			}
		}
	}

	/**
	 * A reducer class that just emits the sum of the input values.
	 */
	public static class Reduce extends
			Reducer<IntPair, IntWritable, Text, IntWritable> {
		private static final Text SEPARATOR = new Text(
				"------------------------------------------------");
		private final Text first = new Text();

		@Override
		public void reduce(IntPair key, Iterable<IntWritable> values,
				Context context) throws IOException, InterruptedException {
			context.write(SEPARATOR, null);
			first.set(Integer.toString(key.getFirst()));
			for (IntWritable value : values) {
				context.write(first, value);
			}
		}
	}

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args)
				.getRemainingArgs();
		if (otherArgs.length != 2) {
			System.err.println("Usage: secondarysrot <in> <out>");
			System.exit(2);
		}
		Job job = new Job(conf, "secondary sort");
		job.setJarByClass(SecondarySort.class);
		job.setMapperClass(MapClass.class);
		job.setReducerClass(Reduce.class);

		job.setNumReduceTasks(2);

		// group and partition by the first int in the pair
		job.setPartitionerClass(FirstPartitioner.class);

		// the map output is IntPair, IntWritable
		job.setMapOutputKeyClass(IntPair.class);
		job.setMapOutputValueClass(IntWritable.class);

		// the reduce output is Text, IntWritable
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}

}

// 输入
// 4 45
// 23 452
// 12 334
// 541 23
// 56 242
// 1 4
// 5 63
// 345 35
// 1 3266
// 6 7
// 6 7
// 223 16
// 17 536
// 763 4
// 5 6
// 7 8
// 7 8

// 输出
// ------------------------------------------------
// 1 4
// ------------------------------------------------
// 1 3266
// ------------------------------------------------
// 4 45
// ------------------------------------------------
// 5 6
// ------------------------------------------------
// 5 63
// ------------------------------------------------
// 6 7
// 6 7
// ------------------------------------------------
// 7 8
// 7 8
// ------------------------------------------------
// 12 334
// ------------------------------------------------
// 17 536
// ------------------------------------------------
// 23 452
// ------------------------------------------------
// 56 242
// ------------------------------------------------
// 223 16
// ------------------------------------------------
// 345 35
// ------------------------------------------------
// 541 23
// ------------------------------------------------
// 763 4
