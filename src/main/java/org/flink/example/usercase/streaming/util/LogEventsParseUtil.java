package org.flink.example.usercase.streaming.util;

import org.flink.example.usercase.model.HDFSAuditEvent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogEventsParseUtil {

  public static HDFSAuditEvent parseHDFSAuditEvent(String log) throws Exception {
        int index0 = log.indexOf(" ");
        index0 = log.indexOf(" ", index0 + 1);
        String data = log.substring(0, index0).trim();
        int index1 = log.indexOf("allowed=");
        int len1 = 8;
        int index2 = log.indexOf("ugi=");
        int len2 = 4;
        int index3 = log.indexOf("ip=/");
        int len3 = 4;
        int index4 = log.indexOf("cmd=");
        int len4 = 4;
        int index5 = log.indexOf("src=");
        int len5 = 4;
        int index6 = log.indexOf("dst=");
        int len6 = 4;
        int index7 = log.indexOf("perm=");

        long timeStampMS = getTimeStampMS(matchTime(log), "yyyy-MM-dd HH:mm:ss,SSS");
        String allowed = log.substring(index1 + len1, index2).trim();
        String ugi = log.substring(index2 + len2, index3).trim();
        String ip = log.substring(index3 + len3, index4).trim();
        String cmd = log.substring(index4 + len4, index5).trim();
        String src = log.substring(index5 + len5, index6).trim();
        String dst = log.substring(index6 + len6, index7).trim();
        HDFSAuditEvent hdfsAudit = new HDFSAuditEvent();
        hdfsAudit.setTimeStampMS(timeStampMS);
        hdfsAudit.setAllowed(allowed);
        hdfsAudit.setUgi(ugi);
        hdfsAudit.setIp(ip);
        hdfsAudit.setCmd(cmd);
        hdfsAudit.setSrc(src);
        hdfsAudit.setDst(dst);
        return hdfsAudit;
    }

    private static String matchTime(String log) throws Exception{
        Pattern pattern = Pattern.compile("[0-9]{4}[-][0-9]{2}[-][0-9]{2}[ ][0-9]{2}[:][0-9]{2}[:][0-9]{2},[0-9]{3}");
        Matcher matcher = pattern.matcher(log);
        if (!matcher.find()) {
            new Exception("can't match time in log.");
        }
        return matcher.group();
    }

    private static long getTimeStampMS(String timeStr, String dateStrFormat) throws ParseException{
        SimpleDateFormat df = new SimpleDateFormat(dateStrFormat);
        return df.parse(timeStr).getTime();
    }
}
