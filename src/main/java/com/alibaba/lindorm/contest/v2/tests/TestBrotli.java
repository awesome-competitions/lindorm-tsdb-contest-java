package com.alibaba.lindorm.contest.v2.tests;

import com.nixxcode.jvmbrotli.common.BrotliLoader;
import com.nixxcode.jvmbrotli.dec.BrotliInputStream;
import com.nixxcode.jvmbrotli.enc.BrotliOutputStream;
import com.nixxcode.jvmbrotli.enc.Encoder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ByteArrayInputStream;

public class TestBrotli {

    public static void main(String[] args) throws IOException {
        int batch = 10000;

        byte[][] strings = new byte[][]{
                "GmyKO8&y.SyuSm8.&CeGqu&iO4e'C4e&''4&8y.mGW4'48G.mi#0&ii#Gei0\\Gm#y4[S#KqaOW.mqGmyGiqO.eiSK\\yOu0e&08'0iOW-.O#uWy#\\K'uu.qm0eyi'e-a4\\u'4SaOO#0C4K8.K.\\a48Ca[aKe[40mK[O\\C8K[SG4O['8mu[a.Wa[GCO.yWaO.y4mCW\\4qqeiiei\\#G&y\\[m[\\C-CCa\\#G[8uSy#'mOie&WaSWu".getBytes(),
                "Sy4que-8CCmiiCWSyu'eCa4GGmy4\\m4[0e&--yKSGWumeW.\\00#-.'&O-&4qiOq\\O4.\\C8yqK&qaq#OGummaaa0.-GKO'm-0i.[Oi\\8K#0#0\\ySG.SSa[8.OmCiS\\\\mSyayK&aqa..Sm'Ca[[mG-0&-''''4q[muy#G0'KWmSa#4yKme48OmKuO'[4&iG[q#4[u\\44yuq\\y#.mm8C.-K8iS[Gq[uy.-'&WK&u&4SSm'.WayWaO[OeC\\4C00&-8C&yim\\qqaeCO..uCKWaCSO-qS&-0maO-y#W''WSyeWuGy#-'&'O840O'm8iyO''eO48yyeyy[444#e8WGq#mGqmu40[GyWCC8\\&8SG8qi'GO4&Gu.8[8y.\\KOe-meW.8O'#.\\[WK#u&.S[&a#SaS'S4aC\\eu4CSyyiaGCeuq4e8iuee808u-0WeS-#G80yaOS.[4\\q0C\\8&\\y'W\\q#\\uKKWSeC\\aS[u'yyWaq0..'SS0y.yKGqKG.8yCm'4&m0--S.W0q[uam[eu-0K0S#O&-.mu'\\iaOq#0mKyS0[e-iOGSmia4SC4qyyy8ySa4S[ui#[8iS8yi4[iu8u8K[-aqy[u4&\\K0aGK#4SyKW&0.y'-'0m'\\[C&'-OWu.\\'&WCe-\\.Kq\\i[-G'Kae0WmS.m0[8-eSO[iCaK\\mm#Gy.O[i[.yG\\y[a\\#eG..iuSySGa0[aC4iG-ye8[-C\\8i#O#O[aC\\.C-W#0'OOimCu.84\\e'qaO#-\\a\\[8Kuuq'quS8KCKSOqWSKCyW[.iyKeKOiKuy0'.e[\\8OWq#quqK\\#K&SOueGSi&Cqu-.u&S#8CSmeGiuO-[OWSa&88y&iOG[&''eCum-yau-C['&GG\\[imyaOKC&8#q08mi.m[-\\aa-'W8W'4O.eK0[G8.-#-&-&\\8[4WG[umO8#OO-K[u8ae&4C'&-0&q[mGS\\0'SyG.Saei0\\0#SOeW4S[WK#4CuqC[[4Sy\\-S0.#0GqyW0['e.CO''m.-qK-8.Su#&'eWe0#m'..m&.Ki#Wu'4.\\O8'#O8m#4-#iaO'Oq-CK4i&iaWqqySe8imC\\KCy[Cq0q\\0.eu8.iyya\\ymS4aW[CCqW.ym.KCCa.imem[K#&.[8m0Wim-S[-#u'uC4qS#&8ya4&4&q'-S\\0#CGCq-'[m0uC[i0aCaqi#\\-y4-q#&&Wi0.4q-Cq8a0\\mGqmm.mi-G&'GyKm8C4Kq&K#&4[#[qGy#S4e.Ge'Cy&u-e0mu&CaW4ymi#i&.[8yy'&u4'iq[CS-[-uGa.W8[8yaaGi4#\\O'eyKa8uK\\8a..WamS['C0KS0Om[GiC#4&['G[W.i0CuyKOC[i[e-WCS8'-aCyW8[-ay8''OC\\&a8i&0-GKK0.K88[uqGy#aCSq.ye'iOe.Gmea.'4uyy#8KC0KaWS\\Wq'&q\\[\\[CmS0WiO.G\\KWy4uyWS0y&qOW4&-mqa&W#OyGuOG4a'8[8ySaC.4-0['4yGS0CG&8i\\G.Wqq'OCG44SyWSSK0K\\WyWGu-yy[u-\\-Ce.\\0iu-CO04Syme.Ky\\#08WaquGKKi'44qaq0eq[iq8u'#a.\\.0#yu-4G#0-OuGW-meu#ai&0C".getBytes(),
                "##u-[me.m4SO-uCOC0-i\\qmCauSiO8CC#a&8.\\#0y#8Ky.CO[S#S4OW.G.yWKCCC08K.my0&'a[GK.yyKmC\\K44u-yO'GWSqC\\yu'a-q-\\.KC8uO-a'Geiqmu'Wi8i0q\\iK8&44ee#q8GGmaCqOWeq'&&W8yK\\48y&uSW-y-a\\4KCWei#qO[4a4.OuCGi#Ci[\\4\\[#O&Se-yKaaGW&-y4'u44ii'O4Suu4meCCaa.yWeWyW-K[SCay&Ce'm[-8imy0[[C8u4K0GW'4-y.#a\\.i08Wu'&.S[4iqa[8KiC4CuqGKK-aCGyCWeiqK\\a4S0.imi#4'CqW&qK\\&a'04[yOSCeqG&W[\\-CiS&ua.KCGK&#K\\auqmq'Gu.GuiyCC-G8S#0q#aCO000W[-u-iG8['Kqqu#\\\\m#SO0CWS8KWu[-WaS#iGi'CO'\\eWCG[-WGW4e8ae8iC4&-uO&8u4\\Cy0#\\KKC[\\[#OOi".getBytes(),
        };
        long s = System.currentTimeMillis();
        for (int k = 0; k < batch; k ++){
            for (int i = 0; i < strings.length; i ++){
                byte[] input = strings[i];
                byte[] compressed = compress(input);
                if (k == 0){
                    System.out.println(compressed.length + "/" + input.length + "=" + (compressed.length * 1.0 / input.length));
                }
            }
        }
        System.out.println("compress cost: " + (System.currentTimeMillis() - s));


    }

