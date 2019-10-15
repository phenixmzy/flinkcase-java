package org.flink.example.usercase.model;

public class GamePlayFactory {
    private final static String IP_SPLIT = ".";
    private final static String[] gameTypes = new String[]{"exe", "web", "online", "flash"};
    private final static String[] channelFroms = new String[]{"my","category", "game_helper", "recommend", "762", "4399", "relateflash", "kuwo"};
    private final static String[] sites = new String[]{"index", "kw", "qvod", "baidu", "tx", "kugo"};

    public static GamePlay makeGamePlay() {
        String gameId = String.valueOf((Math.random()*9+1)*10000);
        String userId = String.valueOf((Math.random()*9+1)*10000000);
        int currTimeStamp = (int)System.currentTimeMillis()/1000 ;
        int delay = getRandNum(1, 300);
        int timeLen = getRandNum(1, 300);
        int leaveTime = currTimeStamp - delay;
        int startTime = leaveTime - timeLen;
        String gameType = gameTypes[getRandNum(0,4) % 4];
        String channelFrom = channelFroms[getRandNum(0,8) % 8];
        String site = sites[getRandNum(0,6) % 6];
        String userIp = getUserIp();

        GamePlay gamePlay = new GamePlay();
        gamePlay.setGameId(gameId);
        gamePlay.setUserId(userId);
        gamePlay.setStartTime(startTime);
        gamePlay.setLeaveTime(leaveTime);
        gamePlay.setGameType(gameType);
        gamePlay.setChannelFrom(channelFrom);
        gamePlay.setSite(site);
        gamePlay.setUserIp(userIp);
        return gamePlay;
    }

    private static int getRandNum(int min, int max)  {
        return (int)(Math.random()*(max-min)+min);
    }

    public static String getUserIp() {
        StringBuilder builder = new StringBuilder(getRandNum(10,220));
        builder.append(IP_SPLIT).append(getRandNum(20,192))
                .append(IP_SPLIT).append(getRandNum(2,160))
                .append(IP_SPLIT).append(getRandNum(2,220));
        return builder.toString();
    }

}
