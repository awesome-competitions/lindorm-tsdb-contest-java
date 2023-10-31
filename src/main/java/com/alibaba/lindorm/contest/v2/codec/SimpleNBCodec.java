package com.alibaba.lindorm.contest.v2.codec;

import com.alibaba.lindorm.contest.util.Util;
import com.alibaba.lindorm.contest.v2.Context;
import net.magik6k.bitbuffer.BitBuffer;
import net.magik6k.bitbuffer.DirectBitBuffer;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class SimpleNBCodec extends Codec<int[]>{
    @Override
    public void encode(ByteBuffer src, int[] data, int size) {
        int minValue = Integer.MAX_VALUE;
        int maxValue = Integer.MIN_VALUE;
        for (int i = 0; i < size; i++) {
            if (data[i] < minValue){
                minValue = data[i];
            }
            if (data[i] > maxValue){
                maxValue = data[i];
            }
        }
        int maxBits = Util.parseBits(maxValue - minValue, true);
        BitBuffer buffer = new DirectBitBuffer(src);
        encodeVarInt(buffer, minValue);
        buffer.put(maxBits, 5);
        for (int value : data) {
            buffer.putInt(value - minValue, maxBits);
        }
        buffer.flip();
    }

    @Override
    public void decode(ByteBuffer src, int[] data, int size) {
        BitBuffer buffer = new DirectBitBuffer(src);
        int min = decodeVarInt(buffer);
        int bits = buffer.getIntUnsigned(5);
        for (int i = 0; i < size; i++) {
            data[i] = buffer.getIntUnsigned(bits) + min;
        }
    }

    public static void main(String[] args) {
        SimpleNBCodec varintCodec = new SimpleNBCodec();
//        int[] numbers = {47508323,38447491,9735651,48158467,31176995,21444931,30686115,41353795,3464675,12283267,16355299,22657539,37193507,44959939,46392611,4670915};
        int[] numbers = {1364896,287712,1075872,1498688,1017216,285152,1042688,194016,821248,1123488,695520,997760,33728,300160,732832,1511136};

        ByteBuffer encodedBuffer = ByteBuffer.allocate(3000);
        varintCodec.encode(encodedBuffer, numbers, numbers.length);

        encodedBuffer.flip();
        System.out.println(encodedBuffer.remaining());
        System.out.println(numbers.length * 4);

        int size = numbers.length;
        varintCodec.decode(encodedBuffer, Context.getBlockIntValues(), size);
        System.out.println(Arrays.toString(Context.getBlockIntValues()));
    }
}