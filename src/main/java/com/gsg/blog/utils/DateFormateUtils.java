package com.gsg.blog.utils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @ClassName DateFormateUtils 日期格式化工具类
 * @Description
 * @Author shuaigang
 * @Date 2021/4/20 0020 上午 10:40:004
 * @Version 1.0
 **/
@Data
@Slf4j
public class DateFormateUtils {

    public static final String STANDARD_STAMP = "yyyyMMddHHmmss";
    public static final String STANDARD_STAMP2 = "yyyy-MM-dd HH:mm:ss";
    public static final String STANDARD_STAMP3 = "yyyyMMddHHmmssSSS";
    public static final String STANDARD_STAMP4 = "yyyy年MM月dd日 HH:mm:ss";
    public static final String STANDARD_STAMP5 = "yyyy/MM/dd HH:mm:ss";
    public static final String STANDARD_STAMP6 = "yyyyMMddHH";
    public static final String STANDARD_DATE_MONTH = "yyyyMM";
    public static final String STANDARD_DATE_MONTH2 = "yyyy-MM";
    public static final String STANDARD_DATE_MONTH3 = "yyyy年MM月";
    public static final String STANDARD_DATE_DAY = "yyyyMMdd";
    public static final String STANDARD_DATE_DAY2 = "yyyy-MM-dd";
    public static final String STANDARD_DATE_DAY3 = "yyyy年MM月dd日";
    public static final String STANDARD_DATE_DAY4 = "yyyy/MM/dd";
    public static final String STANDARD_DATE_YEAR = "yyyy";
    public static final String STANDARD_DATE_VF = "2021-01-01T00:00:00";

//    public static void main(String[] args) {
//        Date utcCurrentDate = getUtcCurrentDate();
//        LocalDate utcCurrentLocalDate = getUtcCurrentLocalDate();
//        LocalDateTime utcCurrentLocalDateTime = getUtcCurrentLocalDateTime();
//        String s = formatUtcCurrentDate(STANDARD_STAMP2);
//        System.out.println(utcCurrentDate);
//        System.out.println(utcCurrentLocalDate);
//        System.out.println(utcCurrentLocalDateTime);
//        System.out.println(s);
//
//        Date utcCurrentDate = DateFormateUtils.getUtcCurrentDate();
//        Date offsetDay = DateFormateUtils.getOffsetDay(utcCurrentDate, 2);
//        Date offsetHours1 = DateFormateUtils.getOffsetHours(utcCurrentDate, 1);
//        Date offsetHours = DateFormateUtils.getOffsetHours(utcCurrentDate, 1, 0);
//        Date offsetMins = DateFormateUtils.getOffsetMinute(DateFormateUtils.getUtcCurrentDate(), 129600);
//
//        System.out.println(utcCurrentDate);
//        System.out.println(offsetDay);
//        System.out.println(offsetHours1);
//        System.out.println(offsetHours);
//
//    }



    private DateFormateUtils(){
    }

