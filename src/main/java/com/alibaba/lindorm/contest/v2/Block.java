package com.alibaba.lindorm.contest.v2;

import com.alibaba.lindorm.contest.structs.ColumnValue;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Block {

    // block size
    private final long[] timestamps;

    private final Map<String, ColumnValue[]> values;

    private final int size;

    private final Data data;

    private int index;

    public Block(Data data){
        this.size = Const.BLOCK_SIZE;
        this.timestamps = new long[size];
        this.data = data;
        this.values = new ConcurrentHashMap<>(60);
    }

    public synchronized void insert(long timestamp, Map<String, ColumnValue> columns){
        timestamps[index] = timestamp;
        for (Map.Entry<String, ColumnValue> e: columns.entrySet()){
            ColumnValue[] values = this.values.computeIfAbsent(e.getKey(), k -> new ColumnValue[this.size]);
            values[index] = e.getValue();
        }
        index ++;
    }

    public int remaining(){
        return size - index - 1;
    }

    public long flush() throws IOException {
        ByteBuffer dataBuffer = Context.getBlockWriteBuffer();
        dataBuffer.clear();

        ByteBuffer headerBuffer = Context.getBlockHeaderBuffer();
        headerBuffer.clear();

        for (String columnKey: Const.SORTED_COLUMNS){
            headerBuffer.putInt(dataBuffer.position());
            ColumnValue[] values = this.values.get(columnKey);
            for (ColumnValue value: values){
                switch (value.getColumnType()){
                    case COLUMN_TYPE_DOUBLE_FLOAT:
                        dataBuffer.putDouble(value.getDoubleFloatValue());
                    case COLUMN_TYPE_INTEGER:
                        dataBuffer.putInt(value.getIntegerValue());
                    case COLUMN_TYPE_STRING:
                        byte[] bs = value.getStringValue().array();
                        dataBuffer.put((byte) bs.length);
                        dataBuffer.put(bs);
                }
            }
        }

        for (long timestamp: timestamps){
            headerBuffer.putLong(timestamp);
        }

        headerBuffer.flip();
        dataBuffer.flip();

        ByteBuffer writerBuffer = Context.getBlockWriteBuffer();
        writerBuffer.clear();
        // header
        writerBuffer.putInt(headerBuffer.remaining());
        writerBuffer.putInt(dataBuffer.remaining());
        writerBuffer.put(headerBuffer);
        writerBuffer.put(dataBuffer);
        return this.data.write(writerBuffer);
    }

    public Map<Long, Map<String, ColumnValue>> read(Set<Long> requestedTimestamps, Set<String> requestedColumns) {
        Map<Long, Map<String, ColumnValue>> results = new HashMap<>();
        for (String requestedColumn: requestedColumns){
            ColumnValue[] columnValues = this.values.get(requestedColumn);
            for (int i = 0; i < timestamps.length; i++) {
                long timestamp = timestamps[i];
                if (! requestedTimestamps.contains(timestamp)){
                    continue;
                }
                Map<String, ColumnValue> values = results.computeIfAbsent(timestamp, k -> new HashMap<>());
                values.put(requestedColumn, columnValues[i]);
            }
        }
        return results;
    }

    public static Map<Long, Map<String, ColumnValue>> read(Data data, long position, Set<Long> requestedTimestamps, Set<String> requestedColumns) throws IOException {
        ByteBuffer readBuffer = Context.getBlockReadBuffer();
        readBuffer.clear();

        int readBytes = data.read(readBuffer, position, 8);
        if (readBytes != 4){
            throw new IOException("read bytes not enough");
        }

        readBuffer.flip();
        int headerSize = readBuffer.getInt();
        int dataSize = readBuffer.getInt();

        readBuffer.clear();
        data.read(readBuffer, position + 8, headerSize);
        readBuffer.flip();

        int[] positions = new int[Const.COLUMN_COUNT];
        for (int i = 0; i < Const.COLUMN_COUNT; i++) {
            positions[i] = readBuffer.getInt();
        }

        long[] timestamps = new long[Const.BLOCK_SIZE];
        for (int i = 0; i < Const.BLOCK_SIZE; i++) {
            timestamps[i] = readBuffer.getLong();
        }

        readBuffer.clear();
        data.read(readBuffer, position + 8 + headerSize, dataSize);
        readBuffer.flip();

        Map<Long, Map<String, ColumnValue>> results = new HashMap<>();
        for (String requestedColumn: requestedColumns){
            Colum column = Const.COLUMNS_INDEX.get(requestedColumn);
            int index = column.getIndex();
            ColumnValue.ColumnType type = column.getType();

            int latestPos = dataSize;
            if (index < Const.COLUMN_COUNT - 1){
                latestPos = positions[index + 1];
            }
            int currentPos = positions[index];

            readBuffer.position(currentPos);
            readBuffer.limit(latestPos - currentPos);

            for (long timestamp: timestamps){
                if (! requestedTimestamps.contains(timestamp)){
                    switch (type){
                        case COLUMN_TYPE_DOUBLE_FLOAT:
                            readBuffer.position(readBuffer.position() + 8);
                            break;
                        case COLUMN_TYPE_INTEGER:
                            readBuffer.position(readBuffer.position() + 4);
                            break;
                        case COLUMN_TYPE_STRING:
                            byte len = readBuffer.get();
                            readBuffer.position(readBuffer.position() + len);
                            break;
                    }
                    continue;
                }

                Map<String, ColumnValue> values = results.computeIfAbsent(timestamp, k -> new HashMap<>());
                switch (type){
                    case COLUMN_TYPE_DOUBLE_FLOAT:
                        values.put(requestedColumn, new ColumnValue.DoubleFloatColumn(readBuffer.getDouble()));
                        break;
                    case COLUMN_TYPE_INTEGER:
                        values.put(requestedColumn, new ColumnValue.IntegerColumn(readBuffer.getInt()));
                        break;
                    case COLUMN_TYPE_STRING:
                        byte len = readBuffer.get();
                        byte[] bs = new byte[len];
                        values.put(requestedColumn, new ColumnValue.StringColumn(ByteBuffer.wrap(bs)));
                        break;
                }
            }
        }
        return results;
    }
}
