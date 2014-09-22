package com.renren.shopping.util;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.wltea.analyzer.IKSegmentation;
import org.wltea.analyzer.Lexeme;

/**
 * 中文分词通用类
 * 
 * @author kk
 * 
 */
public class IKAnalyseUtil {

	/**
	 * 通用分词
	 * 
	 * @param content
	 * @return
	 */
	public static List<String> match(String content) {
		long beg = System.currentTimeMillis();
		StringReader reader = new StringReader(content);
		IKSegmentation ik = new IKSegmentation(reader, true);// 当为true时，分词器进行最大词长切分
		Lexeme lexeme = null;
		List<String> result = new ArrayList<String>();
		try {
			while ((lexeme = ik.next()) != null) {
				result.add(lexeme.getLexemeText());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static void main(String[] args) {
		System.out.println(match("IK Analyzer是一个结合词典分词和文法分词的中文分词开源工具包"));
	}
}