    public static String asString(LocalDate localDate) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern(STANDARD_DATE_DAY4);
        return localDate.format(fmt);
    }

    /**
     * @Description LocalDate 转 Date
     * @Author gaoshenggang
     **/
    public static Date asDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * @Description LocalDateTime 转 Date
     * @Author gaoshenggang
     **/
    public static Date asDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * @Description Date 转 LocalDate
     * @Author gaoshenggang
     **/
    public static LocalDate asLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * @Description  Date 转 LocalDateTime
     * @Author gaoshenggang
     **/
    public static LocalDateTime asLocalDateTime(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * 获取精确到秒的时间戳
     * 精确到秒的时间戳10位长，精确到毫秒的时间戳13位长 Java的Date默认到毫秒
     * @return
     */
    public static Integer getSecondTimestamp(Date date){
        if (null == date) {
            return null;
        }
        String timestamp = String.valueOf(date.getTime());
        int length = timestamp.length();
        if (length > 3) {
            return Integer.valueOf(timestamp.substring(0,length-3));
        } else {
            return null;
        }
    }

    /**
     * @Description 将时间字符串，按指定格式转换成时间戳 Java Date默认转换的时间戳精确到毫秒，13位长
     * 注意 dateStr 和 必须格式一致
     * @Author gaoshenggang
     * @Param [dateStr, format]
     * @return java.lang.Long
     **/
    public static Long convert(String dateStr, String format){
        SimpleDateFormat sdf=new SimpleDateFormat(format);
        Long time = 0L;
        try{
            time = sdf.parse(dateStr).getTime();
            return time;
        }catch (Exception e){
            e.printStackTrace();
        }
        return time;
    }

    public static LocalDateTime convert() throws ParseException {
        String dateStr = STANDARD_DATE_VF;
        dateStr = dateStr.replaceAll("T", " ");
        Date dateTime = new SimpleDateFormat(DateFormateUtils.STANDARD_STAMP2).parse(dateStr);
        return dateTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static LocalDateTime convert(String dateStr) throws ParseException {
        dateStr = dateStr.replaceAll("T", " ");
        Date dateTime = new SimpleDateFormat(DateFormateUtils.STANDARD_STAMP2).parse(dateStr);
        return dateTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static LocalDate convertDate(String dateStr) {
        try {
            Date date = new SimpleDateFormat(DateFormateUtils.STANDARD_DATE_DAY2).parse(dateStr);
            return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String convert(Long time) {
        SimpleDateFormat sdf = new SimpleDateFormat(STANDARD_STAMP2);
        if (time == null || time == 0L) {
            return null;
        } else {
            return sdf.format(new Date(time));
        }
    }

    /**
     * @Description 将时间戳按指定格式转换成时间字符串
     * @Author gaoshenggang
     * @Param [time]
     * @return java.lang.String
     **/
    public static String convert(Long time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        if (time == null || time == 0L) {
            return null;
        } else {
            return sdf.format(new Date(time));
        }
    }

    /**
     * 获取当前时间的格式化时间字符串 ---系统时区
     * @param format 格式化格式
     * @return String 格式化之后的对象
     */
    public static String formatCurrentDate(String format) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * 获取当前UTC 国际时区的时间的格式化时间字符串
     * @param format 格式化格式
     * @return String 格式化之后的对象
     */
    public static String formatUtcCurrentDate(String format) {
        TimeZone timeZoneUTC = TimeZone.getTimeZone("UTC");
        Date date = Calendar.getInstance(timeZoneUTC).getTime();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(timeZoneUTC);
        return sdf.format(date);
    }

    /**
     * 获取当前UTC 国际时区的时间
     * @return Date 国际时区的时间
     */
    public static Date getUtcCurrentDate() {
        return getDateFromStr(formatUtcCurrentDate(STANDARD_STAMP), STANDARD_STAMP);
    }

    /**
     * 获取当前UTC 国际时区的时间
     * @return Date 国际时区的时间
     */
    public static Long getUtcCurrentTimeMillis() {
        return getDateFromStr(formatUtcCurrentDate(STANDARD_STAMP), STANDARD_STAMP).getTime();
    }

    /**
     * 获取当前UTC 国际时区的时间
     * @return LocalDate 国际时区的时间
     */
    public static LocalDate getUtcCurrentLocalDate() {
        return asLocalDate(getDateFromStr(formatUtcCurrentDate(STANDARD_STAMP), STANDARD_STAMP));
    }

    /**
     * 获取当前UTC 国际时区的时间
     * @return LocalDateTime 国际时区的时间
     */
    public static LocalDateTime getUtcCurrentLocalDateTime() {
        return asLocalDateTime(getDateFromStr(formatUtcCurrentDate(STANDARD_STAMP), STANDARD_STAMP));
    }

    /**
     * @Description 格式化指定日期
     * @param date 指定日期
     * @param formater 格式
     * @return String 格式化之后的字符串
     */
    public static String formateDate(Date date, String formater) {
        if (!StringUtils.isEmpty(formater)) {
            return new SimpleDateFormat(formater).format(date);
        }
        return null;
    }

    /**
     * @Description 将格式化的日期字符串转为DATE对象
     * @Author gaoshenggang
     * @param dateStr 格式化时间字符串
     * @param formater 格式
     * @return java.util.Date
     **/
    public static Date getDateFromStr(String dateStr, String formater) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(formater);
            Date date = format.parse(dateStr);
            return date;
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取指定日期当天的最后时间 YYYYMMDD 235959 999 的日期对象，精确到毫秒
     * @param date
     * @return
     */
    public static Date getLastedTimeOfDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }

    /**
     * 获取指定日期当天的开始时间 00:00:00.000 的日期对象，精确到毫秒
     * @param date 需要设置的日期
     * @return Date 日期对象
     */
    public static Date getDayOfBegin(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 以传入的日期对象为基准，获取当月最后一天的最后时间 精确到毫秒
     *
     * @param date
     * @return Date 日期对象
     */
    public static Date getEndOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, maxDay);

        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    /**
     * 依据传入的偏移量，将当前日期按 年 偏移，获取日期对象
     * 如果 offset 为负，则为之前的日期，如 offset=-1，则获取前一年的日期对象
     * 如果 offset 为正，则为之后的日期，如 offset=1，则获取后一年的日期对象
     * @param offset
     * @return
     */
    public static Date getOffsetYear(int offset) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, offset);
        return cal.getTime();
    }

    /**
     * 依据传入的偏移量，将当前日期按 月 偏移，获取日期对象
     * 如果 offset 为负，则为之前的日期，如 offset=-1，则获取前一月的日期对象
     * 如果 offset 为正，则为之后的日期，如 offset=1，则获取后一月的日期对象
     * @param offset
     * @return
     */
    public static Date getOffsetMonth(int offset) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, offset);
        return cal.getTime();
    }

    /**
     * 依据传入的偏移量，将当前时间按 日 偏移，获取日期对象
     * 如果 offset 为负，则为之前的日期，如 offset=-1，则获取昨日的日期对象
     * 如果 offset 为正，则为之后的日期，如 offset=1，则获取明日的日期对象
     * @param offset
     * @return Date 结果日期对象
     */
    public static Date getOffsetDay(int offset) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, offset);
        return cal.getTime();
    }

    /**
     * 依据传入的偏移量，将当前时间按 小时 偏移，获取日期对象
     * 如果 offset 为负，则为之前的日期，如 offset=-1，则获取前一小时的日期对象
     * 如果 offset 为正，则为之后的日期，如 offset=1，则获取后一小时的日期对象
     * @param offset
     * @return Date 结果日期对象
     */
    public static Date getOffsetHours(int offset) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, offset);
        return cal.getTime();
    }

    /**
     * 依据传入的偏移量，将当前时间按 分钟 偏移，获取日期对象
     * 如果 offset 为负，则为之前的日期，如 offset=-1，则获取前一分钟的日期对象
     * 如果 offset 为正，则为之后的日期，如 offset=1，则获取后一分钟的日期对象
     * @param offset
     * @return Date 结果日期对象
     */
    public static Date getOffsetMinute(int offset) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, offset);
        return cal.getTime();
    }

    /**
     * 依据传入的偏移量，将当前时间按 秒 偏移，获取日期对象
     * 如果 offset 为负，则为之前的日期，如 offset=-1，则获取前一秒的日期对象
     * 如果 offset 为正，则为之后的日期，如 offset=1，则获取后一秒的日期对象
     * @param offset
     * @return Date 结果日期对象
     */
    public static Date getOffsetSeconds(int offset) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, offset);
        return cal.getTime();
    }

    /**
     * 依据传入的偏移量，将当前时间按 年 偏移，获取日期对象
     * 如果 offset 为负，则为之前的日期，如 offset=-1，则获取昨日的日期对象
     * 如果 offset 为正，则为之后的日期，如 offset=1，则获取明日的日期对象
     * @param offset
     * @return Date 结果日期对象
     */
    public static Date getOffsetYear(Date date, int offset) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, offset);
        return cal.getTime();
    }

    /**
     * 依据传入的偏移量，将当前时间按 日 偏移，获取日期对象
     * 如果 offset 为负，则为之前的日期，如 offset=-1，则获取昨日的日期对象
     * 如果 offset 为正，则为之后的日期，如 offset=1，则获取明日的日期对象
     * @param offset
     * @return Date 结果日期对象
     */
    public static Date getOffsetDay(Date date, int offset) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, offset);
        return cal.getTime();
    }

    /**
     * 依据传入的偏移量，将当前时间按 小时 偏移，获取日期对象
     * 如果 offset 为负，则为之前的日期，如 offset=-1，则获取前一小时的日期对象
     * 如果 offset 为正，则为之后的日期，如 offset=1，则获取后一小时的日期对象
     * @param offset
     * @return Date 结果日期对象
     */
    public static Date getOffsetHours(Date date, int offset) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR, offset);
        return cal.getTime();
    }

    /**
     * 依据传入的偏移量，将当前时间按 小时 偏移，获取日期对象
     * 如果 offset 为负，则为之前的日期，如 offset=-1，则获取前一小时的日期对象
     * 如果 offset 为正，则为之后的日期，如 offset=1，则获取后一小时的日期对象
     * @param offset
     * @return Date 结果日期对象
     */
    public static Date getOffsetHours(Date date, int offset, int symbol) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        if(1 == symbol){
            offset = ~(offset - 1);
            cal.add(Calendar.HOUR, offset);
            return cal.getTime();
        }else {
            cal.add(Calendar.HOUR, offset);
            return cal.getTime();
        }
    }

    /**
     * 依据传入的偏移量，将当前时间按 小时 偏移，获取日期对象
     * 如果 offset 为负，则为之前的日期，如 offset=-1，则获取前一小时的日期对象
     * 如果 offset 为正，则为之后的日期，如 offset=1，则获取后一小时的日期对象
     * @param offset
     * @return Date 结果日期对象
     */
    public static LocalDateTime getOffsetHours(LocalDateTime localDateTime, int offset, int symbol) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(asDate(localDateTime));
        if(1 == symbol){
            offset = ~(offset - 1);
            cal.add(Calendar.HOUR, offset);
            return DateFormateUtils.asLocalDateTime(cal.getTime());
        }else {
            cal.add(Calendar.HOUR, offset);
            return DateFormateUtils.asLocalDateTime(cal.getTime());
        }
    }

    /**
     * 依据传入的偏移量，将当前时间按 分钟 偏移，获取日期对象
     * 如果 offset 为负，则为之前的日期，如 offset=-1，则获取前一分钟的日期对象
     * 如果 offset 为正，则为之后的日期，如 offset=1，则获取后一分钟的日期对象
     * @param offset
     * @return Date 结果日期对象
     */
    public static Date getOffsetMinute(Date date, int offset) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, offset);
        return cal.getTime();
    }

    /**
     * 依据传入的偏移量，将当前时间按 秒 偏移，获取日期对象
     * 如果 offset 为负，则为之前的日期，如 offset=-1，则获取前一秒的日期对象
     * 如果 offset 为正，则为之后的日期，如 offset=1，则获取后一秒的日期对象
     * @param offset
     * @return Date 结果日期对象
     */
    public static Date getOffsetSeconds(Date date, int offset) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.SECOND, offset);
        return cal.getTime();
    }

    /**
     * 依据传入的偏移量，将传入的日期按 日 偏移，获取日期对象
     * 如果 offset 为负，则为之前的日期，如 offset=-1，则获取前一天的日期对象
     * 如果 offset 为正，则为之后的日期，如 offset=1，则获取后一天的日期对象
     * @Param [date, offset]
     * @return Date 结果日期对象
     */
    public static Date getDateReverseDay(Date date, int offset) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, offset);
        return cal.getTime();
    }

    /**
     * 计算两个日期之间相差的天数，不满一天按0天算
     * @param start 开始时间
     * @param end 结束日期
     * @return int 相差天数
     */
    public static int getDaysBetween(Date start, Date end) {
        long endMill = end.getTime();
        long startMill = start.getTime();
        long dayMill = endMill - startMill;
        return (int) (dayMill / (1000 * 60 * 60 * 24));
    }

    /**
     * 判断当前时间是传入的当天的指定时间区间内
     *
     * @param begin 开始时间 HH:mm:ss
     * @param end   结束时间 HH:mm:ss
     * @return 是否在配置时间内
     */
    public static boolean duringTheTime(String begin, String end){
        SimpleDateFormat sdfDateTime = new SimpleDateFormat(STANDARD_STAMP2);
        SimpleDateFormat sdfDate = new SimpleDateFormat(STANDARD_DATE_DAY2);
        String nowDate = sdfDate.format(getUtcCurrentDate());
        begin = nowDate + " " + begin;
        end = nowDate + " " + end;
        try {
            Date beginDate = sdfDateTime.parse(begin);
            Date endDate = sdfDateTime.parse(end);
            Calendar nowTime = Calendar.getInstance();
            nowTime.setTime(getUtcCurrentDate());

            Calendar beginTime = Calendar.getInstance();
            beginTime.setTime(beginDate);

            Calendar endTime = Calendar.getInstance();
            endTime.setTime(endDate);
            //比较时间的先后
            return nowTime.after(beginTime) && nowTime.before(endTime);
        } catch (ParseException e) {
            log.error("check duringTheTime occurred exception: {}", e);
            return false;
        }
    }

    /**
     * 获取两个时间间隔的分钟数
     * @param start Date对象
     * @param end Date对象
     * @return 间隔分钟数
     */
    public static int minustsBetween(Date start, Date end){
        return (int)((end.getTime()-start.getTime())/1000/60);
    }

    /**
     * 将传入的时间字符串装换成另外的格式
     *
     * @param dateStr 待转换的时间字符串
     * @param dateStrFormat 待转换的时间字符串 的格式
     * @param formater 转换目标格式
     * @return 转换后的时间格式字符串
     */
    public static String formatDate(String dateStr, String dateStrFormat, String formater){
        SimpleDateFormat format = new SimpleDateFormat(dateStrFormat);
        SimpleDateFormat format1 = new SimpleDateFormat(formater);
        Date date;
        String date_Str = "";
        try {
            date = format.parse(dateStr);
            date_Str = format1.format(date);
        } catch (ParseException e) {
            log.error(e.getMessage());
            return null;
        }
        if(date_Str.equals("")){
            return dateStr;
        }else{
            return date_Str;
        }
    }

    /**
     * 获取传入月开始6个月的String月份列表
     * @return List<String> 包含传入月6个月的月份日期 yyyyMM String列表
     */
    public static List<String> getSixMonth(String beginDate){
        List<String> result = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat(STANDARD_DATE_MONTH);
        Date date;
        try {
            date = format.parse(beginDate);
            result.add(beginDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            for(int i=0;i<5;i++){
                cal.add(Calendar.MONTH,1);
                result.add(format.format(cal.getTime()));
            }
        } catch (ParseException e) {
            log.error(e.getMessage());
            return null;
        }
        return result;
    }

    /**
     * 获取传入月开始6个月的String月份列表
     * @return List<String> 包含传入月6个月的月份日期 yyyy年MM月 String列表
     */
    public static String getSixMonthFormat(String beginDate) {
        List<String> result = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat(STANDARD_DATE_MONTH);
        SimpleDateFormat format1 = new SimpleDateFormat(STANDARD_DATE_MONTH3);
        Date date;
        try {
            date = format.parse(beginDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            result.add(format1.format(cal.getTime()));
            for(int i=0;i<5;i++){
                cal.add(Calendar.MONTH,1);
                result.add(format1.format(cal.getTime()));
            }
        } catch (ParseException e) {
            log.error(e.getMessage());
            return null;
        }

        return result.toString();
    }

    /**
     * 获取指定日期所在月份开始的时间 格式yyyyMMddHHmmssSSS精确到毫秒
     * @return Date
     */
    public static Date getMonthBegin(Date date) {
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            //将日期至1号
            c.set(Calendar.DAY_OF_MONTH, 1);
            //将小时至0
            c.set(Calendar.HOUR_OF_DAY, 0);
            //将分钟至0
            c.set(Calendar.MINUTE, 0);
            //将秒至0
            c.set(Calendar.SECOND,0);
            //将毫秒至0
            c.set(Calendar.MILLISECOND, 0);

            return c.getTime();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 判断日期字符串是否合法(yyyy年-MM月-dd日)
     * @param strDate
     * @return
     */
    public static boolean isValidDate(String strDate,String formatStr) {
        //判断传入的yyyy年-MM月-dd日 字符串是否为数字
        String[] sArray = strDate.split("-");
        for (String s : sArray) {
            boolean isNum = s.matches("[0-9]+");
            //+表示1个或多个（如"3"或"225"），*表示0个或多个（[0-9]*）（如""或"1"或"22"），?表示0个或1个([0-9]?)(如""或"7")
            if (!isNum) {
                return false;
            }
        }
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        try {
            // 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2018-02-29会被接受，并转换成2018-03-01

            format.setLenient(false);
            format.parse(strDate);
        } catch (Exception e) {
            // e.printStackTrace();
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            return false;
        }

        return true;
    }

    /**
     * 得到上周的开始和结束日期（上周开始日期是星期一，结束日期是星期日） 格式yyyyMMdd-yyyyMMdd
     */
    public static String getLastTimeInterval() {
        SimpleDateFormat sdf = new SimpleDateFormat(STANDARD_DATE_DAY);
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        int dayOfWeek = calendar1.get(Calendar.DAY_OF_WEEK) - 1;
        int offset1 = 1 - dayOfWeek;
        int offset2 = 7 - dayOfWeek;
        calendar1.add(Calendar.DATE, offset1 - 7);
        calendar2.add(Calendar.DATE, offset2 - 7);
        String lastBeginDate = sdf.format(calendar1.getTime());
        String lastEndDate = sdf.format(calendar2.getTime());
        return lastBeginDate + "-" + lastEndDate;
    }

    /**
     * 得到输入日期所在一周的开始和结束日期（一周开始日期是星期一，结束日期是星期日）
     * 格式 yyyyMMdd-yyyyMMdd
     */
    public static String convertWeekByDate(Date date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(STANDARD_DATE_DAY);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
            if (1 == dayWeek) {
                cal.add(Calendar.DAY_OF_MONTH, -1);
            }
            cal.setFirstDayOfWeek(Calendar.MONDAY);
            int day = cal.get(Calendar.DAY_OF_WEEK);
            cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
            String imptimeBegin = sdf.format(cal.getTime());
            cal.add(Calendar.DATE, 6);
            String imptimeEnd = sdf.format(cal.getTime());
            return imptimeBegin + "-" + imptimeEnd;
        } catch (Exception e) {
            return null;
        }
    }



}