    static {
        // 确保加载了Brotli的本地库
        BrotliLoader.isBrotliAvailable();
    }

    // 压缩方法
    public static byte[] compress(byte[] data) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        Encoder.Parameters params = new Encoder.Parameters().setQuality(3);
        try (BrotliOutputStream brotliOutput = new BrotliOutputStream(bos, params)) {
            brotliOutput.write(data);
        }
        return bos.toByteArray();
    }

    // 解压缩方法
    public static byte[] decompress(byte[] compressedData) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(compressedData);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (BrotliInputStream brotliInput = new BrotliInputStream(bis)) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = brotliInput.read(buffer)) > 0) {
                bos.write(buffer, 0, len);
            }
        }
        return bos.toByteArray();
    }


        // 压缩方法
//    public static byte[] compress(byte[] data) throws IOException {
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        try (BZip2CompressorOutputStream bzip2 = new BZip2CompressorOutputStream(bos, 9)) {
//            bzip2.write(data);
//        }
//        return bos.toByteArray();
//    }
//
//    // 解压缩方法
//    public static byte[] decompress(byte[] data) throws IOException {
//        ByteArrayInputStream bis = new ByteArrayInputStream(data);
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        try (BZip2CompressorInputStream bzip2 = new BZip2CompressorInputStream(bis)) {
//            byte[] buffer = new byte[1024];
//            int n;
//            while ((n = bzip2.read(buffer)) != -1) {
//                bos.write(buffer, 0, n);
//            }
//        }
//        return bos.toByteArray();
//    }


}
