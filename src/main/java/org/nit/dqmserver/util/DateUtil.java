package org.nit.dqmserver.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author sensordb
 * @date 2018/5/21
 */

public class DateUtil {

    public static String dateToStr(final Date date) {
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);

    }

    public static String dateToStrYMD(final Date date) {
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    public static String dateToStrYMDDot(final Date date, final String prefix) {
        final SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
        final String str = format.format(date);
        if (prefix == null) {
            return str;
        }
        return prefix + str;
    }

    public static String dateToStrForLog(final Date date) {
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        return format.format(date);

    }

    public static double timeGap(final long start, final long end) {
        return (end - start) * 1.0 / 1000.0;
    }

    public static Date strToDate(final String str) {
        if (str == null) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(str);
        }
        catch (ParseException e) {
            e.printStackTrace();
            format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                date = format.parse(str);
            }
            catch (ParseException e2) {
                e2.printStackTrace();
            }
        }
        return date;
    }

    public static boolean timeBetween(String startTime, String time, String endTime) {

        java.text.DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Calendar c1 = java.util.Calendar.getInstance();
        java.util.Calendar c2 = java.util.Calendar.getInstance();
        java.util.Calendar c3 = java.util.Calendar.getInstance();
        try {
            c1.setTime(df.parse(startTime));
            c2.setTime(df.parse(time));
            c3.setTime(df.parse(endTime));
        } catch (ParseException e) {
            return false;
        }
        return  c2.compareTo(c1) >= 0 && c2.compareTo(c3) <= 0;
    }

    public static boolean timeBetween(String startTime, Date time, String endTime) {

        java.text.DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Calendar c1 = java.util.Calendar.getInstance();
        java.util.Calendar c2 = java.util.Calendar.getInstance();
        java.util.Calendar c3 = java.util.Calendar.getInstance();
        try {
            c1.setTime(df.parse(startTime));
            c2.setTime(time);
            c3.setTime(df.parse(endTime));
        } catch (Exception e) {
            return false;
        }
        return c2.compareTo(c1) >= 0 && c2.compareTo(c3) <= 0;
    }

    public static String dateMinus(Date date1, Date date2) {
        long l = date1.getTime() - date2.getTime();
        long day = l / (24 * 60 * 60 * 1000);
        long hour = (l / (60 * 60 * 1000) - day * 24);
        long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        // "" + day + "天" + hour + "小时" + min + "分" + s + "秒";
        if (day == 0 && hour == 0 && min == 0) {
            return s + "秒";
        } else if (day == 0 && hour == 0) {
            return min + "分" + s + "秒";
        } else if (day == 0) {
            return hour + "小时" + min + "分" + s + "秒";
        } else {
            return "" + day + "天" + hour + "小时" + min + "分" + s + "秒";
        }
    }

    public static String utcStringToDatetime(String utcString) {
        if (utcString == null){
            return null;
        }
        try{
            SimpleDateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            utcFormat.setTimeZone(TimeZone.getTimeZone("GMT+13"));
            SimpleDateFormat defaultFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = utcFormat.parse(utcString);
            return defaultFormat.format(date);
        } catch(ParseException pe)
        {
            pe.printStackTrace();
            return null;
        }
    }


    public static Date utcStringToDate(String utcString) {
        try{
            SimpleDateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            return utcFormat.parse(utcString);
        } catch(ParseException pe)
        {
            pe.printStackTrace();
            return null;
        }
    }

}
