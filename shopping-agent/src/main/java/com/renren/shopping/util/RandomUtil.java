package com.renren.shopping.util;

import java.util.Random;

public class RandomUtil {

    /**
     * 根据指定长度生成纯数字的随机数
     * @param length
     * @return
     */
    public static String randomFixedLengthNumeralString(int length) {
        StringBuilder sb = new StringBuilder();
        Random rand = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(rand.nextInt(10));
        }
        return sb.toString();
    }

    /**
     * 大小写字母和数字
     * @param length
     * @return
     */
    public static String randomFixedLengthChars(int length) {
        StringBuilder sb = new StringBuilder();
        Random rand = new Random();// 随机用以下三个随机生成器
        Random randdata = new Random();
        int data = 0;
        for (int i = 0; i < length; i++) {
            int index = rand.nextInt(3);
            // 目的是随机选择生成数字，大小写字母
            switch (index) {
            case 0:
                data = randdata.nextInt(10);// 仅仅会生成0~9
                sb.append(data);
                break;
            case 1:
                data = randdata.nextInt(26) + 65;// 保证只会产生65~90之间的整数
                sb.append((char) data);
                break;
            case 2:
                data = randdata.nextInt(26) + 97;// 保证只会产生97~122之间的整数
                sb.append((char) data);
                break;
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(RandomUtil.randomFixedLengthChars(10));
    }

}
