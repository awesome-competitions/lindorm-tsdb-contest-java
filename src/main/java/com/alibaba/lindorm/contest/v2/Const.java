package com.alibaba.lindorm.contest.v2;

import com.alibaba.lindorm.contest.structs.Row;
import com.alibaba.lindorm.contest.util.Column;
import com.alibaba.lindorm.contest.v2.codec.*;
import com.alibaba.lindorm.contest.v2.codec.Codec;

import java.nio.ByteBuffer;
import java.util.*;

public interface Const {

    // settings
    String TEST_DATA_DIR = "D:\\Workspace\\tests\\test-tsdb\\";
    ArrayList<Row> EMPTY_ROWS = new ArrayList<>();
    Set<String> EMPTY_COLUMNS = new HashSet<>();
    int DATA_FILE_COUNT = 4;

    int K = 1024;
    int M = 1024 * K;
    int G = 1024 * M;
    int BYTE_BUFFER_SIZE = 512 * K;
    int DATA_BUFFER_SIZE = 16 * M;

    // vin
    int VIN_COUNT = 5000;

    // block size
    int BLOCK_SIZE = 200;
    // the time span of all the data
    int TIME_SPAN = 60 * 60 * 10;

    // column
    int COLUMN_COUNT = 60;
    List<String> COLUMNS = new ArrayList<>(COLUMN_COUNT);
    long[] COLUMNS_SIZE = new long[COLUMN_COUNT];

    Map<String, Column> COLUMNS_INDEX = new HashMap<>(COLUMN_COUNT);

    // coding
    Map<String, Codec<int[]>> COLUMNS_INTEGER_CODEC = new HashMap<>(COLUMN_COUNT){{
        // Integer
        put("GMVS", Codec.runLengthIntCodec(BLOCK_SIZE));
        put("LIOS", Codec.deltaOfDeltaIntCodec(3));
        put("LFZG", Codec.deltaOfDeltaIntCodec(3));
        put("WLYQ", Codec.deltaOfDeltaIntCodec(3));
        put("ZTGP", Codec.deltaOfDeltaIntCodec(3));
        put("GMNR", Codec.deltaOfDeltaIntCodec(3));
        put("BTJH", Codec.deltaOfDeltaIntCodec(3));
        put("XCDQ", Codec.deltaOfDeltaIntCodec(3));
        put("JKZL", Codec.deltaOfDeltaIntCodec(3));
        put("DSKQ", Codec.deltaOfDeltaIntCodec(3));
        put("MLBY", Codec.deltaOfDeltaIntCodec(3));
        put("EQLU", Codec.deltaOfDeltaIntCodec(3));
        put("KACE", Codec.deltaOfDeltaIntCodec(3));
        put("YDXB", Codec.deltaOfDeltaIntCodec(3));
        put("NXCR", Codec.deltaOfDeltaIntCodec(3));
        put("OZSM", Codec.deltaOfDeltaIntCodec(3));
        put("SYMN", Codec.deltaOfDeltaIntCodec(3));
        put("UAPJ", Codec.deltaOfDeltaIntCodec(3));
        put("PQRQ", Codec.deltaOfDeltaIntCodec(3));
        put("UICP", Codec.deltaOfDeltaIntCodec(3));
        put("KFGP", Codec.deltaVarIntCodec());
        put("ZZBE", Codec.deltaVarIntCodec());
        put("HJPZ", Codec.deltaVarIntCodec());
        put("JDOE", Codec.deltaVarIntCodec());
        put("KWET", Codec.deltaVarIntCodec());
        put("WNHB", Codec.deltaVarIntCodec());
        put("ENVH", Codec.deltaVarIntCodec());
        put("CSRC", Codec.deltaVarIntCodec());
        put("GONE", Codec.deltaVarIntCodec());
        put("BZPV", Codec.deltaVarIntCodec());
        put("AOAO", Codec.deltaIntCodec(3));
        put("EXGV", Codec.deltaIntCodec(3));
        put("JCGU", Codec.deltaIntCodec(3));
        put("TFGW", Codec.deltaIntCodec(3));
        put("MUBC", Codec.deltaIntCodec(3));
        put("JHET", Codec.deltaIntCodec(3));
        put("WKUZ", Codec.runLengthIntCodec(BLOCK_SIZE));
        put("YCTI", Codec.deltaIntCodec(7));
        put("BBPX", Codec.deltaIntCodec(15));
        put("HRXI", Codec.deltaIntCodec(15));
    }};

    Map<String, Codec<double[]>> COLUMNS_DOUBLE_CODEC = new HashMap<>(COLUMN_COUNT){{
        put("FWKW", Codec.deltaOfDeltaDFCMCodec());
        put("BDPO", Codec.deltaOfDeltaDFCMCodec());
        put("FQTH", Codec.deltaOfDeltaDFCMCodec());
        put("SBXA", Codec.deltaOfDeltaDFCMCodec());
        put("XRTP", Codec.deltaOfDeltaDFCMCodec());
        put("ZIKG", Codec.xorDoubleCodec());
        put("LMLK", Codec.xorDoubleCodec());
        put("TEDW", Codec.xorDoubleCodec());
        put("UVGJ", Codec.xorDoubleCodec());
        put("LYLI", Codec.xorDoubleCodec());
    }};

    Map<String, Codec<ByteBuffer>> COLUMNS_STRING_CODEC = new HashMap<>(COLUMN_COUNT){{
        put("FVXS", Codec.bytesCodec());
        put("LIYD", Codec.bytesCodec());
        put("ZEBY", Codec.bytesCodec());
        put("UFPI", Codec.bytesCodec());
        put("UZSV", Codec.bytesCodec());
        put("FLLY", Codec.bytesCodec());
        put("JUBK", Codec.bytesCodec());
        put("ORNI", Codec.bytesCodec());
        put("SCHU", Codec.bytesCodec());
        put("GLNG", Codec.bytesCodec());
    }};

    Codec<int[]> DEFAULT_INT_CODEC = new DefaultIntCodec();
    Codec<double[]> DEFAULT_DOUBLE_CODEC = new DefaultDoubleCodec();
    Codec<ByteBuffer> DEFAULT_STRING_CODEC = new DefaultBytesCodec();

    // dic status

    Map<String, int[]> STRING_DIC = new HashMap<>(){{
        put("FVXS", new int[256]);
        put("LIYD", new int[256]);
        put("ZEBY", new int[256]);
        put("UFPI", new int[256]);
        put("UZSV", new int[256]);
        put("FLLY", new int[256]);
        put("JUBK", new int[256]);
        put("ORNI", new int[256]);
        put("SCHU", new int[256]);
        put("GLNG", new int[256]);
    }};


}
