package org.flink.example;

import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import ua_parser.Client;
import ua_parser.Parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TestHutool {

    public static void testUaParser(String agentStr) {
        Parser uaParser = new Parser();
        Client c = uaParser.parse(agentStr);
        StringBuilder b = new StringBuilder();
        b.append("[agent.family:"+c.userAgent.family).append(",") // => "Mobile Safari"
        .append("agent.major:"+c.userAgent.major).append(",")  // => "5"
        .append("agent.minor:"+c.userAgent.minor).append(",")  // => "1"
        .append("agent.patch:"+c.userAgent.patch).append(",")
        .append("os.family:"+c.os.family).append(",")       // => "iOS"
        .append("os.major:"+c.os.major).append(",")        // => "5"
        .append("os.minor:"+c.os.minor).append(",")         // => "1"
        .append("os.patch:"+c.os.patch).append(",")
        .append("device.family:"+c.device.family).append("]");    // => "iPhone"
        System.out.println(b.toString());
    }

    public static void testHutool(String agentStr) {
        UserAgent ua = UserAgentUtil.parse(agentStr);
        StringBuilder b = new StringBuilder();
        b.append("[Mobile:"+ua.isMobile()).append(" ,");
        b.append("Browser:"+ua.getBrowser()).append(" ,");//Chrome
        b.append("Version:"+ua.getVersion()).append(" ,");//14.0.835.163
        b.append("Engine:"+ua.getEngine()).append(" ,");//Webkit
        b.append("EngineVersion:"+ua.getEngineVersion()).append(" ,");//535.1
        b.append("Platform:"+ua.getPlatform()).append(" ,");//Windows
        b.append("OS:"+ua.getOs()).append(" ,");//Windows 7
        b.append("OsVersion:"+ua.getOsVersion()).append(" ]");
        System.out.println(b.toString());
    }

    public static void main(String[] args) {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader("D:\\nginx.log"));
            String agentStr = null;
            int i = 1;
            while ((agentStr = in.readLine()) != null) {
                System.out.println(String.valueOf(i)+":记录对比");
                testUaParser(agentStr);
                testHutool(agentStr);
                i++;
            }
        } catch (Exception ex) {

        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
