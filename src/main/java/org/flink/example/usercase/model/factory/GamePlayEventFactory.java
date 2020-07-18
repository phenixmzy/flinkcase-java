package org.flink.example.usercase.model.factory;

import org.flink.example.usercase.model.GamePlayEvent;

public class GamePlayEventFactory {
    private final static String IP_SPLIT = ".";
    private final static String[] CLIENT_APPS = new String[]{"kuaiwan", "kuaibo", "baidu", "kuwo"};
    private final static String[] CLIENT_VERSIONS = new String[]{"3.3.12", "2.7.5", "3.2.11", "3.5.25"};
    private final static String[] gameTypes = new String[]{"exe", "web", "online", "flash"};
    private final static String[] channelFroms = new String[]{"my","category", "game_helper", "recommend", "762", "4399", "relateflash", "kuwo"};
    private final static String[] sites = new String[]{"index", "kw", "qvod", "baidu", "tx", "kugo"};

    public static GamePlayEvent makeGamePlay(int gameIdMaxNum, int userIdMaxNum, int maxDelay, int maxTimeLen) {
        String gameId = String.valueOf((int)((Math.random()*9+1) * gameIdMaxNum));
        String userId = String.valueOf((long)((Math.random()*9+1) * userIdMaxNum));
        int currTimeStamp = (int)(System.currentTimeMillis()/1000) ;
        int delay = getRandNum(1, maxDelay);
        int timeLen = getRandNum(1, maxTimeLen);
        int leaveTime = currTimeStamp - delay;
        int startTime = leaveTime - timeLen;
        String gameType = gameTypes[getRandNum(0,4) % 4];
        String clientApp = CLIENT_APPS[getRandNum(0,4) % 4];
        String clientVersion = CLIENT_VERSIONS[getRandNum(0,4) % 4];
        String channelFrom = channelFroms[getRandNum(0,8) % 8];
        String site = sites[getRandNum(0,6) % 6];
        String userIp = getUserIp();

        GamePlayEvent gamePlayEvent = new GamePlayEvent();
        gamePlayEvent.setGameId(gameId);
        gamePlayEvent.setUserId(userId);
        gamePlayEvent.setStartTime(startTime);
        gamePlayEvent.setLeaveTime(leaveTime);
        gamePlayEvent.setGameType(gameType);
        gamePlayEvent.setChannelFrom(channelFrom);
        gamePlayEvent.setSite(site);
        gamePlayEvent.setUserIp(userIp);
        gamePlayEvent.setClientVersion(clientVersion);
        gamePlayEvent.setClientDriver(clientApp);
        return gamePlayEvent;
    }

    private static int getRandNum(int min, int max)  {
        return (int)(Math.random()*(max-min)+min);
    }

    public static String getUserIp() {
        StringBuilder builder = new StringBuilder();
        builder.append(getRandNum(10,220))
                .append(IP_SPLIT).append(getRandNum(20,192))
                .append(IP_SPLIT).append(getRandNum(2,160))
                .append(IP_SPLIT).append(getRandNum(2,220));
        return builder.toString();
    }

    public static void main(String[] args) {
        GamePlayEvent envent = makeGamePlay(10000, 10000000, 300, 300);
        System.out.println("gameId="+envent.getGameId() + " userId=" + envent.getUserId() + " userIp=" + envent.getUserIp()+ " startTime=" + envent.getStartTime() + " channelFrom=" + envent.getChannelFrom() + " gameType=" + envent.getGameType());
    }

}
