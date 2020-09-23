package org.flink.example.usercase.model;

public class GameInfoHelper {
    public static void set(GameInfo gameInfo, String key, byte[] vals) {
        if (key.equals("game_name")) {
            gameInfo.setGameName(new String(vals));
        } else if (key.equals("game_type")) {
            gameInfo.setGameType(new String(vals));
        } else if (key.equals("game_op_type")) {
            gameInfo.setOperatorType(new String(vals));
        } else if (key.equals("game_size")) {
            gameInfo.setGameSize(Long.valueOf(new String(vals)));
        } else if (key.equals("game_label")) {
            gameInfo.setGameLabel(new String(vals));
        } else if (key.equals("developer")) {
            gameInfo.setDeveloper(new String(vals));
        } else if (key.equals("language")) {
            gameInfo.setLanguage(new String(vals));
        } else if (key.equals("publishing_time")) {
            gameInfo.setPublishingTime(Integer.valueOf(new String(vals)));
        } else if (key.equals("update_time")) {
            gameInfo.setUpdateTime(Integer.valueOf(new String(vals)));
        }
    }
}
