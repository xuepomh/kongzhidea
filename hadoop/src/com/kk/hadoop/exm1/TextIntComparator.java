package com.kk.hadoop.exm1;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

//分组内部进行排序，按照第二个字段进行排序 
public class TextIntComparator extends WritableComparator {
	public TextIntComparator() {
		super(TextInt.class, true);
	}

	/**
	 * 组合排序 (t1-t2：从小到大)
	 */
	public int compare(WritableComparable a, WritableComparable b) {
		TextInt ti1 = (TextInt) a;
		TextInt ti2 = (TextInt) b;
		// 首先要保证是同一个组内，同一个组的标识就是第一个字段相同
		if (!ti1.getFirstKey().equals(ti2.getFirstKey()))
			return ti1.getFirstKey().compareTo(ti2.getFirstKey());
		else
			return ti1.getSecondKey() - ti2.getSecondKey();
	}

}