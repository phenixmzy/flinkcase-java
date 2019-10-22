package org.flink.example.usercase.streaming.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;

public class DateTimeUtil {
    public static long getTimeStampMS(String timeStr, String dateStrFormat) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat(dateStrFormat);
        return df.parse(timeStr).getTime();
    }

    public static String getTimeStampStr(long dateTime, String dateStrFormat) throws  ParseException {
        Date date = new Date(dateTime);
        SimpleDateFormat df = new SimpleDateFormat(dateStrFormat);
        return df.format(date);
    }

    /**
    public static void main(String[] args) throws  ParseException {
        System.out.println(getTimeStampStr(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
    }
    */
}
