package com.renren.shopping.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * key 构造器
 * 
 * @author xiong.hua
 */
public class KeysGenerator {

    public static int sufix = 10000;

    private static KeysGenerator instance = new KeysGenerator();

    private static SimpleDateFormat FORMAT = new SimpleDateFormat("yyMMddHHmmssSSS");

    private static Random RANDOM = new Random(System.currentTimeMillis());

    public static KeysGenerator getInstance() {
        return instance;
    }

    public long generatorId() {
        long gid = 0;
        String prefix = FORMAT.format(new Date());
        gid = Long.parseLong(prefix) * sufix + RANDOM.nextInt(sufix);
        return gid;
    }

}
