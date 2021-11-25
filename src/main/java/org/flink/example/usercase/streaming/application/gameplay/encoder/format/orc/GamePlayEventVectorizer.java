package org.flink.example.usercase.streaming.application.gameplay.encoder.format.orc;


import java.io.IOException;
import java.io.Serializable;

import org.apache.flink.table.data.vector.IntColumnVector;
import org.apache.hadoop.hive.ql.exec.vector.BytesColumnVector;
import org.apache.hadoop.hive.ql.exec.vector.LongColumnVector;
import org.apache.flink.orc.vector.Vectorizer;
import org.apache.hadoop.hive.ql.exec.vector.VectorizedRowBatch;


import org.flink.example.usercase.model.GamePlayEvent;

public class GamePlayEventVectorizer extends Vectorizer<GamePlayEvent> implements Serializable {
    private int startTime;
    private int leaveTime;
    private int timeLen;
    private String userIp;
    private String gameType;
    private String channelFrom;
    private String site;
    private String clientVersion;
    private String version;
    private String driver;

    public GamePlayEventVectorizer(String schema) {
        super(schema);
    }

    @Override
    public void vectorize(GamePlayEvent gamePlayEvent, VectorizedRowBatch vectorizedRowBatch) throws IOException {
        LongColumnVector txId = (LongColumnVector)vectorizedRowBatch.cols[0];
        BytesColumnVector gameId = (BytesColumnVector)vectorizedRowBatch.cols[1];
        BytesColumnVector userId = (BytesColumnVector)vectorizedRowBatch.cols[2];
        IntColumnVector startTime = (IntColumnVector)vectorizedRowBatch.cols[3];
        IntColumnVector leaveTime = (IntColumnVector)vectorizedRowBatch.cols[4];
        IntColumnVector timeLen = (IntColumnVector)vectorizedRowBatch.cols[5];
        BytesColumnVector userIp = (BytesColumnVector)vectorizedRowBatch.cols[6];
        BytesColumnVector gameType = (BytesColumnVector)vectorizedRowBatch.cols[7];
        BytesColumnVector channelFrom = (BytesColumnVector)vectorizedRowBatch.cols[8];
        BytesColumnVector site = (BytesColumnVector)vectorizedRowBatch.cols[9];
        BytesColumnVector clientVersion = (BytesColumnVector)vectorizedRowBatch.cols[10];
        BytesColumnVector version = (BytesColumnVector)vectorizedRowBatch.cols[11];
        BytesColumnVector driver = (BytesColumnVector)vectorizedRowBatch.cols[12];
        int row = vectorizedRowBatch.size++;



    }
}
