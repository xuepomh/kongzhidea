package com.kk.hadoop.exm1;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Partitioner;

/**
 * 分区策略
 * 
 * @author kk
 * 
 */
public class KeyPartitioner extends Partitioner<TextInt, IntWritable> {
	@Override
	public int getPartition(TextInt key, IntWritable value, int numPartitions) {
		return (key.getFirstKey().hashCode() & Integer.MAX_VALUE)
				% numPartitions;
	}
}