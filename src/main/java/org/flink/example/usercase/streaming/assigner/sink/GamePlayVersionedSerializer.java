package org.flink.example.usercase.streaming.assigner.sink;

import org.apache.flink.core.io.SimpleVersionedSerializer;
import org.flink.example.usercase.model.GamePlayEvent;
import org.flink.example.usercase.streaming.util.GsonUtil;

import java.io.IOException;

public class GamePlayVersionedSerializer implements SimpleVersionedSerializer<GamePlayEvent> {
    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public byte[] serialize(GamePlayEvent gamePlayEvent) throws IOException {
        return GsonUtil.toJSONBytes(gamePlayEvent);
    }

    @Override
    public GamePlayEvent deserialize(int i, byte[] bytes) throws IOException {
        return null;
    }
}
