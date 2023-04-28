package com.gsg.blog.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author shuaigang
 * @date  2021/9/29 15:22
 */
public class PkGenerator {

    public static String getDate() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String dateStr = format.format(date);
        String pk = dateStr ;
        return pk;
    }

    public static String getTime() {
        String time = System.nanoTime()+"";
        String strTime = time.substring(time.length() - 6);
        return strTime;
    }

    public static String getNum(int num){
        String srtNum = "";
        for(int i = 0; i < num; i++){
            srtNum += (int)(10*(Math.random()));
        }
        return srtNum;
    }

    public static String generate() {
        return getDate() + getTime() + getNum(3);
    }


}
