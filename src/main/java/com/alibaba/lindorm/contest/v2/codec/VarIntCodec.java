package com.alibaba.lindorm.contest.v2.codec;

import com.alibaba.lindorm.contest.v2.Context;
import net.magik6k.bitbuffer.BitBuffer;
import net.magik6k.bitbuffer.DirectBitBuffer;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class VarIntCodec extends Codec<int[]>{
    @Override
    public void encode(ByteBuffer src, int[] data) {
        BitBuffer buffer = new DirectBitBuffer(src);
        for (int value : data) {
            encodeVarInt(buffer, value);
        }
        buffer.flip();
    }

    @Override
    public int[] decode(ByteBuffer src, int size) {
        int[] data = Context.getBlockIntValues();
        BitBuffer buffer = new DirectBitBuffer(src);
        for (int i = 0; i < size; i++) {
            data[i] = decodeVarInt(buffer);
        }
        return data;
    }

    public static void main(String[] args) {
        VarIntCodec varintCodec = new VarIntCodec();
//        int[] numbers = {47508323,38447491,9735651,48158467,31176995,21444931,30686115,41353795,3464675,12283267,16355299,22657539,37193507,44959939,46392611,4670915};
        int[] numbers = {1364896,287712,1075872,1498688,1017216,285152,1042688,194016,821248,1123488,695520,997760,33728,300160,732832,1511136};

        ByteBuffer encodedBuffer = ByteBuffer.allocate(3000);
        varintCodec.encode(encodedBuffer, numbers);

        encodedBuffer.flip();
        System.out.println(encodedBuffer.remaining());
        System.out.println(numbers.length * 4);

        int size = numbers.length;
        int[] decodedNumbers = varintCodec.decode(encodedBuffer, size);
        System.out.println(Arrays.toString(decodedNumbers));
    }
}
