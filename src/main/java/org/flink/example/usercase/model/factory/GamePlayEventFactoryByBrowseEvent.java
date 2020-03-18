package org.flink.example.usercase.model.factory;

import org.flink.example.usercase.model.GameBrowseEvent;
import org.flink.example.usercase.model.GamePlayEvent;

public class GamePlayEventFactoryByBrowseEvent {
    private final static String IP_SPLIT = ".";

    public static GamePlayEvent makeGamePlay(int maxDelay, int maxTimeLen, GameBrowseEvent browseEvent) {
        String gameId = browseEvent.getGameId();
        String userId = browseEvent.getGameId();
        int browseTimeStamp = browseEvent.getBrowseTime() ;
        int delay = getRandNum(1, maxDelay);
        int timeLen = getRandNum(1, maxTimeLen);
        int leaveTime = browseTimeStamp - delay;
        int startTime = leaveTime - timeLen;
        String gameType = browseEvent.getGameType();
        String channelFrom = browseEvent.getChannelFrom();
        String site = browseEvent.getSite();
        String userIp = browseEvent.getUserIp();

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
        GamePlayEvent envent = makeGamePlay( 300, 300, null);
        System.out.println("gameId="+envent.getGameId() + " userId=" + envent.getUserId() + " userIp=" + envent.getUserIp()+ " startTime=" + envent.getStartTime() + " channelFrom=" + envent.getChannelFrom() + " gameType=" + envent.getGameType());
    }

}
