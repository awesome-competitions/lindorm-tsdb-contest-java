package com.alibaba.lindorm.contest.v2.tests;

import com.alibaba.lindorm.contest.v2.Context;
import com.alibaba.lindorm.contest.v2.codec.*;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestDFCM {

    public static void main(String[] args) {

        List<Codec<double[]>> codecs = new ArrayList<>();
        codecs.add(new XORDoubleCodec());
        codecs.add(new DFCMCodec());
        codecs.add(new BDFCMCodec());
        codecs.add(new DeltaDFCMCodec());
        codecs.add(new DeltaOfDeltaDFCMCodec());
        codecs.add(new DeltaOfDeltaBDFCMCodec());

//        double[] numbers = {-17631.700458325424,-17631.700771279953,-17631.701292870832,-17631.70202309806,-17631.702961961626,-17631.704109461523,-17631.705465597748,-17631.70703037029,-17631.70880377913,-17631.71078582426,-17631.712976505667,-17631.715375823325,-17631.717983777227,-17631.720800367348,-17631.723825593668};
//        double[] numbers = {20155.05092182393,20155.050922867114,20155.050925996657,20155.05093121257,20155.05093851484,20155.050947903474,20155.050959378474,20155.050972939836,20155.05098858756,20155.05100632165,20155.0510261421,20155.051048048914,20155.051072042093,20155.051098121632,20155.051126287533,20155.051156539794};
        double[] numbers = {9973.29309055919, 9973.39245805256,9973.397330989092,9973.383986099192,9973.38891858201,9973.371878204334,9973.376779902414,9973.359826356242,9973.357454257546,9973.362384929032,9973.286227238877,9973.291100168366,9973.274285352003,9973.27918715265,9973.26587114058,9973.270772941281};
//        double[] numbers = {250267.57624034025,250267.57936988556,250267.58458579436,250267.5918880666,250267.6012767023,250267.61275170126,250267.6263130635,250267.64196078893,250267.65969487734,250267.67951532867,250267.70142214268,250267.72541531932,250267.7514948583,250267.7796607595,250267.80991302268};
//        double[] numbers = {-2226421.1215246054,-2226421.152820058,-2226421.2049791464,-2226421.278001869,-2226421.3718882254,-2226421.4866382154,-2226421.622251838,-2226421.778729092,-2226421.956069976,-2226422.1542744893,-2226422.3733426295,-2226422.6132743955,-2226422.8740697857,-2226423.155728798,-2226423.4582514297};
//        double[] numbers = {22614286.67410786,22614286.98706239,22614287.50865327,22614288.238880496,22614289.177744064,22614290.32524396,22614291.681380186,22614293.246152725,22614295.019561566,22614297.0016067,22614299.192288104,22614301.591605764,22614304.199559666,22614307.016149785,22614310.041376106};
//        double[] numbers = {-13061,-14901,-22085,-13557,-15621,-18085,-16757,-19525,-17285,-15253,-13013,-17045,-20613,-17941,-13285,-19381};
//        double[] numbers = {15.5, 14.0625, 3.25, 8.625};
//        double[] numbers = {5453148.210216262,5454141.885149957,5454190.614515289,5454057.16561628,5454106.4904444665,5453936.086667698,5453985.103648492,5453815.568186784,5453791.8471998265,5453841.153914697,5453079.577013142,5453128.306308016,5452960.158144395,5453009.176150855,5452876.016030161,5452925.034037175};

        for (Codec<double[]> codec : codecs) {
            System.out.println(codec.getClass().getSimpleName());
            ByteBuffer encodedBuffer = ByteBuffer.allocate(3000);
            codec.encode(encodedBuffer, numbers, numbers.length);

            encodedBuffer.flip();
            System.out.println(encodedBuffer.remaining());

            int size = numbers.length;
            System.out.println(size * 8);
            codec.decode(encodedBuffer, Context.getBlockDoubleValues(), size);
            System.out.println(Arrays.toString(Context.getBlockDoubleValues()));
        }
    }
}
