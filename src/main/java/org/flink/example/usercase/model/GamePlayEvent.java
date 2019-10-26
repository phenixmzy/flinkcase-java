package org.flink.example.usercase.model;

public class GamePlayEvent {
    private String gameId;
    private String userId;
    private int startTime;
    private int leaveTime;
    private int timeLen;
    private String userIp;
    private String gameType;
    private String channelFrom;
    private String site;

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(int leaveTime) {
        this.leaveTime = leaveTime;
    }

    public int getTimeLen() {
        return timeLen;
    }

    public void setTimeLen(int timeLen) {
        this.timeLen = timeLen;
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public String getChannelFrom() {
        return channelFrom;
    }

    public void setChannelFrom(String channelFrom) {
        this.channelFrom = channelFrom;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

  /*  @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GamePlayEvent that = (GamePlayEvent) o;
        return Objects.equals(gameId, that.gameId) &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(gameType, that.gameType);
    }

    @Override
    public int hashCode() {
        StringBuilder builder = new StringBuilder();
        builder.append(gameId).append(userId).append(gameType);
        char[] charArr = builder.toString().toCharArray();
        int hash = 0;
        for(char c : charArr) {
            hash = hash * 131 + c;
        }
        return hash;
    }*/

}
