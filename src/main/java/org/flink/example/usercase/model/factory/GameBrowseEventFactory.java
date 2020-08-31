package org.flink.example.usercase.model.factory;

import org.flink.example.usercase.model.GameBrowseEvent;

public class GameBrowseEventFactory {
    private final static SnowFlake snowFlake = new SnowFlake(10L, 30L);
    private final static String IP_SPLIT = ".";
    private static final String[] GAME_TYPES = new String[]{"exe", "web", "onlie", "flash"};
    private static final String[] CHANNEL_FROMS = new String[]{"my", "category", "game_helper", "recommend", "726", "4399", "kuwo", "relateflash"};
    private static final String[] SITES = new String[]{"index", "kw", "qvod", "kugo", "qq", "qvod"};
    private static final String[] CLIENT_VERSIONS = new String[] {"1.6.2","1.8.8","2.31.7","2.5.16","2.7.19","3.2.1","3.1.2","3.4.8"};
    private static final String[] DRIVERS = new String[] {"PC","MAC PRO","MAC AIR","MI","HUAWEI MateBook","DELL", "LX", "SX", "HP"};
    private static final String[] VERSIONS = new String[]{"0.1","0.2","0.2.3","1.3.1","2.4","2.5.2","2.8"};

    public static GameBrowseEvent build(int gameIdMaxNum, int userIdMaxNum, int maxDelay) {
        String gameId = String.valueOf((int)((Math.random()*9+1) * gameIdMaxNum));
        String userId = String.valueOf((long)((Math.random()*9+1) * userIdMaxNum));
        int currTimeStamp = (int)(System.currentTimeMillis()/1000) ;

        int timeLen = getRandNum(1, 300);
        String gameType = GAME_TYPES[getRandNum(0,4) % 4];
        String channelFrom = CHANNEL_FROMS[getRandNum(0,8) % 8];
        String site = SITES[getRandNum(0,6) % 6];
        String userIp = getUserIp();
        String clientVersion = CLIENT_VERSIONS[getRandNum(0,CLIENT_VERSIONS.length)%CLIENT_VERSIONS.length];
        String version = VERSIONS[getRandNum(0,VERSIONS.length)%VERSIONS.length];
        String driver = DRIVERS[getRandNum(0, DRIVERS.length)%DRIVERS.length];

        GameBrowseEvent browse = new GameBrowseEvent();
        browse.setTxId(snowFlake.nextId());
        browse.setGameId(gameId);
        browse.setUserId(userId);
        browse.setTimeLen(timeLen);
        browse.setBrowseTime(currTimeStamp);
        browse.setGameType(gameType);
        browse.setSite(site);
        browse.setChannelFrom(channelFrom);
        browse.setUserIp(userIp);
        browse.setClientVersion(clientVersion);
        browse.setVersion(version);
        browse.setDriver(driver);
        return browse;
    }

    public static String getUserIp() {
        StringBuilder builder = new StringBuilder();
        builder.append(getRandNum(10,220))
                .append(IP_SPLIT).append(getRandNum(20,192))
                .append(IP_SPLIT).append(getRandNum(2,160))
                .append(IP_SPLIT).append(getRandNum(2,220));
        return builder.toString();
    }

    private static int getRandNum(int min, int max) {
        return (int)(Math.random() * (max - min) + min);
    }

}
