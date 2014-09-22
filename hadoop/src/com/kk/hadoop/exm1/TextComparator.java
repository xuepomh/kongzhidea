package com.kk.hadoop.exm1;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

/**
 * 主要就是对于分组进行排序，分组只按照组建键中的一个值进行分组 即reducer中的Key值按照一个值进行聚合，
 * 
 * @author kk
 * 
 */
public class TextComparator extends WritableComparator {
	// 必须要调用父类的构造器
	protected TextComparator() {
		super(TextInt.class, true);// 注册comparator
	}

	@Override
	public int compare(WritableComparable a, WritableComparable b) {
		TextInt ti1 = (TextInt) a;
		TextInt ti2 = (TextInt) b;
		return ti1.getFirstKey().compareTo(ti2.getFirstKey());
	}
}