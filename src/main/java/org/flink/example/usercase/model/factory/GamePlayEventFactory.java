package org.flink.example.usercase.model.factory;

import org.flink.example.usercase.model.GamePlayEvent;

public class GamePlayEventFactory {
    private final static String IP_SPLIT = ".";
    private final static String[] gameTypes = new String[]{"exe", "web", "online", "flash"};
    private final static String[] channelFroms = new String[]{"my","category", "game_helper", "recommend", "762", "4399", "relateflash", "kuwo"};
    private final static String[] sites = new String[]{"index", "kw", "qvod", "baidu", "tx", "kugo"};

    public static GamePlayEvent makeGamePlay() {
        String gameId = String.valueOf((int)((Math.random()*9+1)*10000));
        String userId = String.valueOf((long)((Math.random()*9+1)*10000000));
        int currTimeStamp = (int)(System.currentTimeMillis()/1000) ;
        int delay = getRandNum(1, 300);
        int timeLen = getRandNum(1, 300);
        int leaveTime = currTimeStamp - delay;
        int startTime = leaveTime - timeLen;
        String gameType = gameTypes[getRandNum(0,4) % 4];
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
        GamePlayEvent envent = makeGamePlay();
        System.out.println("gameId="+envent.getGameId() + " userId=" + envent.getUserId() + " userIp=" + envent.getUserIp()+ " startTime=" + envent.getStartTime() + " channelFrom=" + envent.getChannelFrom() + " gameType=" + envent.getGameType());
    }

}
